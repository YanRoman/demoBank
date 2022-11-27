package com.demoBank.services;

import com.demoBank.entities.Card;
import com.demoBank.entities.Role;
import com.demoBank.entities.User;
import com.demoBank.repositories.CardRepository;
import com.demoBank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardRepository = cardRepository;
    }


    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String email){return userRepository.findByEmail(email);}

    public List<User> allUsers(){
        return userRepository.findAll();
    }
    public boolean saveUser(User user){

        if(userRepository.findByUsername(user.getUsername()) != null){
           return false;
        }

        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public void addCard(Principal principal, Card card){
        card.setDate(new Date());

        int number = (int) (Math.random() * 500000);
        while (cardRepository.findByNumber(number) != null ){
            number = (int) (Math.random() * 500000);
        }
        card.setNumber(number);

        int cvv = (int) (Math.random() * 1000);
        while (cardRepository.findByCvv(cvv) != null){
            cvv = (int) (Math.random() * 1000);
        }
        card.setCvv(cvv);

        User user = findByUsername(principal.getName());
        List<Card> cards = user.getCards();
        cards.add(card);
        user.setCards(cards);
        cardRepository.save(card);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }


}
