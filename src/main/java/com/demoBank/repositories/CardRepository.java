package com.demoBank.repositories;

import com.demoBank.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String number);
    Card findByCvv(int cvv);
}
