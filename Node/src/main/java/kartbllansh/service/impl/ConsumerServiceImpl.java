package kartbllansh.service.impl;

import kartbllansh.service.CallbackMainService;
import kartbllansh.service.ConsumerService;
import kartbllansh.service.MainService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static kartbllansh.RabbitQueue.CALLBACK_UPDATE_CODEX;
import static kartbllansh.RabbitQueue.UPDATE_CODEX;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;
    private final CallbackMainService callbackMainService;


    public ConsumerServiceImpl(MainService mainService, CallbackMainService callbackMainService) {
        this.mainService = mainService;

        this.callbackMainService = callbackMainService;
    }

    @Override
    @RabbitListener(queues = UPDATE_CODEX)
    public void consumeTextMessageUpdates(Update update) {
        mainService.processTextMessage(update);


    }

    @Override
    @RabbitListener(queues = CALLBACK_UPDATE_CODEX)
    public void consumeCallBackMessage(Update update) {
        callbackMainService.processCallBackMessage(update);
    }
}
