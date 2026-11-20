package dio.compliance.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
/**
 * Representa uma empresa analisada pelo fluxo de KYC/compliance.
 *
 * <p>Contém sua identificação de domínio, nome, número de registro e, opcionalmente,
 * a avaliação de risco calculada pela política de compliance.</p>
 */
public class Company {
    private CompanyId id;
    private String name;
    private String registrationNumber;
    private Optional<RiskAssessment> riskAssessment;

    /**
     * Associa ou substitui a avaliação de risco da empresa.
     *
     * @param riskAssessment avaliação calculada pelas consultas externas
     */
    public void applyRiskAssessment(RiskAssessment riskAssessment) {
        this.riskAssessment = Optional.of(riskAssessment);
    }
}
