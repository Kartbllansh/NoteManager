package kartbllansh.service.impl;

import kartbllansh.dao.NoteDAO;
import kartbllansh.dao.UserDAO;
import kartbllansh.entity.NoteTable;
import kartbllansh.entity.UserTable;
import kartbllansh.service.NoteManagerService;
import kartbllansh.supplement.PriorityNotes;
import kartbllansh.supplement.StatusUser;
import kartbllansh.utils.DateTimeNow;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoteManagerServiceImpl implements NoteManagerService {
    private final UserDAO userDAO;
    private final NoteDAO noteDAO;
    public NoteManagerServiceImpl(UserDAO userDAO, NoteDAO noteDAO) {
        this.userDAO = userDAO;
        this.noteDAO = noteDAO;
    }

    @Override
    public void getNameNote(NoteTable noteTable, UserTable userTable, String text) {
        noteTable.setName(text);
        noteTable.setCreator(userTable);
        userTable.setStatusUser(StatusUser.GET_PRIORITY_NOTE_ADD);
        userTable.setActiveAddNote(noteTable);
        noteDAO.save(noteTable);
        userDAO.save(userTable);

    }

    @Override
    public boolean getPriorityNote(NoteTable noteTable, UserTable userTable, String text) {
        var optional = PriorityNotes.fromValue(text.toUpperCase());
        if(optional.isEmpty()){
            return false;
        } else {
            noteTable.setPriority(optional.get());
            noteDAO.save(noteTable);
            return true;
        }
    }

    @Override
    public void getTextNote(NoteTable noteTable, UserTable userTable, String text) {
        noteTable.setText(text);
        noteTable.setTimeOfCreation(LocalDateTime.now());
        noteTable.setTimeOfEdit(LocalDateTime.now());
        noteDAO.save(noteTable);
    }

    @Override
    public StringBuilder viewNote(UserTable user, String callBackData) {
        NoteTable noteTable = noteDAO.findById(Long.parseLong(callBackData));
        return makeFillNote(noteTable);
    }


    private StringBuilder makeFillNote(NoteTable noteTable) {
        StringBuilder stringBuilder = new StringBuilder("Заметка: \n");
        stringBuilder.append("Имя заметки: ").append(noteTable.getName()).append("\n");
        stringBuilder.append("Время создания: ").append(DateTimeNow.dateTimeNow(noteTable.getTimeOfCreation())).append("\n");
        stringBuilder.append("Приоритетность: ").append(noteTable.getPriority()).append("\n");
        stringBuilder.append("Владелец: ").append(noteTable.getCreator().getUserName()).append("\n");
        stringBuilder.append("Текст заметки: \n").append(noteTable.getText());
        return stringBuilder;
    }
}
