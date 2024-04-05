package by.vitikova.discovery.feign;

import by.vitikova.discovery.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign-клиент для взаимодействия с микросервисом управления пользователями.
 */
@FeignClient(contextId = "userClient", value = "${feign.user-service.value}", url = "${feign.user-service.url}")
public interface UserClient {

    /**
     * Находит пользователя по его логину.
     *
     * @param login логин пользователя
     * @return объект ResponseEntity со статусом ответа и найденным пользователем в теле ответа
     */
    @GetMapping("/{login}")
    ResponseEntity<UserDto> findByLogin(@PathVariable("login") String login);
}
