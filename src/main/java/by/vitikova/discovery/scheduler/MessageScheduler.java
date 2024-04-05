package by.vitikova.discovery.scheduler;

import by.vitikova.discovery.constant.ChatStatus;
import by.vitikova.discovery.model.entity.Chat;
import by.vitikova.discovery.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Планировщик задач для удаления старых чатов.
 */
@Component
public class MessageScheduler {

    private static final Logger log = LoggerFactory.getLogger(MessageScheduler.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final ChatRepository chatRepository;

    @Value("${chat-scheduler.period}")
    private int period;

    @Autowired
    public MessageScheduler(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    /**
     * Выполняет очистку старых чатов по расписанию.
     */
    @Scheduled(cron = "${chat-scheduler.cron-expression}")
    public void cleanupOldChats() {
        log.info("MessageScheduler {}", dateFormat.format(new Date()));
        LocalDateTime threshold = LocalDateTime.now().minusDays(period);

        List<Chat> chatsToRemove = chatRepository.findChatsByStatusAndUpdateDateBefore(ChatStatus.CLOSED, threshold);
        chatRepository.deleteAll(chatsToRemove);
    }
}