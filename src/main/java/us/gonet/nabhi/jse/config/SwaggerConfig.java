package us.gonet.nabhi.jse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket( DocumentationType.SWAGGER_2 ).select()
                .apis( RequestHandlerSelectors.basePackage( "us.gonet.nabhi.jse" ) )
                .apis( RequestHandlerSelectors.any() )
                .paths( PathSelectors.any() )
                .build()
                .apiInfo( apiInfo() );
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Nabhi Middleware",
                "Control de ATM y Servicios transaccionales",
                "API 1.0",
                "Terminos y condiciones",
                new Contact( "Gustavo Mancilla Flores", "www.gonet.us", "gustavo.mancilla@gonet.us" ),
                "License of API", "API license URL", Collections.emptyList() );
    }
}
