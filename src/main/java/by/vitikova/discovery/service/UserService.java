package by.vitikova.discovery.service;

import by.vitikova.discovery.UserDto;

public interface UserService {

    UserDto findByLogin(String login);
}
