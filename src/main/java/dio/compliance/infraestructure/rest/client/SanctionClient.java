package dio.compliance.infraestructure.rest.client;

import dio.compliance.infraestructure.rest.dto.SanctionResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "sanction-client", fallback = SanctionClient.Fallback.class)
/**
 * Cliente Feign da API simulada de sanções.
 *
 * <p>A URL de base é configurada para o Mockoon na porta 3001. Se o serviço não
 * responder, o fallback documentado abaixo retorna uma lista vazia.</p>
 */
public interface SanctionClient {

    /**
     * Consulta correspondências de sanções pelo número de registro.
     *
     * @param registrationNumber identificador enviado no caminho da requisição
     * @return correspondências recebidas da API ou resultado vazio no fallback
     */
    @GetMapping("/sanctions/companies/{registrationNumber}")
    SanctionResult getCompanyRisk(@PathVariable String registrationNumber);

    @Component
    /**
     * Implementação de contingência acionada quando a API de sanções falha.
     *
     * <p>Ela evita uma nulidade no fluxo, mas representa ausência de dados e não
     * deve ser interpretada como aprovação de risco.</p>
     */
    class Fallback implements SanctionClient {
        /**
         * Retorna uma lista vazia sem correspondências.
         *
         * @param registrationNumber identificador solicitado
         * @return resultado vazio seguro para o processamento posterior
         */
        public SanctionResult getCompanyRisk(String registrationNumber) {
            return new SanctionResult(List.of());
        }
    }
}
