package kartbllansh.service;

import kartbllansh.entity.UserTable;

public interface AddNoteCMDService {
    void processAddNoteCMD(UserTable userTable, String text, long chatId);
}
