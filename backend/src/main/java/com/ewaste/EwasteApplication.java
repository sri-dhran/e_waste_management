package com.ewaste;

import com.ewaste.model.Admin;
import com.ewaste.model.CollectionCenter;
import com.ewaste.model.User;
import com.ewaste.repository.AdminRepository;
import com.ewaste.repository.CollectionCenterRepository;
import com.ewaste.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class EwasteApplication {

    public static void main(String[] args) {
        SpringApplication.run(EwasteApplication.class, args);
    }

    @Bean
    public CommandLineRunner initFolders() {
        return args -> {
            // Create uploads and qr directories if they do not exist
            String[] dirs = {"../uploads", "./uploads", "../qr", "./qr"};
            for (String dir : dirs) {
                File folder = new File(dir);
                if (!folder.exists()) {
                    if (folder.mkdirs()) {
                        System.out.println("Created directory: " + folder.getAbsolutePath());
                    }
                }
            }
        };
    }

    @Bean
    public CommandLineRunner initDatabase(
            AdminRepository adminRepository,
            CollectionCenterRepository centerRepository,
            UserRepository userRepository) {
        return args -> {
            // 1. Seed Collection Centers if empty
            if (centerRepository.count() == 0) {
                centerRepository.save(new CollectionCenter(null, "Green Earth Recycling", "Chennai", "+91 98765 43210", 13.0827, 80.2707));
                centerRepository.save(new CollectionCenter(null, "Eco Waste Center", "Tambaram", "+91 87654 32109", 12.9249, 80.1240));
                centerRepository.save(new CollectionCenter(null, "E-Planet Recycling", "Velachery", "+91 76543 21098", 12.9815, 80.2180));
                System.out.println("Seeded database with default collection centers.");
            }

            // 2. Seed Admin if empty
            if (adminRepository.count() == 0) {
                adminRepository.save(new Admin(null, "admin", "admin123"));
                System.out.println("Seeded database with default admin user.");
            }

            // 3. Seed Center Staff if empty
            if (userRepository.count() == 0) {
                CollectionCenter center = centerRepository.findAll().stream().findFirst().orElse(null);
                if (center != null) {
                    userRepository.save(new User(null, "Green Earth Staff", "staff@greenearth.com", "+91 99999 88888", "staff123", 0, center));
                    System.out.println("Seeded database with default center staff.");
                }
            }
        };
    }
}
