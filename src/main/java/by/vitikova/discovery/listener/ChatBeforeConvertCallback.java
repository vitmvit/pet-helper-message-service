package by.vitikova.discovery.listener;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.constant.ChatType;
import by.vitikova.discovery.model.entity.Chat;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ChatBeforeConvertCallback implements BeforeConvertCallback<Chat> {

    @Override
    public Publisher<Chat> onBeforeConvert(Chat entity, SqlIdentifier table) {
        if (entity.getId() == null) {
            if (entity.getStatus() == null) {
                entity.setStatus(ChatStatus.FREE);
            }
            if (entity.getType() == null) {
                entity.setType(ChatType.SUPPORT);
            }
        }
        return Mono.just(entity);
    }
}