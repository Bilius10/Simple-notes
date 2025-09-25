package api.example.SimpleNotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleNotesApplication {
    //.\mvnw.cmd clean package -DskipTests
    //docker-compose up --build -d
	public static void main(String[] args) {
		SpringApplication.run(SimpleNotesApplication.class, args);
	}

}
