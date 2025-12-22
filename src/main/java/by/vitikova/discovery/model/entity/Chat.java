package by.vitikova.discovery.model.entity;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.model.entity.parent.LogModel;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@Table("chat")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat extends LogModel {

    @Id
    private Long id;

    private String supportName;
    private String userName;
    private ChatStatus status;
    private ChatType type;

    @Transient
    private List<Message> messageList;
}