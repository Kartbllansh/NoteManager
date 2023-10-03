package kartbllansh.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken){
        super( botToken);
        log.info("Constructor-token work");
    }
    @Autowired
    @Override
    public String getBotUsername() {
        log.info("check username");
        return botName;
    }

    private UpdateConroller updateConroller;
    @Autowired
    public void setUpdateConroller(UpdateConroller updateConroller) {
        this.updateConroller = updateConroller;
    }

    @PostConstruct
    public void afterPropertiesSet() {
        if (updateConroller != null) {
            updateConroller.registerBot(this);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Есть сообщение");
        updateConroller.processUpdate(update);
    }




    public Integer sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                return execute(message).getMessageId();
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
        return null;
    }
}

