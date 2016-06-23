package com.company.restaurant.dao.jdbc.proto;

import com.company.restaurant.model.JoinObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yevhen on 23.06.2016.
 */
public abstract class JdbcDaoJoinTable <T extends JoinObject> extends JdbcDaoTable<T>  {
    private static final String SQL_INSERT_EXPRESSION_PATTERN = "INSERT INTO \"%s\" (%s, %s) VALUES(%d, %d)";
    private static final String SQL_DELETE_EXPRESSION_PATTERN = "DELETE FROM \"%s\" WHERE (%s = %d) AND (%s = %d)";

    protected String firstIdFieldName;
    protected String secondIdFieldName;

    @Override
    protected Map<String, Object> objectToDBMap(JoinObject object) {
        return new HashMap<>();
    }

    public void addRecord(int firstId, int secondId) {
        JoinObject joinObject = new JoinObject();
        joinObject.setFirstId(firstId);
        joinObject.setSecondId(secondId);

        executeUpdate(String.format(SQL_INSERT_EXPRESSION_PATTERN, tableName, firstIdFieldName, secondIdFieldName,
                firstId, secondId));
    }

    public int delRecord(int firstId, int secondId) {
        return executeUpdate(String.format(SQL_DELETE_EXPRESSION_PATTERN, tableName, firstIdFieldName, firstId,
                secondIdFieldName, secondId));
    }
}
