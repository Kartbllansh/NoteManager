package kartbllansh.service;

import kartbllansh.entity.NoteTable;
import kartbllansh.entity.UserTable;

public interface NoteManagerService {
    void getNameNote(NoteTable noteTable, UserTable userTable, String text);
    boolean getPriorityNote(NoteTable noteTable, UserTable userTable, String text);
    void getTextNote(NoteTable noteTable, UserTable userTable, String text);
    StringBuilder viewNote(UserTable user, String callBackData);
}
