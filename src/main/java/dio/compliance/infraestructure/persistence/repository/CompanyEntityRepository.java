package dio.compliance.infraestructure.persistence.repository;

import dio.compliance.infraestructure.persistence.entity.CompanyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RepositoryRestResource(path = "companies")
public interface CompanyEntityRepository extends CrudRepository<CompanyEntity, UUID> {

}
