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
    
    /**
     * Tomcat large file upload connection reset
     * http://www.mkyong.com/spring/spring-file-upload-and-connection-reset-issue/
     * falta agregar dependencias para las clases
     * org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
     * org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
     * Cuando el archivo a cargar es muy grande,
     * hay que dar reset al Tomcat, con 1MB no 
     * tiene problema con 10 MB si, las propiedades
     * del tamanio máximo están en el application.properties
     * @return
     */
    /*@Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbedded() {

        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                //-1 means unlimited
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });

        return tomcat;
    }*/
}
