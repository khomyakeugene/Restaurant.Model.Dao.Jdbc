package com.company.restaurant.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 21.05.2016.
 */
public abstract class JdbcDaoTable<T> extends JdbcDao<T> {
    private static final String SQL_ALL_FIELD_OF_ALL_RECORDS = "SELECT * FROM \"%s\"";
    private static final String SQL_SELECT_BY_FIELD_VALUE = "SELECT %s FROM \"%s\" WHERE (%s = %s)";
    private static final String SQL_SELECT_BY_TWO_FIELD_VALUE = "SELECT %s FROM \"%s\" WHERE (%s = %s) AND (%s = %s)";
    private static final String SQL_DELETE_EXPRESSION_PATTERN = "DELETE FROM \"%s\" WHERE (%s = %s)";
    private static final String SQL_UPDATE_BY_FIELD_VALUE = "UPDATE \"%s\" SET %s WHERE (%s = %s)";
    private static final String SQL_UPDATE_SET_SECTION_PART_PATTERN = "%s = %s";
    private static final String SQL_ALL_FIELDS_WILDCARD = "*";

    protected String tableName;
    protected String viewName;
    protected String orderByCondition;

    public JdbcDaoTable() {
        initMetadata();
    }

    protected abstract void initMetadata();

    protected abstract Map<String, Object> objectToDBMap(T object);

    public String getTableName() {
        return tableName;
    }

    private String getViewName() {
        return (viewName != null && viewName.length() > 0) ? viewName : tableName;
    }

    private String orderByCondition() {
        return (orderByCondition == null) ? "" : " " + orderByCondition;
    }

    private String orderByCondition(String selectFields) {
        return (selectFields.equals(SQL_ALL_FIELDS_WILDCARD)) ? orderByCondition() : "";
    }


    private String allQueryCondition() {
        return String.format(SQL_ALL_FIELD_OF_ALL_RECORDS, getViewName()) + orderByCondition();
    }

    private String fieldQueryCondition(String fieldName, Object value, String selectFields) {
        return String.format(SQL_SELECT_BY_FIELD_VALUE, selectFields, getViewName(), fieldName,
                JdbcDao.toString(value)) + orderByCondition(selectFields);
    }

    private String fieldQueryCondition(String fieldName, Object value) {
        return fieldQueryCondition(fieldName, value, SQL_ALL_FIELDS_WILDCARD);
    }

    protected String twoFieldsFromTableQueryCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2,
                                                      String selectFields) {
        return String.format(SQL_SELECT_BY_TWO_FIELD_VALUE, selectFields, getTableName(), fieldName_1, JdbcDao.toString(value_1),
                fieldName_2, JdbcDao.toString(value_2)) + orderByCondition(selectFields);
    }

    private String twoFieldsFromTableQueryCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2) {
        return twoFieldsFromTableQueryCondition(fieldName_1, value_1, fieldName_2, value_2, SQL_ALL_FIELDS_WILDCARD);
    }

    private String twoFieldsFromViewQueryCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2,
                                                   String selectFields) {
        return String.format(SQL_SELECT_BY_TWO_FIELD_VALUE, selectFields, getViewName(), fieldName_1,
                JdbcDao.toString(value_1), fieldName_2, JdbcDao.toString(value_2)) + orderByCondition(selectFields);
    }

    private String twoFieldsFromViewQueryCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2) {
        return twoFieldsFromViewQueryCondition(fieldName_1, value_1, fieldName_2, value_2, SQL_ALL_FIELDS_WILDCARD);
    }

    public T findObjectByFieldCondition(String fieldName, Object value) {
        return createObjectFromQuery(fieldQueryCondition(fieldName, value));
    }

    protected List<T> findObjectsByFieldCondition(String fieldName, Object value) {
        return createObjectListFromQuery(fieldQueryCondition(fieldName, value));
    }

    protected T findObjectByTwoFieldCondition(String fieldName_1, Object value_1, String fieldName_2, Object value_2) {
        return createObjectFromQuery(twoFieldsFromTableQueryCondition(fieldName_1, value_1, fieldName_2, value_2));
    }

    protected List<T> findObjectsFromTableByTwoFieldCondition(String fieldName_1, Object value_1, String fieldName_2,
                                                              Object value_2) {
        return createObjectListFromQuery(twoFieldsFromTableQueryCondition(fieldName_1, value_1, fieldName_2, value_2));
    }

    protected T findObjectFromTableByTwoFieldCondition(String fieldName_1, Object value_1, String fieldName_2,
                                                       Object value_2) {
        return createObjectFromQuery(twoFieldsFromTableQueryCondition(fieldName_1, value_1, fieldName_2, value_2));
    }

    protected List<T> findObjectsFromViewByTwoFieldCondition(String fieldName_1, Object value_1, String fieldName_2,
                                                             Object value_2) {
        return createObjectListFromQuery(twoFieldsFromViewQueryCondition(fieldName_1, value_1, fieldName_2, value_2));
    }

    protected T findObjectFromViewByTwoFieldCondition(String fieldName_1, Object value_1, String fieldName_2,
                                                             Object value_2) {
        return createObjectFromQuery(twoFieldsFromViewQueryCondition(fieldName_1, value_1, fieldName_2, value_2));
    }

    protected List<T> findAllObjects() {
        return createObjectListFromQuery(allQueryCondition());
    }

    public String getOneFieldByFieldCondition(String selectedField, String fieldName, Object value) {
        String result = null;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(fieldQueryCondition(fieldName, value, selectedField));
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
        } catch (SQLException e) {
            databaseError(e);
        }

        return result;
    }

    private String buildDeleteExpression(String fieldName, Object value) {
        return String.format(SQL_DELETE_EXPRESSION_PATTERN, tableName, fieldName, JdbcDao.toString(value));
    }

    public String delRecordByFieldCondition(String fieldName, Object value) {
        String result = null;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(buildDeleteExpression(fieldName, value));
        } catch (SQLException e) {
            result = e.getMessage();
        }

        return result;
    }

    protected Map<String, Object> getObjectToDBMap(T object) {
        Map<String, Object> objectToDBMap = objectToDBMap(object);

        // Exclude null-value data
        Map<String, Object> result = new HashMap<>();
        objectToDBMap.forEach((k, v) -> {
            if (v != null) {
                result.put(k, v);
            }
        });

        return result;
    }

    protected String buildInsertExpression(String pattern, T object) {
        Map<String, Object> clarifiedDBMap = getObjectToDBMap(object);

        String fieldSequence = String.join(",",
                (CharSequence[]) clarifiedDBMap.keySet().stream().toArray(String[]::new));
        String valueSequence = String.join(",",
                (CharSequence[]) clarifiedDBMap.values().stream().map(v -> (JdbcDao.toString(v))).toArray(String[]::new));

        return String.format(pattern, fieldSequence, valueSequence);
    }

    protected void updateOneFieldByOneFieldCondition(String updateFieldName, Object updateFieldValue,
                                                     String conditionFieldName, Object conditionFieldValue) {
        String query = String.format(SQL_UPDATE_BY_FIELD_VALUE, tableName,
                String.format(SQL_UPDATE_SET_SECTION_PART_PATTERN, updateFieldName, toString(updateFieldValue)),
                conditionFieldName, toString(conditionFieldValue));

        executeUpdate(query);
    }
}
