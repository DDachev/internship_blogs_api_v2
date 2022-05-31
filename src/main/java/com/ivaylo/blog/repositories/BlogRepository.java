package com.ivaylo.blog.repositories;

import com.ivaylo.blog.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM Blog b WHERE user_id = ?1")
    List<Blog> getUserBlogs(long userId);

    Optional<Blog> findById(long id);
}