package dio.compliance.application;

import dio.compliance.domain.Company;
import dio.compliance.domain.CompanyRepository;
import dio.compliance.domain.CompliancePolicy;
import dio.compliance.domain.ComplianceScreening;
import dio.compliance.infraestructure.rest.client.AntiMoneyLauderingClient;
import dio.compliance.infraestructure.rest.client.SanctionClient;
import org.springframework.stereotype.Service;

@Service
/**
 * Caso de uso que consolida sanções e perfil AML e atualiza o risco da empresa.
 *
 * <p>O fluxo consulta os dois clientes externos Mockoon, transforma os dados
 * transportados em objetos de domínio, aplica a política vigente e persiste a
 * avaliação por meio da abstração {@link CompanyRepository}.</p>
 */
public class AnalyzeCompanyRiskUseCase {
    private final SanctionClient sanctionClient;
    private final AntiMoneyLauderingClient antiMoneyLauderingClient;
    private final CompanyRepository companyRepository;

    /**
     * Cria o caso de uso com suas dependências técnicas.
     *
     * @param sanctionClient cliente da API simulada de sanções
     * @param antiMoneyLauderingClient cliente da API simulada de AML
     * @param companyRepository repositório utilizado para salvar o resultado
     */
    public AnalyzeCompanyRiskUseCase(SanctionClient sanctionClient,
                                     AntiMoneyLauderingClient antiMoneyLauderingClient,
                                     CompanyRepository companyRepository) {
        this.sanctionClient = sanctionClient;
        this.antiMoneyLauderingClient = antiMoneyLauderingClient;
        this.companyRepository = companyRepository;
    }

    /**
     * Analisa uma empresa e salva uma avaliação atualizada.
     *
     * @param company empresa identificada pelo número de registro empresarial
     * @throws IllegalArgumentException se um valor obrigatório de domínio estiver ausente
     */
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
