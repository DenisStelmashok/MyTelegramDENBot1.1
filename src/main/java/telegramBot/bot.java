package telegramBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class bot extends TelegramLongPollingBot {
    private final String BOT_NAME = "MyTelegramDENBot";
    private final String BOT_TOKEN = "5340474377:AAFHFromLlOhGka4ZDs3BgVVGeOGuv7K148";

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi((Class<? extends BotSession>) DefaultBotSession.class);
            botsApi.registerBot((LongPollingBot) new bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            setButtons(answer);
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message msg = update.getMessage();
        Message city = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = getUserName(msg);
        //String answer = "Дорогой " + userName + " вы написали: " + msg.getText();
        //setAnswer(chatId, userName, answer);
        try {
            String answer = "Дорогой " + userName + Weather.getWeather(city.getText(), model);
            setAnswer(chatId, userName, answer);
        } catch (IOException e) {
            Throwable e1=e;
            while(e1.getCause()!=null){e1=e1.getCause();}
            String answer = "Дорогой "+ userName + " случилась беда "+ e1.getMessage();
            setAnswer(chatId, userName, answer);
        }
    }

    /**
     * public void sendMsg (Message message, String text){
     * SendMessage sendMessage = new SendMessage();
     * //Model model = new Model();
     * sendMessage.enableMarkdown(true);
     * sendMessage.setChatId(message.getChatId().toString());
     * sendMessage.setReplyToMessageId(message.getMessageId());
     * sendMessage.setText(text);
     * setButtons(sendMessage);
     * //setButtons(sendMessage);
     * //sendMessage(sendMessage);
     * //sendMsg(message, Weather.getWeather(message.getText(), model));
     * }
     */

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Гомель"));
        keyboardFirstRow.add(new KeyboardButton("Брянск"));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }
}