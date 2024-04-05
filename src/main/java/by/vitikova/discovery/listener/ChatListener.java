package by.vitikova.discovery.listener;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.model.entity.Chat;
import jakarta.persistence.PrePersist;

public class ChatListener {

    /**
     * Вызывается перед сохранением новой сущности Chat в базе данных и устанавливает status для нее.
     *
     * @param chat Сущность Chat, которая будет сохранена.
     */
    @PrePersist
    public void persist(Chat chat) {
        chat.setStatus(ChatStatus.FREE);
    }
}
