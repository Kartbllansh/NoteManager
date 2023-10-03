package kartbllansh;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j
public class DispatcherApplication {



    public static void main(String[] args) {
        SpringApplication.run(DispatcherApplication.class);

    }

}