package com.zuhriddin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zuhriddin.model.Chat;
import com.zuhriddin.service.util.JsonUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChatService implements  BaseService <Chat ,UUID> {

    private static final String PATH = "resource/chats.json";

    @Override
    public Chat add(Chat chat) {
        List<Chat> chats = read();
        chats.add(chat);
        write(chats);
        return chat;
    }

    @Override
    public Chat get(UUID id) {
        List<Chat> chats = read();
        return chats.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("comment not written"));
    }

    @Override
    public List<Chat> list() {
        return read();
    }

    @Override
    public void delete(UUID id) {
        List<Chat> chats = read();
        chats.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(chats::remove);
        write(chats);
    }

    private List<Chat> read() {
        return JsonUtil.read(PATH, new TypeReference<>() {
        });
    }

    private void write(List<Chat> chats) {
        JsonUtil.write(PATH, chats);
    }
}
