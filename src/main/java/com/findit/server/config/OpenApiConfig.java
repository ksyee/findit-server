package com.findit.server.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI(Swagger) uad6cuc131 ud074ub798uc2a4
 * API ubb38uc11cud654ub97c uc704ud55c uc124uc815
 */
@Configuration
public class OpenApiConfig {

    /**
     * OpenAPI uad6cuc131 ube48
     * API ubb38uc11cud654ub97c uc704ud55c uae30ubcf8 uc815ubcf4 uc124uc815
     * 
     * @return OpenAPI uad6cuc131 uac1duccb4
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ubd84uc2e4ubb3c ubc0f uc2b5ub4ddubb3c uad00ub9ac API")
                        .version("1.0")
                        .description("uacbducc30uccad ubd84uc2e4ubb3c ubc0f uc2b5ub4ddubb3c ub370uc774ud130ub97c uc81cuacf5ud558ub294 API uc11cube44uc2a4")
                        .contact(new Contact()
                                .name("FindIt Team")
                                .email("support@findit.com")
                                .url("https://findit.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("FindIt API ubb38uc11c")
                        .url("https://findit.com/docs"))
                .servers(List.of(
                        new Server().url("/").description("uae30ubcf8 uc11cubc84"),
                        new Server().url("https://api.findit.com").description("uc6b4uc601 uc11cubc84")
                ));
    }
}
