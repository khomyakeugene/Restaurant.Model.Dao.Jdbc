package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.StateDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableSimpleTypeDic;
import com.company.restaurant.model.State;

/**
 * Created by Yevhen on 25.06.2016.
 */
public class JdbcStateDao extends JdbcDaoTableSimpleTypeDic<State> implements StateDao {
    private static final String STATE_DIC_TABLE_NAME = "state_dic";
    private static final String STATE_TYPE_FIELD_NAME = "state_type";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        this.tableName = STATE_DIC_TABLE_NAME ;
        this.typeFieldName = STATE_TYPE_FIELD_NAME;
    }


    @Override
    public State findStateByType(String type) {
        return findByType(type);
    }

    @Override
    protected State newObject() {
        return new State();
    }
}
