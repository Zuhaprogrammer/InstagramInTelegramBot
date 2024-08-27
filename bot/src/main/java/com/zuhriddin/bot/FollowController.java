package com.zuhriddin.bot;

import com.zuhriddin.model.Follow;
import com.zuhriddin.model.User;
import com.zuhriddin.service.FollowService;
import com.zuhriddin.service.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FollowController {
 UserService userService = new UserService();
    private final FollowService followService = new FollowService();

    public void sendFollowMenu(SendMessage sendMessage, Long chatId, User user) {
        InlineKeyboardMarkup i = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        List<String> a = List.of("Posts","Followers","Followings","Follow");
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (int j = 1; j <= a.size(); j++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(a.get(j - 1));
            button.setCallbackData(a.get(j - 1));
            buttons.add(button);
        }
        list.add(buttons);
        i.setKeyboard(list);
        sendMessage.setReplyMarkup(i);
        sendMessage.setText("Account");
    }

    public SendMessage sendFollowers(Long chatId) {
        User user = userService.getUserByChatId(chatId);
        List<Follow> followers = followService.getFollowers(user.getId());
        StringBuilder responseText = new StringBuilder("Your Followers:\n");
        for (Follow follower : followers) {
            responseText.append(follower.getFollowerId().toString()).append("\n");
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(responseText.toString());
       return message;

    }

    public SendMessage sendFollowings(Long chatId) {
        User user = userService.getUserByChatId(chatId);
        List<Follow> followings = followService.getFollowings(user.getId());

        StringBuilder responseText = new StringBuilder("You are Following:\n");
        for (Follow following : followings) {
            responseText.append(following.getFollowingId().toString()).append("\n");
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(responseText.toString());
       return  message;
    }

    public SendMessage followUser(Long chatId,UUID followingId) {
        try {
            User user = userService.getUserByChatId(chatId);
            Follow follow = new Follow(UUID.randomUUID(), user.getId(), followingId);
            followService.add(follow);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("You have successfully followed: " );
            return message;
        } catch (Exception e) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Failed to follow the user.");
            return message;
        }
    }
}
