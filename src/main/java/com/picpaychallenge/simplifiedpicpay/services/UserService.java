package com.picpaychallenge.simplifiedpicpay.services;

import com.picpaychallenge.simplifiedpicpay.dtos.UserDTO;
import com.picpaychallenge.simplifiedpicpay.model.User.User;
import com.picpaychallenge.simplifiedpicpay.model.User.UserType;
import com.picpaychallenge.simplifiedpicpay.repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private userRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception{

        if(sender.getUserType() == UserType.MERCHANT){
            throw new  Exception("User of type merchant is not authorized to make transactions");
        }
        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Usr has not enough money");
        }
    }

    public User findUserById(Long Id) throws Exception{
        return this.repository.findUsersById(Id).orElseThrow(()->new Exception("User not found"));
    }

    public User createUser(UserDTO user){
        User newUser = new User(user);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }
    public void saveUser(User user){
        this.repository.save(user);
    }

}
