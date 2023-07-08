package CaC.Grupo2.FlySky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlySkyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlySkyApplication.class, args);
	}

}
