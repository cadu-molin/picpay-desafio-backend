package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        if (!authorizationService.autorizeTransaction(sender, transaction.value())) {
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
}
