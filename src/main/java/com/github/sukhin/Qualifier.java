package com.github.sukhin;

/**
 * Filter conditions and sort order.
 */
public enum Qualifier {
    EQUAL("="),
    NOT_EQUAL("!="),
    LIKE("~"),
    NOT_LIKE("!~"),
    LESS_EQUAL("<="),
    GREATER_EQUAL(">="),
    LESS("<"),
    GREATER(">"),
    ASC("+"),
    DESC("-"),
    DO_NOTHING("");
    private final String sign;
    Qualifier(String sign) {
        this.sign = sign;
    }
    /**
     * Text representation of filter condition.
     *
     * @return text representation of filter condition
     */
    public String getSign() {
        return sign;
    }
}
