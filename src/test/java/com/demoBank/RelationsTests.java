package com.demoBank;

import com.demoBank.entities.Card;
import com.demoBank.entities.User;
import com.demoBank.repositories.CardRepository;
import com.demoBank.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
public class RelationsTests {
    private UserRepository userRepository;
    private CardRepository cardRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Autowired
    public void setCardRepository(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }
    @Test
    public void deleteCard() throws Exception{
        Card card = cardRepository.findByNumber("111111");
        card.getUser().setCard(null);
        cardRepository.delete(card);
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(3);

        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(1);
    }
    @Test void deleteUser() throws Exception{
        User user = userRepository.findByUsername("username2");
        userRepository.delete(user);

        List<User> users = userRepository.findAll();
        List<Card> cards = cardRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(cards).hasSize(0);
    }
}
