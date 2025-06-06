package io.github.tacticalminky.mangawatcher.services;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import io.github.tacticalminky.mangawatcher.db.models.Manga;

/**
 * TODO: validate token, username, and id
 *
 * @author Andrew Mink
 * @version June 1, 2025
 * @since 1.0.0
 */
@Service
public class TelegramService extends TelegramLongPollingBot {
    private static boolean enabled = false;

    private static String TELEGRAM_BOT_USERNAME;
    private static String TELEGRAM_CHAT_ID;

    public TelegramService(Environment env) {
        super(env.getProperty("TELEGRAM_BOT_TOKEN"));

        if (!env.getProperty("TELEGRAM_BOT_TOKEN").isEmpty()) {
            enabled = true;

            TELEGRAM_BOT_USERNAME = env.getProperty("TELEGRAM_BOT_USERNAME");
            TELEGRAM_CHAT_ID = env.getProperty("TELEGRAM_CHAT_ID");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotUsername() {
        return TELEGRAM_BOT_USERNAME;
    }

    public void notifyOfMangaUpdate(Manga manga, List<String> addedChapters) {
        Assert.notNull(manga, "Manga can not be null");
        Assert.notNull(addedChapters, "Added chapters can not be null");

        if (!enabled) {
            return;
        }

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
        } catch (TelegramApiException ex) {
            // TODO: log
        }
    }

}
