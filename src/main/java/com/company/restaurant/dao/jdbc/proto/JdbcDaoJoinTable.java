package com.company.restaurant.dao.jdbc.proto;

import com.company.restaurant.model.proto.JoinObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 23.06.2016.
 */
public abstract class JdbcDaoJoinTable<T> extends JdbcDaoTable<T> {
    private static final String SQL_INSERT_EXPRESSION_PATTERN = "INSERT INTO \"%s\" (%s, %s) VALUES(%d, %d)";
    private static final String SQL_DELETE_EXPRESSION_PATTERN = "DELETE FROM \"%s\" WHERE (%s = %d) AND (%s = %d)";
    private static final String SQL_SELECT_JOIN_EXPRESSION_PATTERN =
            "SELECT \"%s\".* FROM \"%s\", \"%s\" WHERE (\"%s\".%s = %d) AND (\"%s\".%s = \"%s\".%s)";
    private static final String SQL_AND_ENTITY_ID_CONDITION = " AND (\"%s\".%s = %d)";

    protected String firstIdFieldName;
    protected String secondIdFieldName;
    protected String joinIdFieldName;
    protected String entityTableName;
    protected String entityIdFieldName;

    protected Map<String, Object> objectToDBMap(T object) {
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

    public T getObjectFromTableByTwoFieldCondition(int firstId, int secondId) {
        return findObjectFromTableByTwoFieldCondition(firstIdFieldName, firstId,
                secondIdFieldName, secondId);
    }

    public T getObjectFromViewByTwoFieldCondition(int firstId, int secondId) {
        return findObjectFromViewByTwoFieldCondition(firstIdFieldName, firstId,
                secondIdFieldName, secondId);
    }

    private String findJoinEntitiesQuery(int joinId) {
        return String.format(SQL_SELECT_JOIN_EXPRESSION_PATTERN, entityTableName,
                entityTableName, tableName, tableName, joinIdFieldName, joinId,
                tableName, entityIdFieldName, entityTableName, entityIdFieldName);
    }

    public List<T> findJoinEntities(int joinId) {
        return createObjectListFromQuery(findJoinEntitiesQuery(joinId));
    }

    public T findJoinEntity(int joinId, int entityId) {
        return getFirstFromList(createObjectListFromQuery(findJoinEntitiesQuery(joinId) + " " +
                String.format(SQL_AND_ENTITY_ID_CONDITION, entityTableName, entityIdFieldName, entityId)));
    }
}
