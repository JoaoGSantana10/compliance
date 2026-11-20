package dio.compliance.domain;

/**
 * Porta de domínio utilizada para persistir empresas e suas avaliações.
 */
public interface CompanyRepository {
    /**
     * Persiste o estado atual da empresa.
     *
     * @param company empresa a ser salva
     */
    void save(Company company);
}
