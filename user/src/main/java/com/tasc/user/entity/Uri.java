package com.tasc.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.entity.BaseEntity;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonPropertyOrder({"id", "url", "description", "createdAt", "updatedAt", "status"})
@Table(name = "uris")
public class Uri extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private String uri;

    @Lob
    private String description;

    @ManyToMany(mappedBy = "uris")
    @JsonBackReference
    private List<Role> roles;
}
