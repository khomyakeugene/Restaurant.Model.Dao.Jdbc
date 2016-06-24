package com.company.restaurant.dao.jdbc.proto;

import com.company.util.DataIntegrityException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by Yevhen on 19.05.2016.
 */
public abstract class JdbcDaoTableWithId<T> extends JdbcDaoTable<T> {
    private static final String CANNOT_GET_LAST_GENERATED_ID_PATTERN = "Add record problem: cannot get last generated %s.%s value";
    private static final String CANNOT_DELETE_RECORD_PATTERN = "Cannot delete record in table <%s> because it is impossible " +
            "to detect condition value for field <%s> nor for field <%s>";

    protected String idFieldName;
    protected String nameFieldName;

    protected abstract void setId(int id, T object);

    public T findObjectById(int id) {
        return findObjectByFieldCondition(idFieldName, id);
    }

    protected T findObjectByName(String name) {
        return findObjectByFieldCondition(nameFieldName, name);
    }

    protected T retrieveAddedRecord(T object, ResultSet resultSet) {
        T result = object;

        try {
            if (resultSet.next()) {
                int id = resultSet.getInt(idFieldName);
                // Store new generated id in the "source variant" of added <object> - at least, it is important
                // to support data integrity
                setId(id, object);
                result = findObjectById(id);
            } else {
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

    public void delRecord(T object) {
        AbstractMap.SimpleEntry<String, Object> keyFieldData = getKeyFieldNameAndFieldValue(object);

        delRecordByFieldCondition(keyFieldData.getKey(), keyFieldData.getValue());
    }

    protected void delRecordById(int id) {
        delRecordByFieldCondition(idFieldName, id);
    }

    protected void delRecordByName(String name) {
        delRecordByFieldCondition(nameFieldName, name);
    }

    protected T updRecord(T object, String updateFieldName, Object updateFieldValue) {
        AbstractMap.SimpleEntry<String, Object> keyFieldData = getKeyFieldNameAndFieldValue(object);

        String keyFieldName = keyFieldData.getKey();
        Object keyFieldValue = keyFieldData.getValue();
        updateOneFieldByOneFieldCondition(updateFieldName, updateFieldValue, keyFieldName, keyFieldValue);
        // Retrieve current state of object (hope, that no key field (in case if this key field is not from primary
        // key) were updated!)
        return findObjectByFieldCondition(keyFieldName, keyFieldValue);
    }
}
