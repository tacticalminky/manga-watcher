package com.example.mangawatcher.services;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.mangawatcher.db.models.Manga;

/**
 * 
 * @author Andrew Mink
 * @version Aug 29, 2023
 * @since 1.0
 */
@Service
public class TelegramService extends TelegramLongPollingBot {

    private static String TELEGRAM_BOT_USERNAME;
    private static String TELEGRAM_CHAT_ID;

    public TelegramService(Environment env) {
        super(env.getProperty("TELEGRAM_BOT_TOKEN"));

        TELEGRAM_BOT_USERNAME = env.getProperty("TELEGRAM_BOT_USERNAME");
        TELEGRAM_CHAT_ID = env.getProperty("TELEGRAM_CHAT_ID");
    }

    @Override
    public void onUpdateReceived(Update update) {}

    @Override
    public String getBotUsername() {
        return TELEGRAM_BOT_USERNAME;
    }

    public void notifyOfMangaUpdate(Manga manga, List<String> addedChapters) {
        String text = manga.getTitle() + " was updated.\n";

        if (addedChapters.size() == 1) {
            text += "Chapter " + addedChapters.get(0) + " has been released!";
        } else {
            text += "There are " + addedChapters.size() + " new chapters. Including";

            for (int i = 0; i < addedChapters.size() && i < 5; i++) {
                text += " Chapter " + addedChapters.get(i) + ",";
            }
            
            text = text.substring(0, text.length() - 1);
            text += "!";
        }

        SendMessage message = new SendMessage(TELEGRAM_CHAT_ID, text);
        try {
            execute(message);
        } catch (TelegramApiException ex) { }
    }
}
