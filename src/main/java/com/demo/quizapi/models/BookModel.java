package com.demo.quizapi.models;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "books", itemRelation = "book")
public class BookModel extends RepresentationModel<BookModel> {
  private Integer id;

  private String title;

  private String author;

  private String downloadUrl;

  private Integer totalPages;

  private Integer rating;

  private String isbn;

  private Date publishedDate;

  private String publisherId;

  private CategoryModel category;

  private Date createdAt;

  private Date updatedAt;
}
