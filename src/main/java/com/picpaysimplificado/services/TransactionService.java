package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        if (!autorizeTransaction(sender, transaction.value())) {
            throw new Exception("Transação não autorizada");
        }

        Transaction newtrasaction = new Transaction();
        newtrasaction.setAmount(transaction.value());
        newtrasaction.setSender(sender);
        newtrasaction.setReceiver(receiver);
        newtrasaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        transactionRepository.save(newtrasaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);

        notificationService.sendNotification(sender, "Transação realizada com sucesso");
        notificationService.sendNotification(receiver,"Transação recebida com sucesso");

        return newtrasaction;
    }

    public boolean autorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse =  restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if (HttpStatus.OK.equals(authorizationResponse.getStatusCode()) && "success".equals(authorizationResponse.getBody().get("status"))) {
            return true;
        } else {
            return false;
        }
    }
}
