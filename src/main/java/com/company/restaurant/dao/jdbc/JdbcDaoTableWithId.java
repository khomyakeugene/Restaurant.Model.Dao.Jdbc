package com.company.restaurant.dao.jdbc;

import com.company.util.DataIntegrityException;

import java.sql.*;
import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by Yevhen on 19.05.2016.
 */
public abstract class JdbcDaoTableWithId<T> extends JdbcDaoTable<T> {
    private static final String CANNOT_GET_LAST_GENERATED_ID_PATTERN = "Add record problem: cannot get last generated %s.%s value";
    private static final String CANNOT_DELETE_RECORD_PATTERN = "Cannot delete record in table <%s> because it is impossible " +
            "to detect condition value for field <%s> nor for field <%s>";
    private static final String SQL_INSERT_EXPRESSION_PATTERN_PART_1 = "INSERT INTO \"%s\"";
    private static final String SQL_INSERT_EXPRESSION_PATTERN_PART_2 = " (%s) VALUES(%s)";

    protected String idFieldName;
    protected String nameFieldName;

    protected abstract void setId(int id, T object);

    public T findObjectById(int id) {
        return findObjectByFieldCondition(idFieldName, id);
    }

    public T findObjectByName(String name) {
        return findObjectByFieldCondition(nameFieldName, name);
    }

    public T addRecord(T object) {
        T result = object;

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {

            String s = buildInsertExpression(String.format(SQL_INSERT_EXPRESSION_PATTERN_PART_1,
                    tableName) + SQL_INSERT_EXPRESSION_PATTERN_PART_2, object);

            statement.executeUpdate(s, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(idFieldName);
                // Store new generated id in the "source variant" of added <object> - at least, it is important
                // to support data integrity
                setId(id, object);
                // Retrieve all fields in case if <tableName> != <viewName>, because entity item can contain also
                // <viewName>-fields
                if (viewName != null && !viewName.equals(tableName)) {
                    result = findObjectById(id);
                }
            } else  {
                throw new SQLException(String.format(CANNOT_GET_LAST_GENERATED_ID_PATTERN, tableName, idFieldName));
            }

        } catch (SQLException e) {
            databaseError(e);
        }

        return result;
    }

    private AbstractMap.SimpleEntry<String, Object> getKeyFieldNameAndFieldValue(T object) {
        Map<String, Object> objectToDBMap = getObjectToDBMap(object);

        String fieldName = idFieldName;
        Object fieldValue = objectToDBMap.get(fieldName);
        if (fieldValue == null) {
            fieldName = nameFieldName;
            fieldValue = objectToDBMap.get(fieldName);
            if (fieldValue == null) {
                throw new DataIntegrityException(String.format(CANNOT_DELETE_RECORD_PATTERN, tableName, idFieldName,
                        nameFieldName));
            }
        }

        return new AbstractMap.SimpleEntry<>(fieldName, fieldValue);
    }

    public String delRecord(T object) {
        AbstractMap.SimpleEntry<String, Object> keyFieldData = getKeyFieldNameAndFieldValue(object);

        return delRecordByFieldCondition(keyFieldData.getKey(), keyFieldData.getValue());
    }

    public String delRecordById(int id) {
        return delRecordByFieldCondition(idFieldName, id);
    }

    public String delRecordByName(String name) {
        return delRecordByFieldCondition(nameFieldName, name);
    }

    public T updRecord(T object, String updateFieldName, Object updateFieldValue) {
        AbstractMap.SimpleEntry<String, Object> keyFieldData = getKeyFieldNameAndFieldValue(object);

        String keyFieldName = keyFieldData.getKey();
        Object keyFieldValue = keyFieldData.getValue();
        updateOneFieldByOneFieldCondition(updateFieldName, updateFieldValue, keyFieldName, keyFieldValue);
        // Retrieve current state of object (hope, that no key field (in case if this key field is not from primary
        // key) were updated!)
        return findObjectByFieldCondition(keyFieldName, keyFieldValue);
    }
}
