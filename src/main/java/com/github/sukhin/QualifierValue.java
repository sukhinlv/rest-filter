package com.github.sukhin;

/**
 * Filter qualifier and text value of condition.
 */
public record QualifierValue(
        Qualifier qualifier,
        String value
) {
}
