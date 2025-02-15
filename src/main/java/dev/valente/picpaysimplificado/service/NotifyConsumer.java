package dev.valente.picpaysimplificado.service;

import dev.valente.picpaysimplificado.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotifyConsumer {

    private final NotifyClient notifyClient;

    @RabbitListener(queues = "direct_queue")
    public void receive(Transaction transaction) {
        var convertedDate = transaction.getDate().withOffsetSameInstant(ZoneOffset.ofHours(-3));
        transaction.setDate(convertedDate);
        notifyClient.pushNotification();
        log.info(" [x] Transação recebida: '{}'", transaction);
    }
}
