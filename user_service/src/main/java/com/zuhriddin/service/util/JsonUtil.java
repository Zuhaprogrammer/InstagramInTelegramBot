package com.zuhriddin.service.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.util.List;

@UtilityClass
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public <T> void write(String path, T t) {
        objectMapper.writeValue(new File(path), t);
    }

    public <T> List<T> read(String path, TypeReference<List<T>> typeReference) {
        System.out.println(new File(path).exists());
        try {
            return objectMapper.readValue(new File(path), typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
