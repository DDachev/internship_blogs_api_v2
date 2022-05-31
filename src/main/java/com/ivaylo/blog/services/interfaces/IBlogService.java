package com.ivaylo.blog.services.interfaces;

import com.ivaylo.blog.entities.Blog;
import com.ivaylo.blog.entities.User;

import java.util.List;
import java.util.Optional;

public interface IBlogService {

    Optional<Blog> getBlog(long id);
    List<Blog> getAllBlogs();
    List<Blog> getBlogsFromUser(long userId);

    List<Blog> getUserBlogs(long userId);

    Blog saveBlog(Blog blog, User user);
}
