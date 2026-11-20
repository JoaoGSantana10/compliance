package dio.compliance.domain;

import java.util.UUID;

/**
 * Identificador único de uma empresa no modelo de domínio.
 *
 * @param id UUID usado para identificar a empresa
 */
public record CompanyId(UUID id) {

}
