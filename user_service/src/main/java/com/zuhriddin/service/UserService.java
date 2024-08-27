package com.zuhriddin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zuhriddin.model.User;
import com.zuhriddin.service.util.JsonUtil;

import java.util.List;
import java.util.UUID;

public class UserService implements BaseService<User, UUID>{
    private static final String PATH = "user_service/src/file/users.json";

    @Override
    public User add(User user) {
        List<User> users = read();
        if (!has(user,users)) {
            users.add(user);
            write(users);
            return user;
        }
        throw new RuntimeException("This user already exists");
    }

    private boolean has(User user, List<User> users) {
        return users.stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()) &&
                        u.getPhoneNumber().equals(user.getPhoneNumber()));
    }

    @Override
    public User get(UUID id) {
        List<User> users = read();
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No such user, found"));
    }

    @Override
    public List<User> list() {
        return read();
    }

    @Override
    public void delete(UUID id) {
        List<User> users = read();
        users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .ifPresent(users::remove);
        write(users);
    }

    public User login(Long chatId, String phoneNumber, String password) {
        List<User> users = read();

        User user = checkUserByChatId(chatId);

        if (user != null) {
            if (user.getPhoneNumber().equals(phoneNumber) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByUserName(String username) {
        List<User> users = read();
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User checkUserByChatId(Long chatId) {
        List<User> list = read();

        return list.stream()
                .filter(n -> n.getChatId().equals(chatId))
                .findFirst()
                .orElse(null);

    }

    private List<User> read() {
        System.out.println("Read");
        return JsonUtil.read(PATH, new TypeReference<>() {});
    }

    private void write(List<User> users) {
        JsonUtil.write(PATH, users);
    }

}
