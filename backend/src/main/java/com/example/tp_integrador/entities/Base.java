package com.example.tp_integrador.entities;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jdk.jfr.BooleanFlag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@MappedSuperclass
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @BooleanFlag @Builder.Default
    Boolean deleted = false;

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
}
