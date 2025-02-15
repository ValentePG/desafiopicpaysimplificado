package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.config.WebProperties;
import dev.valente.picpaysimplificado.exception.UnnavailableServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class NotifyClient {

    private final RestClient.Builder restClient;
    private final WebProperties webProperties;

    public void pushNotification() {
        restClient.build()
                .post()
                .uri(webProperties.notifyUri())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (httpRequest, httpResponse) -> {
                    String responseJson = new String(httpResponse.getBody().readAllBytes());
                    throw new UnnavailableServiceException(responseJson);
                })
                .toEntity(String.class);
    }
}
