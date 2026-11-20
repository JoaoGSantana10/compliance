package dio.compliance.infraestructure.rest.dto;

import dio.compliance.domain.ComplianceScreening;

import java.util.List;

/**
 * Resposta JSON da API simulada de AML.
 *
 * @param riskScore pontuação de risco recebida
 * @param flags sinais adicionais retornados pela API
 * @param pep dados normalizados de pessoas politicamente expostas
 */
public record AmlResult(int riskScore, List<String> flags, Pep pep) {
    /**
     * Dados PEP recebidos pela API.
     *
     * @param isPep indica presença de exposição política
     * @param occurrences ocorrências detalhadas
     */
    public record Pep(boolean isPep, List<PepOccurrence> occurrences) {
        /**
         * Ocorrência individual de uma pessoa PEP.
         *
         * @param personName nome da pessoa
         * @param position cargo público associado
         */
        public record PepOccurrence(
                String personName,
                String position
        ) {}
    }

    /**
     * Converte o payload HTTP para o perfil AML do domínio.
     *
     * @return perfil AML independente do transporte
     */
    public ComplianceScreening.AmlProfile toDomain() {
        List<ComplianceScreening.AmlProfile.PoliticalExposure> exposures = pep().occurrences.stream()
                .map(occ -> new ComplianceScreening.AmlProfile.PoliticalExposure(
                        occ.personName(),
                        occ.position()
                ))
                .toList();

        return new ComplianceScreening.AmlProfile(
                riskScore,
                flags() != null ? flags() : List.of(),
                pep().isPep(),
                exposures
        );
    }

}
