package com.ivaylo.blog.services.interfaces;

import com.ivaylo.blog.entities.User;

import java.util.Optional;

public interface IAuthService {
    User registerNewUser(User user);

    Optional<User> login(User user);

    User logout(String username);

    boolean isLoginUser(String sessionId);

    boolean isProfileOwner(String sessionId, String username);
}
