package com.example.arogya360.repository;

import com.example.arogya360.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPaymentStatus(String paymentStatus);
    long countByPaymentStatus(String paymentStatus);
}
