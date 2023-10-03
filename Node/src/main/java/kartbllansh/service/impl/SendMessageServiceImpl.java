package kartbllansh.service.impl;

import kartbllansh.ButtonForKeyboard;
import kartbllansh.service.ProducerService;
import kartbllansh.service.SendMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendMessageServiceImpl implements SendMessageService {
    private final ProducerService producerService;

    public SendMessageServiceImpl(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }
    @Override
    public void sendMessageAnswerWithInlineKeyboard(String output, long chatId, boolean area, ButtonForKeyboard... buttons) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        sendMessage.setReplyMarkup(doButtonInlineKeyboard(area ,buttons));
        producerService.producerAnswer(sendMessage);
    }

    private InlineKeyboardMarkup doButtonInlineKeyboard(boolean area, ButtonForKeyboard... buttons) {
        InlineKeyboardMarkup markupInlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        int buttonsPerRow = area ? 2 : 1; // Определяем количество кнопок в строке

        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (int i = 0; i < buttons.length; i++) {
            ButtonForKeyboard button = buttons[i];
            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
            inlineButton.setText(button.getText());
            inlineButton.setCallbackData(button.getCallbackData());

            currentRow.add(inlineButton);

            if (currentRow.size() >= buttonsPerRow || i == buttons.length - 1) {
                rowsInline.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        markupInlineKeyboard.setKeyboard(rowsInline);
        return markupInlineKeyboard;
    }
}
