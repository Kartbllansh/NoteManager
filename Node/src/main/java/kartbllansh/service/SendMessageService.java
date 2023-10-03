package kartbllansh.service;

import kartbllansh.ButtonForKeyboard;

public interface SendMessageService {
    void sendAnswer(String output, Long chatId);
    void sendMessageAnswerWithInlineKeyboard(String output, long chatId, boolean area, ButtonForKeyboard... buttons);
}
