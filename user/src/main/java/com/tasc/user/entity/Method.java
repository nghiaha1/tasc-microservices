package com.tasc.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.entity.BaseEntity;
import javax.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonPropertyOrder({"id", "name", "description", "createdAt", "updatedAt", "status"})
@Table(name = "methods")
public class Method extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String method;

    @Lob
    private String description;

    @ManyToMany(mappedBy = "methods")
    @JsonBackReference
    private List<Role> roles;
}
