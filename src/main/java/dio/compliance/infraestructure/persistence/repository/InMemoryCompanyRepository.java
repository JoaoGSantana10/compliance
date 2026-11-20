package dio.compliance.infraestructure.persistence.repository;

import dio.compliance.domain.Company;
import dio.compliance.domain.CompanyRepository;
import dio.compliance.infraestructure.persistence.entity.CompanyEntity;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Adaptador que implementa o contrato de domínio com o repositório Spring Data.
 *
 * <p>Como {@link CompanyEntityRepository} usa repositório Map, os dados são
 * mantidos em memória enquanto o processo da aplicação está ativo.</p>
 */
public class InMemoryCompanyRepository implements CompanyRepository {
    private final CompanyEntityRepository repository;

    /**
     * Cria o adaptador com o repositório Spring Data subjacente.
     *
     * @param repository repositório Map usado para armazenar entidades
     */
    public InMemoryCompanyRepository(CompanyEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    /**
     * Converte e persiste uma empresa no repositório em memória.
     *
     * @param company empresa já avaliada pelo caso de uso
     */
    public void save(Company company) {
        var entity = CompanyEntity.from(company);
        repository.save(entity);
    }
}
