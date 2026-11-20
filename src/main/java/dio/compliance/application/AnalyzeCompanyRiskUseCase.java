package dio.compliance.application;

import dio.compliance.domain.Company;
import dio.compliance.domain.CompanyRepository;
import dio.compliance.domain.CompliancePolicy;
import dio.compliance.domain.ComplianceScreening;
import dio.compliance.infraestructure.rest.client.AntiMoneyLauderingClient;
import dio.compliance.infraestructure.rest.client.SanctionClient;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeCompanyRiskUseCase {
    private final SanctionClient sanctionClient;
    private final AntiMoneyLauderingClient antiMoneyLauderingClient;
    private final CompanyRepository companyRepository;

    public AnalyzeCompanyRiskUseCase(SanctionClient sanctionClient,
                                     AntiMoneyLauderingClient antiMoneyLauderingClient,
                                     CompanyRepository companyRepository) {
        this.sanctionClient = sanctionClient;
        this.antiMoneyLauderingClient = antiMoneyLauderingClient;
        this.companyRepository = companyRepository;
    }

    public void execute(Company company) {
       var sanctions = sanctionClient.getCompanyRisk(company.getRegistrationNumber()).toDomain();
       var amlProfile = antiMoneyLauderingClient.screening(company.getRegistrationNumber()).toDomain();

       var screening = new ComplianceScreening(sanctions, amlProfile);
       var riskAssessment = CompliancePolicy.evaluate(screening);

       company.applyRiskAssessment(riskAssessment);
       companyRepository.save(company);

        // KYC -> San
        // AML

    }
}
