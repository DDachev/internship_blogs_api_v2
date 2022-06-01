package com.ivaylo.blog.controllers;

import com.ivaylo.blog.entities.User;
import com.ivaylo.blog.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.ivaylo.blog.utility.ConstantVariables.SESSION_ID;

@RestController
@RequestMapping("api/auth/")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(path = "register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/auth/register").toUriString());
            authService.registerNewUser(user);
            return ResponseEntity.created(uri).body(user.getUsername() + " register successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            Optional<User> optUser = authService.login(user);
            if (optUser.isPresent()) {
                return ResponseEntity.ok()
                        .header(SESSION_ID, optUser.get().getSessionId())
                        .body(optUser.get().getUsername() + " logged in, Header key: session-id, Header value: " + optUser.get().getSessionId());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(path = "logout/{username}")
    public ResponseEntity<String> logout(@PathVariable("username") String username,
                                         @RequestHeader(name = SESSION_ID, required = false) String sessionId) {
        if (sessionId == null) {
            return ResponseEntity.notFound().build();
        }
        if (!authService.isProfileOwner(sessionId, username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            User user = authService.logout(username);
            return ResponseEntity.ok().body(user.getUsername() + " logout");
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    // Sample exception handling method
    @ResponseStatus(value=HttpStatus.CONFLICT, reason="Data integrity violation")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
        // Nothing to do
    }
}
