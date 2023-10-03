package kartbllansh.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackMainService {
    void processCallBackMessage(Update update);
}
