package dio.compliance.domain;

import java.util.List;

/**
 * Conjunto consolidado das consultas externas realizadas para uma empresa.
 *
 * @param sanctions correspondências encontradas em listas de sanções
 * @param amlProfile perfil AML associado ao número de registro
 */
public record ComplianceScreening(
        List<SanctionIdentity> sanctions,
        AmlProfile amlProfile
) {
    /**
     * Identidade ou entidade encontrada em uma lista de sanções.
     *
     * @param name nome da entidade correspondida
     * @param sourceList lista onde a correspondência foi encontrada
     * @param reason motivo ou descrição da sanção
     * @param confidence índice de confiança entre zero e um
     */
    public record SanctionIdentity(
        String name,
        String sourceList,
        String reason,
        double confidence
    ) {

    }
    /**
     * Resultado normalizado da consulta AML.
     *
     * @param riskScore pontuação de risco fornecida pela fonte
     * @param riskFlags sinais textuais adicionais encontrados
     * @param isPep indica se existe pelo menos uma exposição política
     * @param exposures relações PEP encontradas
     */
    public record AmlProfile(
            int riskScore,
            List<String> riskFlags,
            boolean isPepPresent,
            List<PoliticalExposure> exposures
    ) {
        /**
         * Ocorrência de uma pessoa identificada como PEP.
         *
         * @param personName nome da pessoa exposta
         * @param publicOffice cargo público associado
         */
        public record PoliticalExposure(
                String personName,
                String publicOffice
        ){
        }
    }
}
