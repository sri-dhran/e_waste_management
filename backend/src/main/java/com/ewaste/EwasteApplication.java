package com.ewaste;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

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
}
