package com.demo.quizapi.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "categories", itemRelation = "category")
@JsonPropertyOrder(alphabetic = true)
public class CategoryModel extends RepresentationModel<CategoryModel> {
  private Integer id;

  private String name;

  private Boolean active; // use to enable and disable categories;

  private Date createdAt;

  private Date updatedAt;

  private Set<BookModel> books;
}
