package by.vitikova.discovery.config;

import by.vitikova.discovery.client.UserClient;
import by.vitikova.discovery.interseptor.WebClientAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientUserConfig {

    @Value("${user-service.url}")
    private String pathUserService;

    @Bean
    public UserClient userClient(WebClient.Builder builder, WebClientAuthorizationFilter authFilter) {
        WebClient webClient = builder
                .filter(authFilter)
                .baseUrl(pathUserService)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return factory.createClient(UserClient.class);
    }
}