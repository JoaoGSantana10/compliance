package dio.compliance.infraestructure.persistence.event;

import dio.compliance.application.AnalyzeCompanyRiskUseCase;
import dio.compliance.infraestructure.persistence.entity.CompanyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
/**
 * Processa eventos disparados pelo Spring Data REST.
 *
 * <p>Após a criação de uma empresa, inicia automaticamente o caso de uso que
 * consulta sanções e AML e salva a avaliação produzida.</p>
 */
public class CompanyEventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CompanyEventHandler.class);

    private final AnalyzeCompanyRiskUseCase analyzeCompanyRiskUseCase;

    /**
     * Cria o manipulador com o caso de uso de análise.
     *
     * @param analyzeCompanyRiskUseCase caso de uso executado após a criação
     */
    public CompanyEventHandler(AnalyzeCompanyRiskUseCase analyzeCompanyRiskUseCase) {
        this.analyzeCompanyRiskUseCase = analyzeCompanyRiskUseCase;
    }

    @HandleAfterCreate
    /**
     * Trata a criação de uma empresa após sua persistência.
     *
     * @param entity empresa recém-criada
     */
    public void handleCompanyCreated(CompanyEntity entity) {
        LOG.info("handleAfterCreateEvent {}", entity);
        this.analyzeCompanyRiskUseCase.execute(entity.toDomain());
    }
}
