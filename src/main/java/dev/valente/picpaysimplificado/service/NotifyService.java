package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final RabbitTemplate rabbitTemplate;

    public void notify(Transaction transaction) {
        rabbitTemplate.convertAndSend("testeexchange", "testeroutingkey", transaction);
    }
}
