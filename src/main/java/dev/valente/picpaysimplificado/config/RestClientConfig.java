package dev.valente.picpaysimplificado.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final WebProperties webProperties;

    @Bean
    public RestClient.Builder getRestClientBuilder() {
        return RestClient.builder()
                .baseUrl(webProperties.url());
    }
}
