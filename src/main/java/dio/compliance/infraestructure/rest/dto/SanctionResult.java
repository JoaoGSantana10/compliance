package dio.compliance.infraestructure.rest.dto;

import dio.compliance.domain.ComplianceScreening;

import java.util.List;

/**
 * Resposta JSON da API simulada de sanções.
 *
 * @param matches lista de correspondências retornadas pela fonte
 */
public record SanctionResult(List<SanctionMatch> matches) {
    /**
     * Correspondência individual encontrada em uma lista.
     *
     * @param entity entidade correspondida
     * @param list nome da lista de sanções
     * @param reason motivo da sanção
     * @param confidenceScore índice de confiança, quando fornecido
     */
    public record SanctionMatch(
       String entity,
       String list,
       String reason,
       Double confidenceScore
    ) {}

    /**
     * Converte a resposta externa para identidades do modelo de domínio.
     *
     * @return lista normalizada, vazia quando não houver correspondências válidas
     */
    public List<ComplianceScreening.SanctionIdentity> toDomain() {
        if (matches == null) {
            return List.of();
        }

        return matches().stream()
                .map(match -> new ComplianceScreening.SanctionIdentity(
                        match.entity(),
                        match.list(),
                        match.reason(),
                        match.confidenceScore() != null ? match.confidenceScore() : 0.0
                ))
                .toList();
    }
}

/*

Example JSON payload:

{
    "matches": [
        {
        "entity": "{{urlParam 'registrationNumber'}}",
        "list": "OFAC SDN List",
        "reason": "Financing of Prohibited Entities",
        "confidenceScore": 0.98
        }
    ]
}


 */
