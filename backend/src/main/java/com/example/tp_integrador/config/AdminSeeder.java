package com.example.tp_integrador.config;

import com.example.tp_integrador.entities.User;
import com.example.tp_integrador.enums.Rol;
import com.example.tp_integrador.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    UserRepository  userRepository;
    public AdminSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findByEmail("admin@foodstore.com").isEmpty()){
            User admin = User.builder()
                    .firstName("Super")
                    .lastName("Administrador")
                    .email("admin@foodstore.com")
                    .password("123456")
                    .role(Rol.ADMIN)
                    .build();

            userRepository.save(admin);
        }
    }
}
