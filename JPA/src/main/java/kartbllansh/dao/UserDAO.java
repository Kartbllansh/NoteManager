package kartbllansh.dao;

import kartbllansh.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<UserTable, Long> {
    Optional<UserTable> findByTelegramUserId(Long id);
}
