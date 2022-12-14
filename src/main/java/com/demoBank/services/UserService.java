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
import java.util.Optional;


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
    public User findByTelephone(String telephone){return userRepository.findByTelephone(telephone);}
    public List<User> allUsers(){
        return userRepository.findAll();
    }
    public boolean saveUser(User user){
        if (userRepository.findByUsername(user.getUsername()) != null){
            return false;
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setIndebtedness(0);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public boolean setUsername(String username, Long id){
        if (findByUsername(username) != null){
            return false;
        }
        User user = userRepository.findById(id).get();
        user.setUsername(username);
        userRepository.save(user);
        return true;
    }
    public boolean setEmail(String email, Long id){
        if (findByEmail(email) != null){
            return false;
        }
        User user = userRepository.findById(id).get();
        user.setEmail(email);
        userRepository.save(user);
        return true;
    }
    public boolean setTelephone(String telephone, Long id){
        if (findByTelephone(telephone) != null){
            return false;
        }
        User user = userRepository.findById(id).get();
        user.setTelephone(telephone);
        userRepository.save(user);
        return true;
    }
    public void setPassword(String password, Long id){
        User user = userRepository.findById(id).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    public void setIndebtedness(double amount, Long id){
        User user = userRepository.findById(id).get();
        user.setIndebtedness(amount);
        userRepository.save(user);
    }
    public boolean addCard(Principal principal){
        User user = findByUsername(principal.getName());
        if(user.getCard() != null){
            return false;
        }
        Card card = new Card();

        card.setDate(new Date());

        StringBuilder number = new StringBuilder(new String());
        for (int i = 0; i < 4; i++){
            number.append(Integer.toString(((int) (Math.random() * 8999)) + 1000));
            if (i<3){
                number.append(" ");
            }
        }
        while (cardRepository.findByNumber(number.toString()) != null){
            for (int i = 0; i < 4; i++){
                number.append(Integer.toString(((int) (Math.random() * 8999)) + 1000));
                if (i<3){
                    number.append(" ");
                }
            }
        }
        card.setNumber(number.toString());


        int cvv = ( (int) (Math.random() * 900) ) + 100;
        while (cardRepository.findByCvv(cvv) != null){
            cvv = ( (int) (Math.random() * 900) ) + 100;
        }
        card.setCvv(cvv);
        card.setBalance(60000);
        user.setCard(card);
        cardRepository.save(card);
        return true;
    }
    public void deleteCard(Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        Card card = user.getCard();
        user.setCard(null);

        cardRepository.delete(card);
    }

    public boolean send(double sum, String number, Principal principal){
        Card recipientCard = cardRepository.findByNumber(number);

        if (recipientCard == null){
            return false;
        }

        double newBalance = recipientCard.getBalance() + sum;
        recipientCard.setBalance(newBalance);
        return true;
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
