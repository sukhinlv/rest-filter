package com.github.sukhin.condition;

import com.github.sukhin.Qualifier;
import org.jooq.Condition;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Filter condition interface.
 */
public interface FilterCondition {
    Function<String, Short> BASIC_SHORT_CONVERTER = Short::parseShort;
    Function<String, Integer> BASIC_INTEGER_CONVERTER = Integer::parseInt;
    Function<String, Long> BASIC_LONG_CONVERTER = Long::parseLong;
    Function<String, LocalDateTime> BASIC_LOCAL_DATE_TIME_CONVERTER = LocalDateTime::parse;
    Function<String, Boolean> BASIC_BOOLEAN_CONVERTER = Boolean::parseBoolean;
    UnaryOperator<String> BASIC_STRING_CONVERTER = s -> s;

    /**
     * Get filter condition for use in JOOQ API.
     * <p>
     * Map("ip_src", ["!=10.119.24.99"], "port_dst", ["<11", "100"]) => (ip_src != ?) AND (port_dst < ? OR port_dst == ?)
     * </p>
     *
     * @param qualifier   filter condition qualifier {@link Qualifier}
     * @param stringValue text representation of value for filtering
     * @return JOOQ filter condition {@link Condition}
     */
    Condition getCondition(Qualifier qualifier, String stringValue);
}
