package com.micnusz.edns;

import org.springframework.boot.SpringApplication;

public class TestEdnsApplication {

	public static void main(String[] args) {
		SpringApplication.from(EdnsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
