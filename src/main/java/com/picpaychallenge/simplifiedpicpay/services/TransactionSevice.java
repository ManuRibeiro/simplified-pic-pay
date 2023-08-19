package com.picpaychallenge.simplifiedpicpay.services;

import com.picpaychallenge.simplifiedpicpay.dtos.TransactionDTO;
import com.picpaychallenge.simplifiedpicpay.model.Transactions.Transactions;
import com.picpaychallenge.simplifiedpicpay.model.User.User;
import com.picpaychallenge.simplifiedpicpay.repository.transactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionSevice {

    @Autowired
    private transactionRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transactions createTransaction(TransactionDTO transaction) throws Exception{
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender,transaction.value());

        Boolean isAuthorized = checkAuthorization(sender,transaction.value());

        if(!isAuthorized){
            throw new Exception("Not Authorized transaction");
        }

        Transactions newTransaction = new Transactions();
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setAmount(transaction.value());
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender,"Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver,"Transação realizada com sucesso");

        return newTransaction;

    }

    public Boolean checkAuthorization(User sender, BigDecimal value){
        ResponseEntity<Map> authorizationResponse =
                restTemplate.getForEntity(
                        "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6",
                        Map.class);

        if(authorizationResponse.getStatusCode() == HttpStatus.OK){
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }else return false;

    }

}
