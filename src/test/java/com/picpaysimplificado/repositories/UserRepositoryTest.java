package com.picpaysimplificado.repositories;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.mapstruct.user.UserMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from DB")
    void findUserByDocumentSuccessfully() {
        String document = "12345678901";
        UserDTO data = new UserDTO("Carlos", "Teste", document, new BigDecimal(10), "teste@gmail.com", "1234", UserType.COMMON);

        createUser(data);

        Optional<User> founbdedUser = usuarioRepository.findUserByDocument(document);

        Assertions.assertTrue(founbdedUser.isPresent());
    }

    @Test
    @DisplayName("Should not get User from DB when user not exists")
    void findUserByDocumentUnsuccessful() {
        String document = "12345678901";

        Optional<User> founbdedUser = usuarioRepository.findUserByDocument(document);

        Assertions.assertFalse(founbdedUser.isPresent());
    }

    private User createUser(UserDTO data) {
        User newUser = new User(data);

        entityManager.persist(newUser);

        return newUser;
    }
}