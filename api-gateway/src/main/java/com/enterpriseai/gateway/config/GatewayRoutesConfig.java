package com.enterpriseai.gateway.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GatewayRoutesConfig {

    @Bean
    RouteLocator platformRoutes(RouteLocatorBuilder routes,
                                @Value("${platform.service-uri:http://localhost:8081}") URI platformUri) {
        return routes.routes()
            .route("platform-auth", route -> route
                .path("/api/auth/**")
                .filters(filters -> filters
                    .addRequestHeader("X-Gateway-Name", "enterprise-ai-gateway")
                    .retry(2))
                .uri(platformUri))
            .route("platform-api", route -> route
                .path("/api/**")
                .filters(filters -> filters
                    .addRequestHeader("X-Gateway-Name", "enterprise-ai-gateway")
                    .addResponseHeader("X-Platform", "Enterprise-AI-Assistant-Management"))
                .uri(platformUri))
            .build();
    }
}
