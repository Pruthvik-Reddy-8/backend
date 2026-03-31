package com.sms;

import com.sms.entity.User;
import com.sms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User("admin", "admin123", "ADMIN");
            userRepository.save(admin);
            System.out.println("✅ Default admin user created: username=admin, password=admin123");
        }
    }
}
