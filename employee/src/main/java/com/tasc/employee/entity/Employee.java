package com.tasc.employee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tasc.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String address;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime dob;
}
