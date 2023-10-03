package kartbllansh.service;

import kartbllansh.entity.UserTable;

public interface EditNoteCMDService {
    void processEditNoteCMD(UserTable userTable, String s, long chatId);
}
