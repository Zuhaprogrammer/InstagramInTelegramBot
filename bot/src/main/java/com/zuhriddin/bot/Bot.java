package com.zuhriddin.bot;


import com.zuhriddin.model.User;
import com.zuhriddin.service.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.*;


public class Bot extends TelegramLongPollingBot {
    private static final String BOT_USERNAME = "https://t.me/buMenikiBot";
    private static final String BOT_TOKEN = "7547076537:AAHjqr5nQWdOXbwEJBYNRn6YDm3NZEyqWss";
    private static final UserService userService = new UserService();
    private Map<Long, User> userMap = new HashMap<>();
    private static final RegisterController registerControl = new RegisterController();
    private static SendMessage sendMessage = new SendMessage();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().hasContact()) {
           sendMessage = registerControl.automaticRegister(update.getMessage().getContact(), update.getMessage().getChatId());
        }
        Long chatId = update.getMessage().getChatId();
        User user = null;

        if (!userMap.containsKey(chatId)) {
            user = userService.checkUserByChatId(chatId);

        }
        if (user == null) {
            sendMessage = registerControl.buildUser(chatId, update);

        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

}
