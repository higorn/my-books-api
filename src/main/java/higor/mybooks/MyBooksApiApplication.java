package higor.mybooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MyBooksApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBooksApiApplication.class, args);
	}
  }
