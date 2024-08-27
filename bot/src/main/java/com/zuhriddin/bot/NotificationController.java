package com.zuhriddin.bot;

import com.zuhriddin.model.Notification;
import com.zuhriddin.model.User;
import com.zuhriddin.service.NotificationService;
import com.zuhriddin.service.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.List;



public class NotificationController {

    public void sendNotificationMenu(SendMessage message) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Notifications"));
        keyboardMarkup.setKeyboard(List.of(row1));
        message.setText("You have got new notifications");
        message.setReplyMarkup(keyboardMarkup);
    }


}
