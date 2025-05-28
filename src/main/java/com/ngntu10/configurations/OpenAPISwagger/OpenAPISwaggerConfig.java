package com.ngntu10.configurations.OpenAPISwagger;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SecurityScheme(
        name = "bearerAuth", // Đổi tên này thành "bearerAuth" để khớp với @SecurityRequirement
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenAPISwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Server productionServer = new Server();
        productionServer.setUrl("https://api.thienducgroup.com.vn/");
        productionServer.setDescription("Server URL in production environment");

        Contact contact = new Contact();
//        contact.setEmail("phamnguyentu04@gmail.com");
//        contact.setName("Pham Nguyen Tu");
//        contact.setUrl("https://www.linkedin.com/in/ngntu10/");

        License mitLicense = new License().name("Apache 2.0").url("https://springdoc.org");

        Info info = new Info()
                .title("CAFFEETRIO API")
                .version("3.0.0")
                .contact(contact)
                .description("This API exposes optimart endpoints.")
//                .termsOfService("https://www.linkedin.com/in/ngntu10/")
                .license(mitLicense);

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, productionServer))
                .addSecurityItem(securityRequirement);
    }
}
