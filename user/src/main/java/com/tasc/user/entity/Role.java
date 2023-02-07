package com.tasc.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.entity.BaseEntity;
import javax.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonPropertyOrder({"id", "name", "description", "createdAt", "updatedAt", "status"})
@Table(name = "roles")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Lob
    private String description;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> user;

    @ManyToMany
    @JoinTable(name = "roles_methods",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "method_id"))
    @JsonBackReference
    private List<Method> methods;

    @ManyToMany
    @JoinTable(name = "roles_uris",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "url_id"))
    @JsonBackReference
    private List<Uri> uris;
}
