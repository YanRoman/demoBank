package com.demoBank.services;

import com.demoBank.entities.Role;
import com.demoBank.entities.User;
import com.demoBank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }


}
