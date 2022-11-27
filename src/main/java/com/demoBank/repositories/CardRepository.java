package com.demoBank.repositories;

import com.demoBank.entities.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Long> {
    public Card findByNumber(int number);
    public Card findByCvv(int cvv);
}
