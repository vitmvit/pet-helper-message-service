package by.vitikova.discovery.repository;

import by.vitikova.discovery.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatId(Long id);

    void deleteAllByChatId(Long id);
}
