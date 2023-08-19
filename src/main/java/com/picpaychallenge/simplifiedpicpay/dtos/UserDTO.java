package com.picpaychallenge.simplifiedpicpay.dtos;

import com.picpaychallenge.simplifiedpicpay.model.User.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName,
                      String lastName,
                      String document,
                      BigDecimal balance,
                      String email,
                      String password,
                      UserType userType) {
}
