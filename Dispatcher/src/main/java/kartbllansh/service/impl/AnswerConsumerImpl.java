package kartbllansh.service.impl;

import kartbllansh.controller.UpdateConroller;
import kartbllansh.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static kartbllansh.RabbitQueue.ANSWER_CODEX;

@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateConroller updateConroller;

    public AnswerConsumerImpl(UpdateConroller updateConroller) {
        this.updateConroller = updateConroller;
    }

    @Override
    @RabbitListener(queues = ANSWER_CODEX)
    public void consume(SendMessage sendMessage) {
        updateConroller.setView(sendMessage);


    }

   /* @Override
    @RabbitListener(queues = ANSWER_CALLBACK_ANSWER)
    public void consumeWithCallBack(EditMessageText editMessageText) {
        updateConroller.setViewWithCallBack(editMessageText);
    }

    @Override
    @RabbitListener(queues = DELETE_MESSAGE_ANSWER)
    public void consumeDeleteMessage(DeleteMessage deleteMessage) {
        updateConroller.setViewDeleteMessage(deleteMessage);
    }*/
}
