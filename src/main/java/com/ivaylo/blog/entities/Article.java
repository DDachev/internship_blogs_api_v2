package com.ivaylo.blog.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "article")
@JsonIgnoreProperties("blog")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @ManyToOne
    @JoinColumn(name = "blog_id", referencedColumnName = "id",foreignKey = @ForeignKey(name="FK_article_blog"))
    private Blog blog;
    @OneToOne(mappedBy = "article")
    private Image image;

    public Article(String title,String content){
        this.title = title;
        this.content = content;
    }
}
