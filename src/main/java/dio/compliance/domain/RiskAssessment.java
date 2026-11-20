package dio.compliance.domain;

/**
 * Resultado final de uma análise de risco.
 *
 * @param score pontuação obtida da fonte AML
 * @param level classificação qualitativa derivada do resultado
 * @param status decisão da política de compliance
 */
public record RiskAssessment(int score, RiskLevel level, RiskAssessmentStatus status) {
    /**
     * Cria uma avaliação calculando automaticamente o nível de risco.
     *
     * @param score pontuação obtida da fonte AML
     * @param status decisão produzida pela política
     */
    public RiskAssessment(int score, RiskAssessmentStatus status) {
        this(score, determineRiskLevel(score, status), status);
    }

    private static RiskLevel determineRiskLevel(int score, RiskAssessmentStatus status) {
        if (status == RiskAssessmentStatus.REJECTED) return RiskLevel.CRITICAL;
        if (score > 70) return RiskLevel.HIGH;
        if (score > 30) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }
}
