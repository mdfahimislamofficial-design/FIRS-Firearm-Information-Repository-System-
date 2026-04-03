package com.firs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@Controller
public class FirsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirsApplication.class, args);
	}

	@RequestMapping("/LogIn")
	public String home() {
		return "LogIn.Html";
	}
}
