package com.javarush.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TelegramBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = ""; // Add Bot name
    public static final String TELEGRAM_BOT_TOKEN = ""; //Add Telegram token
    public static final String OPEN_AI_TOKEN = ""; //Add ChatGPT token

    private ArrayList<String> list = new ArrayList<>();
    public TelegramBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }
    private ChatGPTService gptService = new ChatGPTService(OPEN_AI_TOKEN);
    public DialogMode dialogMode = null;
    @Override
    public void onUpdateEventReceived(Update update) {

        String message = getMessageText();

        if (message.equals("/start")){
            dialogMode = DialogMode.MAIN;
            sendPhotoMessage("avatar_main");
            String text = loadMessage("main");
            sendTextMessage(text);

            showMainMenu("Старт", "/start", "Общение с ИИ", "/gpt");
            return;
        }

        if (message.equals("/gpt")){
            dialogMode = DialogMode.GPT;
            String text = loadMessage("gpt");
            sendTextMessage(text);
            return;
        }

        if (dialogMode == DialogMode.GPT){
            String prompt = loadPrompt("gpt");
            String answer = gptService.sendMessage(prompt, message);
            sendTextMessage(answer);
        }

    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TelegramBoltApp());
    }
}