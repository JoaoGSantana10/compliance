package dio.compliance.domain;

/**
 * Decisão administrativa produzida pela política de compliance.
 */
public enum RiskAssessmentStatus {
    /** Empresa aprovada pelas regras atuais da política. */
    APROVED,
    /** Empresa rejeitada por correspondência crítica em sanções. */
    REJECTED,
    /** Empresa exige revisão manual por PEP ou score AML elevado. */
    MANUAL_REVIEW
}
