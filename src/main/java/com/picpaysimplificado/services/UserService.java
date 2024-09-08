package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.mapstruct.user.UserMapper;
import com.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (UserType.MERCHANT.equals(sender.getUserType())) {
            throw new Exception("Usuário do tipo lojista não está autorizado a realizar transação");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserDTO user) {
        User newUser = userMapper.toUserEntity(user);

        return saveUser(newUser);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
