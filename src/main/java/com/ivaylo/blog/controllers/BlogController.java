package com.ivaylo.blog.controllers;

import com.ivaylo.blog.entities.Blog;
import com.ivaylo.blog.entities.User;
import com.ivaylo.blog.services.AuthService;
import com.ivaylo.blog.services.BlogService;
import com.ivaylo.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.ivaylo.blog.utility.ConstantVariables.SESSION_ID;

@RestController
@RequestMapping("api/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @GetMapping(path = "/blog/{id}")
    public ResponseEntity<?> getBlog(@PathVariable("id") Long id,
                                     @RequestHeader(name = SESSION_ID, required = false) String sessionId){
        if(sessionId == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!authService.isLoginUser(sessionId)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<Blog> blog = blogService.getBlog(id);
        if(blog.isPresent()){
            return ResponseEntity.ok().body(blog.get());
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<Blog>> getAllBlogs(@RequestHeader(name = SESSION_ID, required = false) String sessionId)
    {
        if(sessionId == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!authService.isLoginUser(sessionId)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok().body(blogService.getAllBlogs());
    }

    @GetMapping(path = "/{username}")
    public ResponseEntity<?> getUserBlogs(@PathVariable("username") String username,
                                          @RequestHeader(name = SESSION_ID, required = false) String sessionId){
        if(sessionId == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!authService.isLoginUser(sessionId)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<User> user = userService.getUserByUsername(username);
        if(user.isPresent()){
            List<Blog> blogs = blogService.getBlogsFromUser(user.get().getId());
            return  ResponseEntity.ok().body(blogs);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
