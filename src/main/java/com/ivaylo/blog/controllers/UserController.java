package com.ivaylo.blog.controllers;

import com.ivaylo.blog.entities.Blog;
import com.ivaylo.blog.entities.User;
import com.ivaylo.blog.services.AuthService;
import com.ivaylo.blog.services.BlogService;
import com.ivaylo.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.ivaylo.blog.utility.ConstantVariables.*;

@RestController
@RequestMapping(path = "api/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private BlogService blogService;


    @PostMapping(path = "{username}/add_blog")
    public ResponseEntity<Blog> addBlock(@RequestBody Blog blog,@PathVariable("username") String username,
                                         @RequestHeader(name = SESSION_ID,required = false)String sessionId){
        if(sessionId == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!authService.isProfileOwner(sessionId,username)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/blogs/"+username).toUriString());
        Optional<User> user = userService.getUserByUsername(username);
        if(user.isPresent()){
            Blog responseBlog = blogService.saveBlog(blog,user.get());
            return ResponseEntity.created(uri).body(responseBlog);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(path = "{username}/delete_blog/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable("username") String username, @PathVariable("id") long id,
                                             @RequestHeader(name = SESSION_ID, required = false) String sessionId){
        if(sessionId == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!authService.isProfileOwner(sessionId,username)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
       return ResponseEntity.ok().body("TODO DELETE");
    }
    @PutMapping(path = "{username}/update_blog/{id}")
    public ResponseEntity<String> updateBlog(@PathVariable("username") String username, @PathVariable("id") long id,
                                             @RequestHeader(name = SESSION_ID, required = false) String sessionId){
        if(sessionId == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!authService.isProfileOwner(sessionId,username)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok().body("TODO UPDATE");
    }
}
