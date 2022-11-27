package com.demoBank.repositories;

import com.demoBank.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    public Card findByNumber(String number);
    public Card findByCvv(int cvv);
}
