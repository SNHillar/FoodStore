package com.example.tp_integrador.services;

import com.example.tp_integrador.dtos.user.UserCreate;
import com.example.tp_integrador.dtos.user.UserDto;
import com.example.tp_integrador.dtos.user.UserEdit;
import com.example.tp_integrador.entities.User;
import com.example.tp_integrador.enums.Rol;
import com.example.tp_integrador.repositories.UserRepository;
import com.example.tp_integrador.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final UserCreate USER_CREATE = new UserCreate("Jorge", "Secco", "test@test.com", "123456789", "asDweq212", "user");

    private final User USER_PREPARED =  User.builder()
            .id(1L)
            .firstName("Saul")
            .lastName("Hillar")
            .email("test@test.com")
            .phone("3513525436")
            .password("Testing1234")
            .role(Rol.ADMIN).build();

    User USER_PREPARED_2 = User.builder()
            .id(2L)
            .firstName("Clara")
            .lastName("Rodriguez")
            .email("clara@test.com")
            .password("password")
            .role(Rol.USER)
            .build();

    private final UserEdit USER_EDIT = new UserEdit("Pepe", "Garro", "testing@test.com", "11231241", "password", "user");

    @Test
    void update(){
        // Cuando busquemos en el repositorio el id 1, entonces esperamos que retorne al usuario.
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(USER_PREPARED));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocationOnMock ->  invocationOnMock.getArgument(0));
        UserDto resultDto = userService.update(USER_EDIT, 1L);

        assertEquals(1L, resultDto.id());
        assertEquals("Pepe", resultDto.firstName());
        assertEquals("Garro", resultDto.lastName());
        assertEquals("testing@test.com", resultDto.email());
        assertEquals("11231241", resultDto.phone());


        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void findById(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(USER_PREPARED));
        UserDto resultDto = userService.findById(1L);
        assertEquals(1L, resultDto.id());


        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void findAll(){
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(USER_PREPARED, USER_PREPARED_2));
        List<UserDto> resultDto = userService.findAll();

        assertEquals(2, resultDto.size());
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }


    @Test
    void delete(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(USER_PREPARED));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocationOnMock ->   invocationOnMock.getArgument(0));

        userService.delete(1L);
        assertTrue(USER_PREPARED.getDeleted());
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void save(){
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(USER_PREPARED);
        userService.save(USER_CREATE);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void findByIdWrong_must_throw_exception(){
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.findById(99L));
    }
}
