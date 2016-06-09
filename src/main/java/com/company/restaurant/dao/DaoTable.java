package com.company.restaurant.dao;

/**
 * Created by Yevhen on 09.06.2016.
 */

public abstract class DaoTable {
    private static final String SQL_MAX_STATEMENT = "MAX(%s)";
    private static final String SQL_DELETE_EXPRESSION_PATTERN = "DELETE FROM \"%s\" WHERE (%s = %s)";
    private static final String SQL_UPDATE_BY_FIELD_VALUE = "UPDATE \"%s\" SET %s WHERE (%s = %s)";
    private static final String SQL_UPDATE_SET_SECTION_PART_PATTERN = "%s = %s";
    private static final String SQL_ALL_FIELD_OF_ALL_RECORDS = "SELECT * FROM \"%s\"";
    private static final String SQL_SELECT_BY_FIELD_VALUE = "SELECT %s FROM \"%s\" WHERE (%s = %s)";
    private static final String SQL_SELECT_BY_TWO_FIELD_VALUE = "SELECT %s FROM \"%s\" WHERE (%s = %s) AND (%s = %s)";
    private static final String SQL_ALL_FIELDS_WILDCARD = "*";

    protected String tableName;
    protected String viewName;
    protected String orderByCondition;

    public static String toString(Object object) {
        String result;

        if (object == null) {
            result = "null";
        } else {
            result = object.toString();
            if (object instanceof String) {
                result =  "'" + result + "'";
            }
        }

        return result;
    }

    protected abstract void initMetadata();

    private String getViewName() {
        return (viewName != null && viewName.length() > 0) ? viewName : tableName;
    }

    private String orderByCondition() {
        return (orderByCondition == null) ? "" : " " + orderByCondition;
    }

    private String orderByCondition(String selectFields) {
        return (selectFields.equals(SQL_ALL_FIELDS_WILDCARD)) ? orderByCondition() : "";
    }

    protected String allQueryCondition() {
        return String.format(SQL_ALL_FIELD_OF_ALL_RECORDS, getViewName()) + orderByCondition();
    }

    protected String maxFieldValueExpression(String fieldName) {
        return String.format(SQL_MAX_STATEMENT, fieldName);
    }

    protected String fieldQueryCondition(String fieldName, Object value, String selectFields) {
        return String.format(SQL_SELECT_BY_FIELD_VALUE, selectFields, getViewName(), fieldName,
                DaoTable.toString(value)) + orderByCondition(selectFields);
    }

    protected String fieldQueryCondition(String fieldName, Object value) {
        return fieldQueryCondition(fieldName, value, SQL_ALL_FIELDS_WILDCARD);
    }

    protected String twoFieldsFromTableQueryCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2,
                                                      String selectFields) {
        return String.format(SQL_SELECT_BY_TWO_FIELD_VALUE, selectFields, tableName, fieldName_1, DaoTable.toString(value_1),
                fieldName_2, DaoTable.toString(value_2)) + orderByCondition(selectFields);
    }

    protected String twoFieldsFromTableQueryCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2) {
        return twoFieldsFromTableQueryCondition(fieldName_1, value_1, fieldName_2, value_2, SQL_ALL_FIELDS_WILDCARD);
    }

    private String twoFieldsFromViewQueryCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2,
                                                   String selectFields) {
        return String.format(SQL_SELECT_BY_TWO_FIELD_VALUE, selectFields, getViewName(), fieldName_1,
                DaoTable.toString(value_1), fieldName_2, DaoTable.toString(value_2)) + orderByCondition(selectFields);
    }

    protected String twoFieldsFromViewQueryCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2) {
        return twoFieldsFromViewQueryCondition(fieldName_1, value_1, fieldName_2, value_2, SQL_ALL_FIELDS_WILDCARD);
    }

    protected String buildDeleteExpression(String fieldName, Object value) {
        return String.format(SQL_DELETE_EXPRESSION_PATTERN, tableName, fieldName, DaoTable.toString(value));
    }

    protected String buildOneFieldByOneFieldUpdateCondition(String updateFieldName,
                                                            Object updateFieldValue,
                                                            String conditionFieldName,
                                                            Object conditionFieldValue) {
        return String.format(SQL_UPDATE_BY_FIELD_VALUE, tableName,
                String.format(SQL_UPDATE_SET_SECTION_PART_PATTERN, updateFieldName, toString(updateFieldValue)),
                conditionFieldName, toString(conditionFieldValue));
    }
}
