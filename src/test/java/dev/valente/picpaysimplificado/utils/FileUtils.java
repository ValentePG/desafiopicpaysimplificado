package dev.valente.picpaysimplificado.utils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Component
@RequiredArgsConstructor
public class FileUtils {

    private final ResourceLoader resourceLoader;

    @SneakyThrows
    public String readFile(String path) {
        var file = resourceLoader.getResource("classpath:" + path).getFile();

        return Files.readString(file.toPath());
    }
}
