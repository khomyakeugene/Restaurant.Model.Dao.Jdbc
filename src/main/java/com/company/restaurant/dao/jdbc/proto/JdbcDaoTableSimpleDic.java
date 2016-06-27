package com.company.restaurant.dao.jdbc.proto;

import com.company.restaurant.model.proto.SimpleDic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yevhen on 21.05.2016.
 */
public abstract class JdbcDaoTableSimpleDic<T extends SimpleDic> extends JdbcDaoTableWithId<T> {
    private static final String NAME_FIELD_NAME = "name";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY name";

    @Override
    protected void initMetadata() {
        this.nameFieldName = NAME_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    protected abstract T newObject();

    @Override
    protected T newObject(ResultSet resultSet) throws SQLException {
        T object = newObject();
        object.setId(resultSet.getInt(idFieldName));
        object.setName(resultSet.getString(nameFieldName));

        return object;
    }

    public T addRecord(String name) {
        T object = newObject();
        object.setId(0);
        object.setName(name);

        return addRecord(object);
    }

    @Override
    protected Map<String, Object> objectToDBMap(T object) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(nameFieldName, object.getName());

        return result;
    }
}
