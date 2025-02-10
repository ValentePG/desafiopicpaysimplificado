package dev.valente.picpaysimplificado.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web")
public record WebProperties(String url, String authorizationUri, String notifyUri) {
}
