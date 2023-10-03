package kartbllansh.service.impl;

import kartbllansh.ButtonForKeyboard;
import kartbllansh.dao.NoteDAO;
import kartbllansh.dao.UserDAO;
import kartbllansh.entity.NoteTable;
import kartbllansh.entity.UserTable;
import kartbllansh.service.*;
import kartbllansh.supplement.StatusUser;
import kartbllansh.utils.ServiceCommand;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static kartbllansh.utils.BigMessage.HELP_MESSAGE;
import static kartbllansh.utils.BigMessage.START_MESSAGE;

@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final UserDAO userDAO;
    private final NoteDAO noteDAO;
    private final SendMessageService sendMessageService;
    private final AddNoteCMDService addNoteCMDService;
    private final NoteManagerService noteManagerService;
    private final EditNoteCMDService editNoteCMDService;

    public MainServiceImpl(UserDAO userDAO, NoteDAO noteDAO, SendMessageService sendMessageService, AddNoteCMDService addNoteCMDService, NoteManagerService noteManagerService, EditNoteCMDService editNoteCMDService) {
        this.userDAO = userDAO;
        this.noteDAO = noteDAO;
        this.sendMessageService = sendMessageService;
        this.addNoteCMDService = addNoteCMDService;
        this.noteManagerService = noteManagerService;
        this.editNoteCMDService = editNoteCMDService;
    }

    @Override
    public void processTextMessage(Update update) {
        var user = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();
        var statusUser = user.getStatusUser();
        switch (statusUser){
            case GET_NAME_NOTE_ADD, GET_PRIORITY_NOTE_ADD, GET_TEXT_NOTE_ADD -> addNoteCMDService.processAddNoteCMD(user, text, chatId);
            case CHOOSE_NOTE_VIEW -> processViewNote(user, text, chatId);
            case COMMON_STATUS -> processServiceCommand(user, chatId, text);
            case EDIT_NOTE, CHOOSE_NOTE_EDIT, CHOOSE_FIELD_EDIT -> editNoteCMDService.processEditNoteCMD(user, text, chatId);
        }

    }

    private void processViewNote(UserTable user, String text, Long chatId) {
        StringBuilder stringBuilder = noteManagerService.viewNote(user, text);
        user.setStatusUser(StatusUser.COMMON_STATUS);
        userDAO.save(user);
        sendMessageService.sendAnswer(stringBuilder.toString(), chatId);
    }

    private void processServiceCommand(UserTable user, Long chatId, String text) {
        var optional = ServiceCommand.fromValue(text);
        if(optional.isEmpty()){
            sendMessageService.sendAnswer("Неизвестная команда!", chatId);
        } else {
        switch (optional.get()){
            case START -> sendMessageService.sendAnswer("Приветствую вас, "+user.getUserName()+"\n"+START_MESSAGE, chatId);
            case CANCEL -> sendMessageService.sendAnswer("Галя, отмена!", chatId);
            case SUPPORT -> sendMessageService.sendAnswer("Если возникли вопросы пишите -> @Kartbllansh", chatId);
            case HELP -> sendMessageService.sendAnswer(HELP_MESSAGE, chatId);
            case ADD_NOTE -> addNoteStart(user, chatId);
            case VIEW_NOTE -> viewNoteStart(user, chatId);
            case EDIT_NOTE -> editNoteStart(user, chatId);
        }
        }
    }

    private void editNoteStart(UserTable user, Long chatId) {
        user.setStatusUser(StatusUser.CHOOSE_NOTE_EDIT);
        userDAO.save(user);
        chooseNoteStart(user, chatId);
    }

    private void viewNoteStart(UserTable user, Long chatId) {
        user.setStatusUser(StatusUser.CHOOSE_NOTE_VIEW);
        userDAO.save(user);
        chooseNoteStart(user, chatId);
    }

    private void addNoteStart(UserTable user, long chatId) {
        user.setStatusUser(StatusUser.GET_NAME_NOTE_ADD);
        userDAO.save(user);
        sendMessageService.sendAnswer("Активирована команда: Создание заметки \n Введите название заметки", chatId);
    }

    private void chooseNoteStart(UserTable user, Long chatId) {
        var optional = noteDAO.findByCreator(user, Sort.by(Sort.Direction.ASC, "priority"));
        if (optional.isEmpty()) {
        sendMessageService.sendAnswer("У вас еще нет ни одной заметки \n Создайте сначала: /add", chatId);
        } else {

            List<ButtonForKeyboard> buttonsList = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder("Выберите заметку\n");
            int count = 1; // Начальное значение порядкового номера
            for (NoteTable noteTable : optional.get()) {

                buttonsList.add(new ButtonForKeyboard(String.valueOf(count), noteTable.getId().toString()));
                stringBuilder.append(count).append(")").append(noteTable.getName()).append("\n\n");
                count++;
            }
            sendMessageService.sendMessageAnswerWithInlineKeyboard(stringBuilder.toString(), chatId, true, buttonsList.toArray(new ButtonForKeyboard[0]));
        }
    }

    private UserTable findOrSaveAppUser(Update update) {
        var telegramUser = update.getMessage().getFrom();
        var optional = userDAO.findByTelegramUserId(telegramUser.getId());
        if(optional.isEmpty()){
            UserTable transientAppUser = UserTable.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .lastName(telegramUser.getLastName())
                    .firstName(telegramUser.getFirstName())
                    .statusUser(StatusUser.COMMON_STATUS)
                    .build();
            log.info("Новый пользователь: "+transientAppUser.getUserName());
            return userDAO.save(transientAppUser);
        }
        return optional.get();
    }
    }
