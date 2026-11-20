package dio.compliance.infraestructure.rest.client;

import dio.compliance.infraestructure.rest.dto.AmlResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("aml-client")
/**
 * Cliente Feign da API simulada de AML.
 *
 * <p>A URL de base é configurada pela propriedade
 * {@code spring.cloud.openfeign.client.config.aml-client.url}, normalmente
 * apontada para o Mockoon em {@code http://localhost:3002}.</p>
 */
public interface AntiMoneyLauderingClient {
    /**
     * Consulta o perfil AML de uma empresa pelo número de registro.
     *
     * @param registrationNumber identificador enviado no caminho da requisição
     * @return resultado JSON convertido para {@link AmlResult}
     */
    @GetMapping("/aml/v1/screening/{registrationNumber}")
    AmlResult screening(@PathVariable String registrationNumber);
}
