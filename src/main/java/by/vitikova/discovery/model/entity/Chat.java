package by.vitikova.discovery.model.entity;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.listener.ChatListener;
import by.vitikova.discovery.model.entity.parent.LogModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сущность Chat, представляющая собой чат между пользователем и технической поддержкой.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(ChatListener.class)
public class Chat extends LogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supportName;
    private String userName;

    @Enumerated(EnumType.STRING)
    private ChatStatus status;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messageList;
}
