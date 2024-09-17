package com.jt.borrownetapi;

import org.springframework.boot.SpringApplication;

public class TestBorrowNetApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(BorrowNetApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
