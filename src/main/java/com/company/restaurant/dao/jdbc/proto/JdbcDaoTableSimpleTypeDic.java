package com.company.restaurant.dao.jdbc.proto;

import com.company.restaurant.model.SimpleTypeDic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yevhen on 25.06.2016.
 */
public abstract class JdbcDaoTableSimpleTypeDic<T extends SimpleTypeDic> extends JdbcDaoTable<T>  {
    private static final String NAME_FIELD_NAME = "name";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY name";

    protected String typeFieldName;
    protected String nameFieldName;

    @Override
    protected void initMetadata() {
        this.nameFieldName = NAME_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    protected abstract T newObject();

    @Override
    protected T newObject(ResultSet resultSet) throws SQLException {
        T object = newObject();
        object.setType(resultSet.getString(typeFieldName));
        object.setName(resultSet.getString(nameFieldName));

        return object;
    }

    @Override
    protected Map<String, Object> objectToDBMap(T object) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(typeFieldName, object.getType());
        result.put(nameFieldName, object.getName());

        return result;
    }

    protected T findByType(String type) {
        return findObjectByFieldCondition(typeFieldName, type);

    }
}
