package com.zuhriddin.bot;

import com.zuhriddin.model.Notification;
import com.zuhriddin.model.User;
import com.zuhriddin.service.NotificationService;
import com.zuhriddin.service.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


public class Bot extends TelegramLongPollingBot {

UserService userService = new UserService();
    private static final Map<Long, User> followings = new HashMap<>();

    private static final FollowController followController = new FollowController();
    private static final NotificationController notificationController = new NotificationController();
    private static final NotificationService notificationService = new NotificationService();

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            System.out.println(chatId);
            String messageText = update.getMessage().getText();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            if (messageText.equalsIgnoreCase("/Notifications")) {
                notificationController.sendNotificationMenu(sendMessage);
                sendNotifications(chatId);
            }
            else if (messageText.equalsIgnoreCase("/Search")) {
                showUser(sendMessage,messageText);
            }
            else if (messageText.equalsIgnoreCase("/Account")) {
                User user;
                if (update.hasCallbackQuery()) {
                    UUID followingId = UUID.fromString(update.getCallbackQuery().getData());
                    user = userService.get(followingId);
                    followings.put(chatId, user);
                } else {
                    user = userService.getUserByChatId(chatId);
                }
                followController.sendFollowMenu(sendMessage,chatId, user);
            } else if (messageText.equalsIgnoreCase("/Followers")) {
                followController.sendFollowers(chatId);
            } else if (messageText.equalsIgnoreCase("/Followings")) {
                followController.sendFollowings(chatId);
            } else if (messageText.equalsIgnoreCase("/Follow ")) {
                User following = followings.get(chatId);
                followController.followUser(chatId, following.getId());
            }

            try {
                execute(sendMessage); // Telegram API orqali xabarni yuborish
            } catch (TelegramApiException e) {
                e.printStackTrace(); // Xatolarni qayta ishlash
            }
        }
    }

     public void showUser(SendMessage sendMessage,String messageText){
         User foundUser = userFind(messageText);
         if (foundUser != null) {
             sendMessage.setText("User found: " + foundUser.getUsername());
             InlineKeyboardMarkup i = new InlineKeyboardMarkup();
             List<List<InlineKeyboardButton>> rows = new ArrayList<>();
             List<InlineKeyboardButton> row = new ArrayList<>();
             InlineKeyboardButton a = new InlineKeyboardButton();
             a.setText("account");
             a.setCallbackData(String.valueOf(foundUser.getId()));
             row.add(a);
             rows.add(row);
             i.setKeyboard(rows);
             sendMessage.setReplyMarkup(i);
         } else {
             sendMessage.setText("User not found.");
         }
     }

    public void sendNotifications(long chatId) {
        User user = userService.getUserByChatId(chatId);
        List<Notification> notifications = notificationService.notificationList(user.getId());

        for (Notification notification : notifications) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(notification.getId().toString());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public User userFind(String username) {
        return userService.getUserByUserName(username);
    }

    @Override
    public String getBotUsername() {
        return "NameReverseBot"; // Botingizning foydalanuvchi nomi
    }

    @Override
    public String getBotToken() {
        return "7514452696:AAHPL-WuLhvL3UFCnUf_a5r41aaOwmSwObM"; // Botingizning tokeni
    }

}
