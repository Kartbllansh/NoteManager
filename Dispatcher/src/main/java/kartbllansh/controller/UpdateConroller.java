package kartbllansh.controller;

import kartbllansh.service.UpdateProducer;
import kartbllansh.utils.MessageUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static kartbllansh.RabbitQueue.CALLBACK_UPDATE_CODEX;
import static kartbllansh.RabbitQueue.UPDATE_CODEX;

@Component
@Log4j
public class UpdateConroller {

    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;
    private TelegramBot telegramBot;



    public UpdateConroller(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessagesByType(update);
        } else if (update.hasCallbackQuery()) {
            processCallBackQuery(update);
        } else {
            log.error("Unsupported message type is received: " + update);
        }
    }

    private void processCallBackQuery(Update update) {
        updateProducer.produce(CALLBACK_UPDATE_CODEX, update);
    }


    private void distributeMessagesByType(Update update) {
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Неподдерживаемый тип сообщений");
        setView(sendMessage);
        log.error("Получен неподдерживаемый тип сообщения");

    }

    public void setView(SendMessage sendMessage) {
        Integer messageId =  telegramBot.sendAnswerMessage(sendMessage);

    }


    public void setViewWithCallBack(EditMessageText editMessageText){
        try {
            telegramBot.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error(editMessageText.getText()+" "+e);
            throw new RuntimeException(e);
        }
    }
    public void setViewDeleteMessage(DeleteMessage deleteMessage){
        try {
            telegramBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void processPhotoMessage(Update update) {
        setUnsupportedMessageTypeView(update);
    }

    private void processDocMessage(Update update) {
        setUnsupportedMessageTypeView(update);

    }

    private void processTextMessage(Update update) {
        updateProducer.produce(UPDATE_CODEX, update);
    }
}

