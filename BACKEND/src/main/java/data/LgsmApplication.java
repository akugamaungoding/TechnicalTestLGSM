package data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LgsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(LgsmApplication.class, args);
    }

}
