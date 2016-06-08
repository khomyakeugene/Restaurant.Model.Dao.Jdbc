package com.company.restaurant.dao.jdbc;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yevhen on 19.05.2016.
 */
public abstract class JdbcDao<T> {
    public static final String SQL_MAX_STATEMENT = "MAX(%s)";

    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void databaseError(Exception e) {
        throw new RuntimeException(e);
    }

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

    protected T createObjectFromQuery(String query) {
        List<T> objectList = createObjectListFromQuery(query);

        return (objectList.size() > 0) ? objectList.get(0) : null;
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
}