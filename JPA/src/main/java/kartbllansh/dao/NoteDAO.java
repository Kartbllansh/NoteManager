package kartbllansh.dao;

import kartbllansh.entity.NoteTable;
import kartbllansh.entity.UserTable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteDAO extends JpaRepository<NoteTable, Long> {

    Optional<List<NoteTable>> findByCreator(UserTable userTable, Sort sort);
    NoteTable findById(long id);
}
