package dio.compliance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@SpringBootApplication
@EnableMapRepositories
@EnableFeignClients
/**
 * Classe inicializadora da aplicação Spring Boot Compliance Shield.
 *
 * <p>Ativa a infraestrutura web, os repositórios baseados em Map, os clientes
 * Feign e a descoberta dos componentes da aplicação.</p>
 */
public class ComplianceApplication {

    /**
     * Inicia o contexto Spring e todos os componentes configurados.
     *
     * @param args argumentos de linha de comando repassados ao Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(ComplianceApplication.class, args);
    }

}
