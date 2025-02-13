package dev.valente.picpaysimplificado.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    @RestartScope
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                .withDatabaseName("picpaysimplificado");
    }

    @Bean
    @ServiceConnection
    @RestartScope
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:4-management"))
                .withExposedPorts(5672, 15672);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);

        Queue directExchangeQueue = new Queue("direct_queue", false);
        rabbitAdmin.declareQueue(directExchangeQueue);

        DirectExchange directExchange = new DirectExchange("direct_exchange");
        rabbitAdmin.declareExchange(directExchange);

        Binding binding = BindingBuilder.bind(directExchangeQueue).to(directExchange)
                .with("direct_routingKey");
        rabbitAdmin.declareBinding(binding);
        return rabbitAdmin;
    }

}
