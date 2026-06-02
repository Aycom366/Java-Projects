package com.aycom.feedback_app.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Feedback App API").version("1.0"));
    }

    @Bean
    OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            RequestMapping classMapping = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
            String basePath = (classMapping != null && classMapping.value().length > 0)
                    ? classMapping.value()[0]
                    : "";

            if (basePath.startsWith("/api/auth")) {
                return operation;
            }

            operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));

            operation.addParametersItem(new Parameter()
                    .in("header")
                    .name("X-Organization-Id")
                    .required(true)
                    .schema(new StringSchema()));

            return operation;
        };
    }
}
