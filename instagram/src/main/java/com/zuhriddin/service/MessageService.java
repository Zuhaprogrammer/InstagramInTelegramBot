package com.zuhriddin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zuhriddin.model.Message;
import com.zuhriddin.service.util.JsonUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MessageService implements BaseService<Message, UUID> {
    private static final String PATH = "";

    @Override
    public Message add(Message message) {
        List<Message> messeges = read();
        messeges.add(message);
        write(messeges);
        return message;
    }

    @Override
    public Message get(UUID id) {
        return read().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message not found!"));
    }

    @Override
    public List<Message> list() {
        return read();
    }

    @Override
    public void delete(UUID id) {
        List<Message> messeges = read().stream()
                .filter(m -> !m.getId().equals(id))
                .collect(Collectors.toList());
        write(messeges);
    }

    public List<Message> messegesList(UUID id) {

    }

    private List<Message> read() {
        return JsonUtil.read(PATH, new TypeReference<>() {
        });
    }

    private void write(List<Message> messeges) {
        JsonUtil.write(PATH, messeges);
    }
}
