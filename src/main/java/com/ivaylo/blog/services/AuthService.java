package com.ivaylo.blog.services;

import com.ivaylo.blog.entities.User;
import com.ivaylo.blog.repositories.UserRepository;
import com.ivaylo.blog.services.interfaces.IAuthService;
import com.ivaylo.blog.utility.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.ivaylo.blog.utility.UserValidator.*;
import static com.ivaylo.blog.utility.ValidationResult.SUCCESS;
import static java.lang.String.format;

@Service
public class AuthService implements IAuthService {
    private static final String USERNAME_ALREADY_TAKEN = "\"%s\" is already taken!";
    private static final String EMAIL_ALREADY_TAKEN = "This email \"%s\" is already taken!";
    private static final String USER_DO_NOT_EXIST = "\"$s\" do not exist";
    @Autowired
    private UserRepository userRepository;
    @Override
    public User registerNewUser(User user) {
        ValidationResult result = isValidNameLength()
                .and(isValidName())
                .and(isValidEmail())
                .and(isValidPasswordLength())
                .and(isValidPassword())
                .apply(user);
        if(result!= SUCCESS){
            throw new IllegalStateException(result.getMessage());
        }
        Optional<User> userFromDbByName = userRepository.findByUsername(user.getUsername());
        if(userFromDbByName.isPresent()){
            throw new IllegalStateException(format(USERNAME_ALREADY_TAKEN,user.getUsername()));
        }
        Optional<User> userFromDbByEmail = userRepository.findByEmail(user.getEmail());
        if(userFromDbByEmail.isPresent()){
            throw new IllegalStateException(format(EMAIL_ALREADY_TAKEN,user.getEmail()));
        }
        String bcryptPassword = BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(12));
        user.setPassword(bcryptPassword);
        userRepository.save(user);
        return user;
    }
    
    // Sample refactoring - you can think which of the logic in all service classes can be move to a new methods
    private void isExistingCustomer(User user) {
        Optional<User> userFromDbByName = userRepository.findByUsername(user.getUsername());
        if(userFromDbByName.isPresent()){
            throw new IllegalStateException(format(USERNAME_ALREADY_TAKEN,user.getUsername()));
        }
        Optional<User> userFromDbByEmail = userRepository.findByEmail(user.getEmail());
        if(userFromDbByEmail.isPresent()){
            throw new IllegalStateException(format(EMAIL_ALREADY_TAKEN,user.getEmail()));
        }
    }
    
    @Override
    public Optional<User> login(User user) {
        ValidationResult result = isValidNameLength()
                .and(isValidName())
                .and(isValidPasswordLength())
                .and(isValidPassword())
                .apply(user);
        if(result!= SUCCESS){
            throw new IllegalStateException(result.getMessage());
        }
        Optional<User> userDB = userRepository.findByUsername(user.getUsername());
        if(userDB.isPresent() && BCrypt.checkpw(user.getPassword(), userDB.get().getPassword())){
            userDB.get().setLogin(true);
            String sessionId = UUID.randomUUID().toString();
            userDB.get().setSessionId(sessionId);
            userRepository.save(userDB.get());
            return userDB;
        }
        return Optional.empty();
    }
    @Override
    public User logout(String username) {
        Optional<User> userDB = userRepository.findByUsername(username);
        if(userDB.isPresent()) {
            userDB.get().setSessionId(null);
            userDB.get().setLogin(false);
            userRepository.save(userDB.get());
            return userDB.get();
        }else{
            throw new IllegalStateException(String.format(USER_DO_NOT_EXIST,username));
        }
    }
    @Override
    public boolean isLoginUser(String sessionId) {
        Optional<User> userDB = userRepository.findBySessionId(sessionId);
        return userDB.isPresent() && userDB.get().isLogin();
    }
    @Override
    public boolean isProfileOwner(String sessionId, String username) {
        Optional<User> userDB = userRepository.findBySessionId(sessionId);
        return userDB.isPresent() && userDB.get().getUsername().equals(username) && userDB.get().isLogin();
    }
}
