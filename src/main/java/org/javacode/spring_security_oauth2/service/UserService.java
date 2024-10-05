package org.javacode.spring_security_oauth2.service;

import org.javacode.spring_security_oauth2.model.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, User user);

    void deleteUser(Long id);

}
