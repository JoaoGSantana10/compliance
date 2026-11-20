package dio.compliance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
/**
 * Teste básico de integração do contexto Spring.
 *
 * <p>Garante que a configuração da aplicação, as dependências, o repositório
 * Spring Data e os componentes do contexto possam ser inicializados.</p>
 */
class ComplianceApplicationTests {

    @Test
    /**
     * Verifica se o contexto completo consegue carregar sem falhas.
     */
    void contextLoads() {
    }

}
