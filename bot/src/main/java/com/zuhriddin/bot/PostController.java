package com.zuhriddin.bot;

import com.zuhriddin.model.Post;
import com.zuhriddin.model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.UUID;

public class PostController {
    public void addPost(SendMessage sendMessage, User user, Long chatId, Map<Long, Post> postMap) {
        sendMessage.setText("Enter title of post:");
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setUserId(user.getId());
        post.setStep(1);
        postMap.put(chatId, post);
    }

    public void buildPost(Map<Long, Post> postMap, Long chatId, Message message, SendMessage sendMessage) {
        Post post = postMap.get(chatId);

        if (post != null) {
            if (post.getStep() == 1 && !message.getText().equalsIgnoreCase("addPost")) {
                post.setTitle(message.getText());
                post.setStep(2);
                postMap.put(chatId, post);
                sendMessage.setText("Enter location of post:");
            } else if (post.getStep() == 2) {
                post.setLocation(message.getText());
                post.setStep(3);
                postMap.put(chatId, post);
                sendMessage.setText("Enter photo or video for post:");
            }
        }
        System.out.println(post);
    }


}
