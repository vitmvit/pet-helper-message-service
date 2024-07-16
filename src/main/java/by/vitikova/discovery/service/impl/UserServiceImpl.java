package by.vitikova.discovery.service.impl;

import by.vitikova.discovery.UserDto;
import by.vitikova.discovery.exception.EntityNotFoundException;
import by.vitikova.discovery.feign.UserClient;
import by.vitikova.discovery.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserClient userClient;

    /**
     * Находит пользователя по логину.
     *
     * @param login логин пользователя
     * @return объект UserDto, представляющий найденного пользователя
     * @throws EntityNotFoundException если пользователь не найден
     */
    @Override
    public UserDto findByLogin(String login) {
        try {
            logger.info("UserService: find user by login: " + login);
            return userClient.findByLogin(login).getBody();
        } catch (Exception e) {
            logger.error("UserService: Entity not found error");
            throw new EntityNotFoundException();
        }
    }
}