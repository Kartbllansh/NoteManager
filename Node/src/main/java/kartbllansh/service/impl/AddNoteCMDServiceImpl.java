package kartbllansh.service.impl;

import kartbllansh.ButtonForKeyboard;
import kartbllansh.dao.UserDAO;
import kartbllansh.entity.NoteTable;
import kartbllansh.entity.UserTable;
import kartbllansh.service.AddNoteCMDService;
import kartbllansh.service.NoteManagerService;
import kartbllansh.service.SendMessageService;
import kartbllansh.supplement.StatusUser;
import org.springframework.stereotype.Service;

@Service
public class AddNoteCMDServiceImpl implements AddNoteCMDService {
    private final UserDAO userDAO;
    private final NoteManagerService noteManagerService;
    private final SendMessageService sendMessageService;

    public AddNoteCMDServiceImpl(UserDAO userDAO, NoteManagerService noteManagerService, SendMessageService sendMessageService) {
        this.userDAO = userDAO;
        this.noteManagerService = noteManagerService;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void processAddNoteCMD(UserTable userTable, String text, long chatId) {

        var statusUserAddNote = userTable.getStatusUser();
        switch (statusUserAddNote){
            case GET_NAME_NOTE_ADD -> {
                NoteTable noteTable = new NoteTable();
                getNameNote(noteTable, userTable, chatId, text);
            }
            case GET_PRIORITY_NOTE_ADD -> getPriorityNote(userTable.getActiveAddNote(), userTable, chatId, text);
            case GET_TEXT_NOTE_ADD -> getTextNote(userTable.getActiveAddNote(), userTable, chatId, text);

        }
    }

    private void getTextNote(NoteTable noteTable, UserTable userTable, long chatId, String text) {
        noteManagerService.getTextNote(noteTable, userTable, text);
        sendMessageService.sendAnswer("Заметка с названием '"+noteTable.getName()+"' успешно создана!", chatId);
        userTable.setStatusUser(StatusUser.COMMON_STATUS);
        userDAO.save(userTable);
    }

    private void getPriorityNote(NoteTable noteTable, UserTable userTable, long chatId, String text) {
     boolean result = noteManagerService.getPriorityNote(noteTable, userTable, text);
     if(result){
         sendMessageService.sendAnswer("Приоритетность заметки установлена! \n Теперь введите содержание заметки", chatId);
         userTable.setStatusUser(StatusUser.GET_TEXT_NOTE_ADD);
         userDAO.save(userTable);
     } else {
         sendMessageService.sendMessageAnswerWithInlineKeyboard("Неправильно введена приоритетность повторите попытку! \nВарианты: HIGH, MEDIUM, LIGHT", chatId, false, new ButtonForKeyboard("HIGH", "HIGH"), new ButtonForKeyboard("MEDIUM", "MEDIUM"), new ButtonForKeyboard("LIGHT", "LIGHT"));
     }
    }

    private void getNameNote(NoteTable noteTable, UserTable userTable, long chatId, String text) {
        noteManagerService.getNameNote(noteTable, userTable, text);
        sendMessageService.sendMessageAnswerWithInlineKeyboard("Имя заметки установлено! \n Задайте заметки приоритетность", chatId, false, new ButtonForKeyboard("HIGH", "HIGH"), new ButtonForKeyboard("MEDIUM", "MEDIUM"), new ButtonForKeyboard("LIGHT", "LIGHT"));


    }
}
