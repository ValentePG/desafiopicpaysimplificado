package dev.valente.picpaysimplificado;

import dev.valente.picpaysimplificado.config.WebProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WebProperties.class)
public class PicpaysimplificadoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicpaysimplificadoApplication.class, args);
    }

}
