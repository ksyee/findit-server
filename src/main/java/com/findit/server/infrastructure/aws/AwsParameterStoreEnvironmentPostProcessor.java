package com.findit.server.infrastructure.aws;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathRequest;
import software.amazon.awssdk.services.ssm.model.GetParametersByPathResponse;

/**
 * Loads configuration values from AWS Systems Manager Parameter Store when enabled via
 * {@code aws.ssm.enabled=true}. Parameters located under {@code aws.ssm.parameter-path}
 * are fetched (recursively) and exposed as a high-priority property source.
 */
public class AwsParameterStoreEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AwsParameterStoreEnvironmentPostProcessor.class);
    private static final String PROPERTY_SOURCE_NAME = "aws-ssm-parameters";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        boolean enabled = Boolean.parseBoolean(environment.getProperty("aws.ssm.enabled", "false"));
        if (!enabled) {
            return;
        }

        String parameterPath = environment.getProperty("aws.ssm.parameter-path");
        if (!StringUtils.hasText(parameterPath)) {
            log.warn("AWS SSM integration enabled but 'aws.ssm.parameter-path' is not set.");
            return;
        }

        Region region = Region.of(environment.getProperty("aws.ssm.region", "ap-northeast-2"));
        Map<String, Object> properties = new HashMap<>();

        try (SsmClient ssmClient = SsmClient.builder().region(region).build()) {
            String nextToken = null;
            do {
                GetParametersByPathResponse response = ssmClient.getParametersByPath(
                    GetParametersByPathRequest.builder()
                        .path(parameterPath)
                        .withDecryption(true)
                        .recursive(true)
                        .nextToken(nextToken)
                        .build()
                );

                response.parameters().forEach(parameter -> {
                    String name = parameter.name();
                    String key = name.startsWith(parameterPath)
                        ? name.substring(parameterPath.length())
                        : name;
                    key = key.replaceFirst("^/", "").replace('/', '.');

                    // Map SSM parameter names to Spring Boot property names
                    key = mapParameterName(key);

                    if (StringUtils.hasText(key)) {
                        properties.put(key, parameter.value());
                    }
                });

                nextToken = response.nextToken();
            } while (nextToken != null);

            if (properties.isEmpty()) {
                log.warn("AWS SSM parameter path '{}' returned no entries.", parameterPath);
                return;
            }

            log.info("Loaded {} parameters from AWS SSM path '{}'.", properties.size(), parameterPath);
            environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
        } catch (Exception ex) {
            log.error("Failed to load parameters from AWS SSM path '{}': {}", parameterPath, ex.getMessage(), ex);
        }
    }

    @Override
    public int getOrder() {
        // Ensure values are available before most other property sources.
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }

    /**
     * Maps SSM parameter names to Spring Boot property names.
     * Example: datasource-url -> spring.datasource.url
     */
    private String mapParameterName(String key) {
        Map<String, String> mappings = Map.of(
            "datasource-url", "spring.datasource.url",
            "datasource-username", "spring.datasource.username",
            "datasource-password", "spring.datasource.password",
            "api-security-enabled", "security.api.enabled",
            "api-security-key", "security.api.key",
            "police-api-enabled", "police.api.enabled"
        );
        return mappings.getOrDefault(key, key);
    }
}
