package com.ivaylo.blog.utility;

import com.ivaylo.blog.entities.User;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ivaylo.blog.utility.ValidationResult.*;
import static com.ivaylo.blog.utility.ValidationResult.SUCCESS;

public interface UserValidator extends Function<User, ValidationResult> {

    static UserValidator isValidNameLength(){
        return user -> user.getUsername().length() >=3 && user.getUsername().length() <= 100?
                SUCCESS:
                NAME_MUST_BE_BETWEEN;
    }
    static UserValidator isValidName(){
        return user -> {
            String regex = "(^[a-zA-Z\\d]+(\\w+)?)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(user.getUsername());
            return matcher.matches()?SUCCESS:NAME_MUST_HAVE_THESE_SYMBOLS;
        };
    }
    static UserValidator isValidEmail(){
        return user -> {
                String regex = "(^(\\w+)@(\\w+)(.[a-z]+)$)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(user.getEmail());
                return matcher.matches()?SUCCESS:EMAIL_IS_NOT_VALID;
        };
    }
    static UserValidator isValidPasswordLength(){
        return user -> user.getPassword().length() >=4 && user.getPassword().length() <= 72? SUCCESS:PASSWORD_MUST_BE_BETWEEN;
    }
    static UserValidator isValidPassword(){
        return user -> {
            String regex = "^(\\w+)?";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(user.getPassword());
            return matcher.matches()?SUCCESS:PASSWORD_MUST_HAVE_THESE_SYMBOLS;
        };
    }

    default UserValidator and(UserValidator other){
        return user -> {
          ValidationResult result = this.apply(user);
          return result.equals(SUCCESS)? other.apply(user): result;
        };
    }
}
