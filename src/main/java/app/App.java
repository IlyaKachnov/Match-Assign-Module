package app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {"app.controllers" })
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}