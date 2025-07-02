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
 * OpenAPI(Swagger) 구성 클래스
 * API 문서화를 위한 설정
 */
@Configuration
public class OpenApiConfig {

    /**
     * OpenAPI 구성 빈
     * API 문서화를 위한 기본 정보 설정
     *
     * @return OpenAPI 구성 객체
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("분실물 및 습득물 관리 API")
                        .version("1.0")
                        .description("경찰청 분실물 및 습득물 데이터를 제공하는 API 서비스")
                        .contact(new Contact()
                                .name("FindIt Team")
                                .email("support@findit.com")
                                .url("https://findit.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("FindIt API 문서")
                        .url("https://findit.com/docs"))
                .servers(List.of(
                        new Server().url("/").description("기본 서버"),
                        new Server().url("https://api.findit.com").description("운영 서버")
                ));
    }
}
