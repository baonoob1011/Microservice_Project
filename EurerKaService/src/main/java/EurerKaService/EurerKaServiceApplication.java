package EurerKaService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurerKaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurerKaServiceApplication.class, args);
	}

}
