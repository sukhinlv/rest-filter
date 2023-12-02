package com.github.sukhin;

import com.github.sukhin.condition.FilterCondition;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.sukhin.Qualifier.ASC;
import static com.github.sukhin.Qualifier.DESC;
import static com.github.sukhin.Qualifier.GREATER;
import static com.github.sukhin.Qualifier.GREATER_EQUAL;
import static com.github.sukhin.Qualifier.LESS;
import static com.github.sukhin.Qualifier.LESS_EQUAL;
import static com.github.sukhin.Qualifier.NOT_EQUAL;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Utility class for creating filtering and sorting conditions of JOOQ API.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterUtils {
    /**
     * Converts a list of strings like "!=20" to a list of objects {@link QualifierValue}.
     *
     * @param stringQualifierValues list of strings like "!=20" (filter condition and its value)
     * @return list of objects of type {@link QualifierValue}
     */
    public static List<QualifierValue> parseQualifierValues(List<String> stringQualifierValues) {
        List<QualifierValue> qualifierValues = new ArrayList<>();
        for (String stringValue : stringQualifierValues) {
            for (Qualifier qualifier : Qualifier.values()) {
                String sign = qualifier.getSign();
                if (stringValue.startsWith(sign)) {
                    qualifierValues.add(
                            new QualifierValue(qualifier,
                                    stringValue.substring(stringValue.indexOf(sign) + sign.length()).trim())
                    );
                    break;
                }
            }
        }
        return qualifierValues;
    }

    /**
     * Gets fields and sort order from objects of type {@link QualifierValue}.
     *
     * @param fields                fields that can be used for sorting
     * @param stringQualifierValues list of filter conditions
     * @return list of fields to sort
     */
    public static List<SortField<?>> getSortFieldsFromQualifierValues(Map<String, Field<?>> fields,
                                                                      List<String> stringQualifierValues) {
        return getSortFieldsFromQualifierValues(fields, stringQualifierValues, null);
    }

    /**
     * Gets fields and sort order from objects of type {@link QualifierValue}.
     *
     * @param fields                fields that can be used for sorting
     * @param initialSortFields     already existing sort fields
     * @param stringQualifierValues list of filter conditions
     * @return list of fields to sort
     */
    public static List<SortField<?>> getSortFieldsFromQualifierValues(Map<String, Field<?>> fields,
                                                                      List<String> stringQualifierValues,
                                                                      List<SortField<?>> initialSortFields) {
        List<SortField<?>> sortFields =
                nonNull(initialSortFields) ? new ArrayList<>(initialSortFields) : new ArrayList<>();
        List<QualifierValue> qualifierValues = parseQualifierValues(stringQualifierValues);
        for (QualifierValue qualifierValue : qualifierValues) {
            if (fields.containsKey(qualifierValue.value())) {
                if (ASC == qualifierValue.qualifier()) {
                    sortFields.add(fields.get(qualifierValue.value()).asc());
                } else if (DESC == qualifierValue.qualifier()) {
                    sortFields.add(fields.get(qualifierValue.value()).desc());
                }
            }
        }
        return sortFields;
    }

    /**
     * Get a filter condition based on a list of filter fields and filter conditions.
     *
     * @param filterConditions list of fields and filtering conditions for fields of a certain type
     * @param filter           list of fields and filtering conditions
     * @return filter condition based on a list of filter fields and filter conditions
     */
    public static Condition getConditionFromFilter(Map<String, FilterCondition> filterConditions,
                                                   Map<String, List<String>> filter) {
        return getConditionFromFilter(filterConditions, filter, null);
    }

    /**
     * Get a filter condition based on a list of filter fields and filter conditions.
     *
     * @param filterConditions list of fields and filtering conditions for fields of a certain type
     * @param filter           list of fields and filtering conditions
     * @param initialCondition initial filter condition value
     * @return filter condition based on a list of filter fields and filter conditions
     */
    public static Condition getConditionFromFilter(Map<String, FilterCondition> filterConditions,
                                                   Map<String, List<String>> filter,
                                                   Condition initialCondition) {
        Condition condition = nonNull(initialCondition) ? initialCondition : DSL.noCondition();
        for (Map.Entry<String, List<String>> entry : filter.entrySet()) {
            String fieldName = entry.getKey();
            List<String> filterValues = entry.getValue();
            if (filterConditions.containsKey(fieldName)) {
                Condition innerCondition = DSL.noCondition();
                FilterCondition filterCondition =
                        filterConditions.get(fieldName);
                List<QualifierValue> qualifierValues =
                        parseQualifierValues(filterValues);
                for (QualifierValue qualifierValue : qualifierValues) {
                    innerCondition =
                            innerCondition.or(filterCondition.getCondition(qualifierValue.qualifier(),
                                    qualifierValue.value()));
                }
                condition = condition.and(innerCondition);
            }
        }
        return condition;
    }

    public static int compareStringDates(String date1, String date2) {
        LocalDateTime dateTime1 = LocalDateTime.parse(date1, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime dateTime2 = LocalDateTime.parse(date2, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return dateTime1.compareTo(dateTime2);
    }

    public static boolean isStringDateQualified(List<QualifierValue> qualifierValues, String value) {
        if (isNull(qualifierValues) || qualifierValues.isEmpty()) {
            return true;
        }

        LocalDateTime date;
        boolean pass = false;
        try {
            date = LocalDateTime.parse(value,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            for (QualifierValue qualifierValue : qualifierValues) {
                int compareResult =
                        date.compareTo(LocalDateTime.parse(qualifierValue.value(),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                if (Qualifier.EQUAL == qualifierValue.qualifier()) {
                    pass = pass || compareResult == 0;
                } else if (NOT_EQUAL == qualifierValue.qualifier()) {
                    pass = pass || compareResult != 0;
                } else if (LESS_EQUAL == qualifierValue.qualifier()) {
                    pass = pass || compareResult <= 0;
                } else if (GREATER_EQUAL == qualifierValue.qualifier()) {
                    pass = pass || compareResult >= 0;
                } else if (LESS == qualifierValue.qualifier()) {
                    pass = pass || compareResult < 0;
                } else if (GREATER == qualifierValue.qualifier()) {
                    pass = pass || compareResult > 0;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return pass;
    }

    public static boolean isStringQualified(List<QualifierValue> qualifierValues, String value) {
        if (isNull(qualifierValues) || qualifierValues.isEmpty()) {
            return true;
        }
        if (isNull(value)) {
            return false;
        }

        boolean pass = false;
        for (QualifierValue qualifierValue : qualifierValues) {
            if (Qualifier.EQUAL == qualifierValue.qualifier()) {
                pass = pass || value.equalsIgnoreCase(qualifierValue.value());
            } else if (NOT_EQUAL == qualifierValue.qualifier()) {
                pass = pass || !value.equalsIgnoreCase(qualifierValue.value());
            } else if (Qualifier.LIKE == qualifierValue.qualifier()) {
                pass = pass ||
                        value.toLowerCase().contains(qualifierValue.value().toLowerCase());
            } else if (Qualifier.NOT_LIKE == qualifierValue.qualifier()) {
                pass = pass || !
                        value.toLowerCase().contains(qualifierValue.value().toLowerCase());
            }
        }
        return pass;
    }
}
