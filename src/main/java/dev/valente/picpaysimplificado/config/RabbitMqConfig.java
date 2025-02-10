package dev.valente.picpaysimplificado.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Declarables declarables() {
        Queue directExchangeQueue = new Queue("direct_queue", false);

        DirectExchange directExchange = new DirectExchange("testeexchange");

        return new Declarables(directExchangeQueue, directExchange,
                BindingBuilder
                        .bind(directExchangeQueue)
                        .to(directExchange).with("testeroutingkey"));
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
