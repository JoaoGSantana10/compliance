package dio.compliance.infraestructure.persistence.entity;

import dio.compliance.domain.Company;
import dio.compliance.domain.CompanyId;
import dio.compliance.domain.RiskAssessment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.util.Optional;
import java.util.UUID;

@KeySpace("companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entidade persistida pelo Spring Data para representar uma empresa.
 *
 * <p>Esta classe mantém o mesmo estado necessário ao repositório REST, mas usa
 * tipos apropriados à persistência. Os métodos de conversão isolam esse formato
 * do modelo de domínio.</p>
 */
public class CompanyEntity {
    @Id
    private UUID id;
    private String name, registrationNumber;
    private RiskAssessment riskAssessment;

    /**
     * Converte uma empresa de domínio para a entidade persistível.
     *
     * @param company objeto de domínio a converter
     * @return entidade pronta para ser salva pelo Spring Data
     */
    public static CompanyEntity from(Company company){
        return new CompanyEntity(
                company.getId().id(),
                company.getName(),
                company.getRegistrationNumber(),
                company.getRiskAssessment().orElse(null)
        );
    }

    /**
     * Converte esta entidade para o modelo de domínio.
     *
     * @return empresa com identificador e avaliação normalizados
     */
    public Company toDomain(){
        return new Company(
                new CompanyId(this.getId()),
                this.getName(),
                this.getRegistrationNumber(),
                Optional.ofNullable(this.getRiskAssessment())
        );
    }
}
