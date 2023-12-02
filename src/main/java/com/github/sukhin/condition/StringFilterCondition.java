package com.github.sukhin.condition;

import com.github.sukhin.Qualifier;
import org.jooq.Condition;
import org.jooq.Converter;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.github.sukhin.Qualifier.EQUAL;
import static com.github.sukhin.Qualifier.LIKE;
import static com.github.sukhin.Qualifier.NOT_EQUAL;
import static com.github.sukhin.Qualifier.NOT_LIKE;
import static java.lang.String.format;

/**
 * Filter condition for field with type {@link String}.
 */
public class StringFilterCondition extends AbstractFilterCondition<String> {
    public StringFilterCondition(Field<String> field, Converter<String, String> converter) {
        super(field, converter);
    }

    public StringFilterCondition(Field<String> field, UnaryOperator<String> basicConverter) {
        super(field, basicConverter);
    }

    public StringFilterCondition(Field<String> field) {
        super(field, BASIC_STRING_CONVERTER);
    }

    @Override
    public Condition getCondition(Qualifier qualifier, String stringValue) {
        Optional<String> optionalValue = getConvertedValue(stringValue);
        if (optionalValue.isEmpty()) {
            return DSL.noCondition();
        }

        String value = optionalValue.get();
        value = format("%%%s%%", value);

        // The order in which conditions are checked matters
        if (EQUAL == qualifier) {
            return field.equalIgnoreCase(value);
        } else if (NOT_EQUAL == qualifier) {
            return field.notEqualIgnoreCase(value);
        } else if (NOT_LIKE == qualifier) {
            return field.notLikeIgnoreCase(value);
        } else if (LIKE == qualifier) {
            return field.likeIgnoreCase(value);
        }
        return DSL.noCondition();
    }
}
