package com.demoBank;


import com.demoBank.entities.Card;
import com.demoBank.entities.Role;
import com.demoBank.entities.User;
import com.demoBank.repositories.CardRepository;
import com.demoBank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class DemoBankApplication implements CommandLineRunner {

	public static void main(String[] args) {SpringApplication.run(DemoBankApplication.class, args);}


	@Autowired
	UserRepository userRepository;
	@Autowired
	CardRepository cardRepository;

	@Override
	public void run(String... args) throws Exception {

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		User user1 = new User();
		user1.setUsername("username1");
		user1.setPassword(bCryptPasswordEncoder.encode("100"));
		user1.setEmail("mail1@mail.ru");
		user1.setRoles(Collections.singleton(Role.USER));

		User user2 = new User();
		user2.setUsername("username2");
		user2.setPassword(bCryptPasswordEncoder.encode("100"));
		user2.setEmail("mail2@mail.ru");
		user2.setRoles(Collections.singleton(Role.USER));

		Card card1 = new Card();
		card1.setNumber("111111");
		Card card2 = new Card();
		card2.setNumber("222222");

		user1.setCard(card1);
		user2.setCard(card2);

		List<User> users = Arrays.asList(user1, user2);

		userRepository.saveAll(users);

		//System.out.println("<<<<<<<<<<<<<<<<<" + userRepository.findAll());
	}
}
