package com.demoBank.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Это поле не должно быть пустым")
    @Size(min = 2, message = "Длина имени должна быть больше 2 символов")
    @Size(max = 30, message = "Длина имени должна быть меньше чем 30 символов")
    private String username;

    @NotBlank(message = "Это поле не должно быть пустым")
    private String password;

    @Transient
    private String passwordConfirm;

    @Email(message = "формат email не верный")
    @NotBlank(message = "email не может быть пустым")
    private String email;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;
}
