package com.zuhriddin.bot;

import com.zuhriddin.model.User;
import com.zuhriddin.service.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;


public class RegisterController {

    private static final UserService userService = new UserService();
    private static ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    private static List<KeyboardRow> keyboardRows = new ArrayList<>();
    private static KeyboardRow row = new KeyboardRow();
    private static KeyboardButton keyboardButton = new KeyboardButton();

    public SendMessage buildUser(SendMessage sendMessage, long chatId, Update update, Map<Long, User> userMap) {
        String text = update.getMessage().getText();
        User user = userMap.get(chatId);


        if (user == null) {
            user = new User();
            user.setId(UUID.randomUUID());

            if (text.equalsIgnoreCase("Automatic register")) {
                return sendRequestForContact(update);
            }
            else if (text.equalsIgnoreCase("Manualy register")) {
                userMap.put(chatId, user);
            }
            else {
                return manualyOrAutomaticRequest(chatId);
            }
            userMap.put(chatId, user);

        }

        System.out.println(user.toString());

        switch (user.getCurrentStep()) {
            case 0 -> {
                user.setCurrentStep(1);
                userMap.put(chatId, user);
                sendMessage.setText("Enter name");
            }
            case 1 -> {
                user.setName(text);
                user.setCurrentStep(2);
                userMap.put(chatId, user);
                sendMessage.setText("Enter username");
            }
            case 2 -> {
                user.setUsername(text);
                user.setCurrentStep(3);
                userMap.put(chatId, user);
                sendMessage.setText("Enter your phone Number");
            }
            case 3 -> {
                user.setPhoneNumber(text);
                user.setCurrentStep(4);
                userMap.put(chatId, user);
                sendMessage.setText("Enter password");
            }
            case 4 -> {
                user.setPassword(text);
                user.setChatId(chatId);
                user.setCreatedDate(new Date());
                userMap.put(chatId, user);
                System.out.println(user);
                userService.add(user);
                userMap.remove(chatId);

                printLogin(sendMessage);
            }
        }
        return sendMessage;
    }

    private SendMessage sendRequestForContact(Update update) {
        Long chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Iltimos, telefon raqamingizni ulashing");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton contactButton = new KeyboardButton();
        contactButton.setText("Telefon raqamini ulashish");
        contactButton.setRequestContact(true);

        row.add(contactButton);
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public SendMessage automaticRegister(Contact contact, Long chatId) {
        User user = new User();
        user.setChatId(chatId);
        user.setName(contact.getFirstName());
        user.setUsername(contact.getLastName());
        user.setPhoneNumber(contact.getPhoneNumber());
        userService.add(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Heey sen " + user.getName() + ", instagramga nima bor darsingni qilmaysanmi!!!");
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    public SendMessage manualyOrAutomaticRequest(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Birini tanlang: ");

        keyboardRows = new ArrayList<>();
        keyboardButton.setText("Manualy register");

        row.add(keyboardButton);
        keyboardButton = new KeyboardButton();
        keyboardButton.setText("Automatic register");

        row.add(keyboardButton);

        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;

    }

    public SendMessage sendTextMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    public void printLogin(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Login"));

        replyKeyboardMarkup.setKeyboard(List.of(keyboardRow));
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("You must authenticate.");
    }
}
