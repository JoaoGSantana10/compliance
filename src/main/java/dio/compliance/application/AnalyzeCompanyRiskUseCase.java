package dio.compliance.application;

import dio.compliance.domain.Company;
import dio.compliance.infraestructure.rest.client.SanctionClient;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeCompanyRiskUseCase {
    private final SanctionClient sanctionClient;

    public AnalyzeCompanyRiskUseCase(SanctionClient sanctionClient) {
        this.sanctionClient = sanctionClient;
    }

    public void execute(Company domain) {
        sanctionClient.getCompanyRisk(domain.getRegistrationNumber());
        // KYC -> San
        // AML

    }
}
