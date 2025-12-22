package by.vitikova.discovery.model.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table("message")
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private Long id;

    private Long chatId;
    private String senderName;
    private String content;
    private String uuidPhoto;

    @CreatedDate
    private LocalDateTime createDate;
}