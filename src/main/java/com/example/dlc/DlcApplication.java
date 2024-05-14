package com.example.dlc;

import com.example.dlc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DlcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DlcApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
//		Student student1 = new Student("Charlie", "Chun", "ycc88@gmail.com");
//		studentRepository.save(student1);
//		Student student2 = new Student("Andrew", "Yong", "ycddfa@gmail.com");
//		studentRepository.save(student2);
//		Student student3 = new Student("Deb", "Chaoi", "yosi@gmail.com");
//		studentRepository.save(student3);
	}

}
