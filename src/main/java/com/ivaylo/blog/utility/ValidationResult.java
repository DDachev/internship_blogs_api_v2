package com.ivaylo.blog.utility;

public enum ValidationResult {
    SUCCESS("Success"),
    NAME_MUST_BE_BETWEEN("Username must be between 3 and 100 symbols"),
    NAME_IS_MANDATORY("Username is mandatory"),
    NAME_MUST_HAVE_THESE_SYMBOLS("Username must have only alphabetical symbols ,numbers and underscore"),
    PASSWORD_MUST_BE_BETWEEN("Password must be between 4 and 72 symbols"),
    PASSWORD_MUST_HAVE_THESE_SYMBOLS("Password must have only alphabetical symbols ,numbers and underscore"),
    EMAIL_IS_NOT_VALID("Email is invalid");
    private final String message;

    ValidationResult(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
