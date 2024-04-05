package by.vitikova.discovery.repository;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findChatsBySupportName(String name);

    List<Chat> findChatsByUserName(String name);

    List<Chat> findChatsByUserNameContainsAndSupportName(String name, String supportName);

    List<Chat> findChatsByStatus(ChatStatus status);

    List<Chat> findChatsByStatusAndUpdateDateBefore(ChatStatus status, LocalDateTime dateTime);

    List<Chat> findChatsBySupportNameAndUserName(String supportName, String userName);
}