package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.config.WebProperties;
import dev.valente.picpaysimplificado.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotifyConsumer {

    private final RestClient.Builder restClient;
    private final WebProperties webProperties;

    @RabbitListener(queues = "direct_queue")
    public void receive(Transaction transaction) {

        var response = restClient.build()
                .post()
                .uri(webProperties.notifyUri())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (httpRequest, httpResponse) -> {
                    String responseJson = new String(httpResponse.getBody().readAllBytes());
                    throw new RuntimeException(responseJson);
                })
                .toEntity(String.class);

        log.info(" [x] Transação recebida: '{}'", transaction);
    }
}
