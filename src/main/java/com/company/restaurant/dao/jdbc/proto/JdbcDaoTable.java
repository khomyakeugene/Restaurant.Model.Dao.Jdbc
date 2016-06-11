package com.company.restaurant.dao.jdbc.proto;

import com.company.restaurant.dao.proto.DaoTable;
import com.company.util.Util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 21.05.2016.
 */
public abstract class JdbcDaoTable<T> extends DaoTable {

    protected DataSource dataSource;

    public JdbcDaoTable() {
        initMetadata();
    }

    protected abstract Map<String, Object> objectToDBMap(T object);

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void databaseError(Exception e) {
        throw new RuntimeException(e);
    }

    protected abstract T newObject(ResultSet resultSet) throws SQLException;

    private T createObject(ResultSet resultSet) {
        T result = null;

        if (resultSet != null) {
            try {
                result = newObject(resultSet);
            } catch (SQLException e) {
                databaseError(e);
            }
        }

        return result;
    }

    protected List<T> createObjectListFromQuery(String query) {
        ArrayList<T> result = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                result.add(createObject(resultSet));
            }
        } catch (SQLException e) {
            databaseError(e);
        }

        return result;
    }

    private T createObjectFromQuery(String query) {
        return Util.getFirstFromList(createObjectListFromQuery(query));
    }

    public String executeUpdate(String query) {
        String result = null;

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            result = e.getMessage();
        }

        return result;
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

    protected String getOneFieldByFieldCondition(String selectedField, String fieldName, Object value) {
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
                (CharSequence[]) clarifiedDBMap.values().stream().map(v -> (toString(v))).toArray(String[]::new));

        return String.format(pattern, fieldSequence, valueSequence);
    }

    protected void updateOneFieldByOneFieldCondition(String updateFieldName, Object updateFieldValue,
                                                     String conditionFieldName, Object conditionFieldValue) {
        executeUpdate(buildOneFieldByOneFieldUpdateCondition(updateFieldName, updateFieldValue,
                conditionFieldName, conditionFieldValue));
    }
}
