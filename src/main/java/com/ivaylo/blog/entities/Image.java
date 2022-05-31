package com.ivaylo.blog.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "images" ,uniqueConstraints = {
        @UniqueConstraint(name = "image_article_id_unique", columnNames = "article_id")
})
@JsonIgnoreProperties("user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 20)
    private Long id;
    @Column(nullable = false, length = 300)
    @NotBlank(message = "url is mandatory")
    private String url;
    @OneToOne(optional = false)
    @JoinColumn(name = "article_id", referencedColumnName = "id",foreignKey = @ForeignKey(name="FK_image_article"))
    private Article article;

    public Image(String url){
        this.url = url;
    }
}
