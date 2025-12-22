package by.vitikova.discovery.scheduler;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.model.entity.Chat;
import by.vitikova.discovery.repository.ChatRepository;
import by.vitikova.discovery.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageScheduler {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Value("${chat-scheduler.period}")
    private int period;

    @Scheduled(cron = "${chat-scheduler.cron-expression}")
    public void cleanupOldChats() {
        log.info("MessageScheduler started at {}", LocalDateTime.now());
        LocalDateTime threshold = LocalDateTime.now().minusDays(period);

        chatRepository.findByStatusAndUpdateDateBefore(ChatStatus.CLOSED, threshold)
                .map(Chat::getId)
                .collectList()
                .flatMap(ids -> {
                    if (ids.isEmpty()) {
                        log.info("No old chats found for cleanup");
                        return Mono.empty();
                    }
                    log.info("Deleting {} old chats and their messages", ids.size());
                    return messageRepository.deleteAllByChatIdIn(ids)
                            .then(chatRepository.deleteAllById(ids));
                })
                .subscribe(
                        null,
                        error -> log.error("Error during chat cleanup: ", error),
                        () -> log.info("MessageScheduler cleanup finished")
                );
    }
}