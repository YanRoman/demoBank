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
	UserRepository userRepository;
	CardRepository cardRepository;

	@Autowired
	public void setUserRepository(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	@Autowired
	public void setCardRepository(CardRepository cardRepository){
		this.cardRepository = cardRepository;
	}

	@Override
	public void run(String... args) throws Exception {

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		User admin = new User();
		admin.setUsername("admin");
		admin.setPassword(bCryptPasswordEncoder.encode("100"));
		admin.setEmail("admin@mail.ru");
		admin.setRoles(Collections.singleton(Role.ADMIN));


		User testUser1 = new User();
		testUser1.setUsername("username1");
		testUser1.setPassword(bCryptPasswordEncoder.encode("100"));
		testUser1.setEmail("mail1@mail.ru");
		testUser1.setRoles(Collections.singleton(Role.USER));

		User testUser2 = new User();
		testUser2.setUsername("username2");
		testUser2.setPassword(bCryptPasswordEncoder.encode("100"));
		testUser2.setEmail("mail2@mail.ru");
		testUser2.setRoles(Collections.singleton(Role.USER));

		Card card1 = new Card();
		card1.setNumber("111111");
		Card card2 = new Card();
		card2.setNumber("222222");

		testUser1.setCard(card1);
		testUser2.setCard(card2);


		List<User> users = Arrays.asList(admin ,testUser1, testUser2);

		userRepository.saveAll(users);
	}
}
