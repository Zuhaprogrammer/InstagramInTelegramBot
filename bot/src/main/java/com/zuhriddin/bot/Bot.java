package com.zuhriddin.bot;


import com.zuhriddin.enumeration.PostType;
import com.zuhriddin.model.Post;
import com.zuhriddin.model.User;
import com.zuhriddin.service.PostService;
import com.zuhriddin.service.UserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;


public class Bot extends TelegramLongPollingBot {
    private static final String BOT_USERNAME = "https://t.me/my_example_telegram_bot";
    private static final String BOT_TOKEN = "6951873817:AAGdT8rDVAC24zE5WJUBhis6j_-VxFKvq90";
    private static final UserService userService = new UserService();
    private final Map<Long, User> userMap = new HashMap<>();
    private final Map<Long, String> loginMap = new HashMap<>();
    private final Map<Long, Post> postMap = new HashMap<>();
    private static final RegisterController registerControl = new RegisterController();
    private static final PostController postController = new PostController();
    private static final PostService postService = new PostService();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            Post post = postMap.get(chatId);

            if (post != null) {
                if (post.getStep() == 3) {
                    setImage(message, update, post, chatId);
                }
            }

            if (update.getMessage().hasContact()) {
                sendMessage = registerControl.automaticRegister(update.getMessage().getContact(), update.getMessage().getChatId());
            }
            User user = null;

            if (!userMap.containsKey(chatId)) {
                user = userService.checkUserByChatId(chatId);
            }
            mainLogics(sendMessage, user, chatId, update, message);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void mainLogics(SendMessage sendMessage, User user, Long chatId, Update update, Message message) {
        if (user == null) {
            sendMessage = registerControl.buildUser(sendMessage, chatId, update, userMap);
        }
        else if (message.getText().equalsIgnoreCase("/start")) {
            registerControl.printLogin(sendMessage);
        } else if (message.getText().equalsIgnoreCase("login")) {
            sendMessage.setText("Enter your phoneNumber:");
            loginMap.put(chatId, "phoneNumber");
        } else if ("phoneNumber".equalsIgnoreCase(loginMap.get(chatId))) {
            loginMap.put(chatId, message.getText());
            sendMessage.setText("Enter your password:");
        } else if (loginMap.get(chatId) != null && !"phoneNumber".equalsIgnoreCase(loginMap.get(chatId))) {
            User user1 = userService.login(chatId, loginMap.get(chatId), message.getText());
            if (user1 != null) {
                mainPage(sendMessage);
            }
            loginMap.remove(chatId);
        } else if (message.getText().equalsIgnoreCase("home")) {
            mainPage(sendMessage);
        } else if (message.getText().equalsIgnoreCase("addPost")) {
            postController.addPost(sendMessage, user, chatId, postMap);
        }

        postController.buildPost(postMap, chatId, message, sendMessage);
    }

    private void mainPage(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Home"));
        keyboardRow.add(new KeyboardButton("Search"));
        keyboardRow.add(new KeyboardButton("AddPost"));
        keyboardRow.add(new KeyboardButton("Account"));
        keyboardRow.add(new KeyboardButton("Notification"));

        replyKeyboardMarkup.setKeyboard(List.of(keyboardRow));
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("Well Come!");
    }

    private void setImage(Message message, Update update, Post post, Long chatId) {
        if (message.hasVideo()) {
            saveVideo(update, constructPostPath(post.getId(), ".mp4"));
            post.setPath(constructPostPath(post.getId(), ".mp4"));
            post.setPostType(PostType.VIDEO);
        } else if (message.hasPhoto()) {
            savePhoto(update, constructPostPath(post.getId(), ".jpg"));
            post.setPath(constructPostPath(post.getId(), ".jpg"));
            post.setPostType(PostType.PHOTO);
        }
        post.setCreateDate(new Date());
        postService.add(post);
        postMap.remove(chatId);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private String constructUserPath(String username) {
        return "instagram/resource/users/" + username + ".jpg";
    }

    private String constructPostPath(UUID postId, String type) {
        return "instagram/resource/posts/" + postId + type;
    }

    public void savePhoto(Update update, String path) {
        String fileId = update.getMessage().getPhoto().get(update.getMessage().getPhoto().size() - 1).getFileId();

        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            java.io.File downloadedFile = downloadFile(file.getFilePath());

            saveFileToCustomLocation(downloadedFile, path);
            System.out.println("File is downloaded: " + downloadedFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveVideo(Update update, String path) {
        String fileId = update.getMessage().getVideo().getFileId();

        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            java.io.File downloadedFile = downloadFile(file.getFilePath());

            saveFileToCustomLocation(downloadedFile, path);
            System.out.println("File is downloaded: " + downloadedFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFileToCustomLocation(java.io.File sourceFile, String destinationPath) {
        try {
            Path destination = Path.of(destinationPath);
            Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File is saved: " + destination.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
