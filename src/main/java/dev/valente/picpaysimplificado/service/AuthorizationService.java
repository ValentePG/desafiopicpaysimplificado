package dev.valente.picpaysimplificado.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.valente.picpaysimplificado.dto.AuthorizationDTO;
import dev.valente.picpaysimplificado.web.config.WebProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorizationService {

    private final RestClient.Builder restClient;
    private final WebProperties webProperties;

    public void getAuthorization() {
//        var objectMapper = new ObjectMapper();

        var response = restClient.build()
                .get()
                .uri(webProperties.authorizationUri())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((httpRequest, httpResponse) -> {
                    throw new RuntimeException("Não autorizado");
                }))
                .body(AuthorizationDTO.class);

        log.info("Authorization httpResponse: {}", response);
    }
}
