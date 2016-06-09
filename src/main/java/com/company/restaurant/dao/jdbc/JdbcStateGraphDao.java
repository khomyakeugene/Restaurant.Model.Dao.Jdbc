package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.StateGraphDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTable;
import com.company.restaurant.model.StateGraph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 22.05.2016.
 */
public class JdbcStateGraphDao extends JdbcDaoTable<StateGraph> implements StateGraphDao {
    private static final String STATE_GRAPH_TABLE_NAME = "state_graph";
    private static final String INIT_STATE_TYPE_FIELD_NAME = "init_state_type";
    private static final String FINITE_STATE_TYPE_FIELD_NAME = "finite_state_type";
    private static final String ACTION_TYPE_FIELD_NAME = "action_type";
    private static final String ENTITY_NAME_FIELD_NAME = "entity_name";
    private static final String COMMENT_FIELD_NAME = "comment";
    private static final String DEFAULT_ORDER_BY_CONDITION = "";

    @Override
    protected void initMetadata() {
        this.tableName = STATE_GRAPH_TABLE_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected Map<String, Object> objectToDBMap(StateGraph object) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(INIT_STATE_TYPE_FIELD_NAME, object.getInitStateType());
        result.put(FINITE_STATE_TYPE_FIELD_NAME, object.getFiniteStateType());
        result.put(ACTION_TYPE_FIELD_NAME, object.getActionType());
        result.put(ENTITY_NAME_FIELD_NAME, object.getEntityName());
        result.put(COMMENT_FIELD_NAME, object.getComment());

        return result;
    }

    @Override
    protected StateGraph newObject(ResultSet resultSet) throws SQLException {
        StateGraph result = new StateGraph();
        result.setInitStateType(resultSet.getString(INIT_STATE_TYPE_FIELD_NAME));
        result.setFiniteStateType(resultSet.getString(FINITE_STATE_TYPE_FIELD_NAME));
        result.setActionType(resultSet.getString(ACTION_TYPE_FIELD_NAME));
        result.setEntityName(resultSet.getString(ENTITY_NAME_FIELD_NAME));
        result.setComment(resultSet.getString(COMMENT_FIELD_NAME));

        return result;
    }
    @Override
    public List<StateGraph> findEntityStateGraph(String entityName) {
        return findObjectsByFieldCondition(ENTITY_NAME_FIELD_NAME, entityName);
    }
}
