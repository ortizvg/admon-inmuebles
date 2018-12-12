package mx.com.admoninmuebles;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import mx.com.admoninmuebles.storage.StorageProperties;
import mx.com.admoninmuebles.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class AdministracionInmueblesApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AdministracionInmueblesApplication.class, args);
    }

    @Bean
    CommandLineRunner init(final StorageService storageService) {
        return args -> storageService.init();
    }
}
