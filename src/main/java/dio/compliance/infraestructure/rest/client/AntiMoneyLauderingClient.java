package dio.compliance.infraestructure.rest.client;

import dio.compliance.infraestructure.rest.dto.AmlResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("aml-client")
public interface AntiMoneyLauderingClient {
    @GetMapping("/aml/v1/screening/{registrationNumber}")
    AmlResult screening(@PathVariable String registrationNumber);
}
