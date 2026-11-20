package dio.compliance.domain;

/**
 * Classificação qualitativa da severidade do risco.
 */
public enum RiskLevel {
    /** Risco baixo segundo a pontuação atual. */
    LOW,
    /** Risco médio segundo a pontuação atual. */
    MEDIUM,
    /** Risco alto segundo a pontuação ou status atual. */
    HIGH,
    /** Risco crítico associado a rejeição por sanção. */
    CRITICAL
}
