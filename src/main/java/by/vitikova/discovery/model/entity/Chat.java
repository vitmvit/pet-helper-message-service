package by.vitikova.discovery.model.entity;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.listener.ChatListener;
import by.vitikova.discovery.model.entity.parent.LogModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.List;

/**
 * Сущность Chat, представляющая собой чат между пользователем и технической поддержкой.
 */
@Getter
@Setter
@Entity
@Builder
@Audited
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ChatListener.class)
public class Chat extends LogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supportName;
    private String userName;

    @Enumerated(EnumType.STRING)
    private ChatStatus status;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messageList;
}
