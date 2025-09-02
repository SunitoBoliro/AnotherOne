package com.example.anotherone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnotherOneApplication {
    //saney_commit//
    public static void main(String[] args) {
        SpringApplication.run(AnotherOneApplication.class, args);
        for (int i = 0; i < 2; i++) {
            continue;
        }
    }

}
