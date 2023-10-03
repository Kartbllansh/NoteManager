package kartbllansh.service.impl;

import kartbllansh.dao.NoteDAO;
import kartbllansh.dao.UserDAO;
import kartbllansh.entity.UserTable;
import kartbllansh.service.CallbackMainService;
import kartbllansh.service.NoteManagerService;
import kartbllansh.service.SendMessageService;
import kartbllansh.supplement.StatusUser;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
public class CallbackMainServiceImpl implements CallbackMainService {
    private final UserDAO userDAO;
    private final NoteDAO noteDAO;
    private final NoteManagerService noteManagerService;
    private  final SendMessageService sendMessageService;

    public CallbackMainServiceImpl(UserDAO userDAO, NoteDAO noteDAO, NoteManagerService noteManagerService, SendMessageService sendMessageService) {
        this.userDAO = userDAO;
        this.noteDAO = noteDAO;
        this.noteManagerService = noteManagerService;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void processCallBackMessage(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        var user = findUser(chatId);
        var statusUser = user.getStatusUser();
        String callBackData = update.getCallbackQuery().getData();
        switch (statusUser){
            case GET_NAME_NOTE_ADD, GET_PRIORITY_NOTE_ADD, GET_TEXT_NOTE_ADD -> processAddNote(user, callBackData, chatId);
            case CHOOSE_NOTE_VIEW -> processViewNote(user, callBackData, chatId);
        }
    }

    private void processViewNote(UserTable user, String callBackData, Long chatId) {
        switch (user.getStatusUser()){
            case CHOOSE_NOTE_VIEW -> {
                StringBuilder stringBuilder = noteManagerService.viewNote(user, callBackData);
                user.setStatusUser(StatusUser.COMMON_STATUS);
                userDAO.save(user);
                sendMessageService.sendAnswer(stringBuilder.toString(), chatId);
            }
        }
    }

    private void processAddNote(UserTable user, String callBackData, long chatId) {
        switch (user.getStatusUser()){
            case GET_PRIORITY_NOTE_ADD -> {
               boolean b = noteManagerService.getPriorityNote(user.getActiveAddNote(), user, callBackData);
               if(b) {
                   sendMessageService.sendAnswer("Приоритетность заметки установлена! \n Теперь введите содержание заметки", chatId);
                   user.setStatusUser(StatusUser.GET_TEXT_NOTE_ADD);
                   userDAO.save(user);
               } else {
                   sendMessageService.sendAnswer("К сожалению не удалось установить приоритетность! \n Напишите нам @Kartbllansh", chatId);
               }
               }
        }
    }

    private UserTable findUser( long chatId){
        var optional = userDAO.findByTelegramUserId(chatId);
        return optional.orElse(null);

    }
}
