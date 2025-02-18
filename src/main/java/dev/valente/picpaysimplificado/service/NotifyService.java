package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotifyService {

    private final RabbitTemplate rabbitTemplate;

    public void notify(Transaction transaction) {
        log.info("Enviando transação: '{}'", transaction);
        rabbitTemplate.convertAndSend("direct_exchange", "direct_routingKey", transaction);
    }
}
