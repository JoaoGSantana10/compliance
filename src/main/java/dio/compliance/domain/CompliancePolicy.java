package dio.compliance.domain;

/**
 * Política central que converte os sinais de sanções e AML em uma decisão.
 *
 * <p>Uma sanção com confiança acima de 80% resulta em rejeição. PEP ou score AML
 * acima de 70 resulta em revisão manual. Sem nenhum desses sinais, o status é
 * aprovado. O nível de risco deriva do status e do score AML.</p>
 */
public class CompliancePolicy {

    /**
     * Avalia uma triagem de compliance e produz uma avaliação completa.
     *
     * @param screening resultados consolidados das fontes de sanções e AML
     * @return decisão, nível e score calculados pela política
     */
    public static RiskAssessment evaluate(ComplianceScreening screening){
        var status = RiskAssessmentStatus.APROVED;

        boolean hasCriticalSanction = screening.sanctions().stream()
                .anyMatch(s -> s.confidence() > 0.8);

        if (hasCriticalSanction) {
            status = RiskAssessmentStatus.REJECTED;
        } else if (screening.amlProfile().isPepPresent()) {
            status = RiskAssessmentStatus.MANUAL_REVIEW;
        }

        int amlScore = screening.amlProfile().riskScore();

        if (status == RiskAssessmentStatus.APROVED && amlScore > 70){
            status = RiskAssessmentStatus.MANUAL_REVIEW;
        }

        return new RiskAssessment(amlScore, status);
    }
}
