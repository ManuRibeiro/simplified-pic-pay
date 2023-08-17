package com.picpaychallenge.simplifiedpicpay.repository;

import com.picpaychallenge.simplifiedpicpay.model.Transactions.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface transactionRepository extends JpaRepository<Transactions,Long> {
}
