package com.ivaylo.blog.services;

import com.ivaylo.blog.entities.Blog;
import com.ivaylo.blog.entities.User;
import com.ivaylo.blog.repositories.BlogRepository;
import com.ivaylo.blog.services.interfaces.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService implements IBlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Override
    public Optional<Blog> getBlog(long id) {
        return blogRepository.findById(id);
    }
    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
   @Override
   public List<Blog> getBlogsFromUser(long userId) {
           return blogRepository.getUserBlogs(userId);
   }
    @Override
    public List<Blog> getUserBlogs(long userId) {
        return null;
    }
    @Override
    public Blog saveBlog(Blog blog, User user) {
        blog.setUser(user);
        blogRepository.save(blog);
        return blog;
    }
}