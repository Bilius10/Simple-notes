package api.example.SimpleNotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class SimpleNotesApplication {
    //entender por qual motivo que quando tento fazer login pelo front, ele não funciona
	public static void main(String[] args) {
		SpringApplication.run(SimpleNotesApplication.class, args);
	}

}
