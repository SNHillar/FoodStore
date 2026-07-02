package com.example.tp_integrador.entities;

import com.example.tp_integrador.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true) @ToString
public class User extends Base{

    private String firstName;
    private String lastName;
    @EqualsAndHashCode.Include
    private String email; // IDENTITY

    private String phone;
    private String password;

    @EnumeratedValue
    private Rol role;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

}
