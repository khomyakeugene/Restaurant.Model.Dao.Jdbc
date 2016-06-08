package com.company.restaurant.dao.jdbc;

import com.company.restaurant.model.Portion;
import com.company.restaurant.dao.PortionDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 24.05.2016.
 */
public class JdbcPortionDao extends JdbcDaoTableWithId<Portion> implements PortionDao {
    private static final String PORTION_TABLE_NAME = "portion_dic";
    private static final String PORTION_ID_FIELD_NAME = "portion_id";
    private static final String PORTION_TYPE_ID_FIELD_NAME = "portion_type_id";
    private static final String MEASURING_TYPE_FIELD_NAME = "measuring_type_id";
    private static final String AMOUNT_FIELD_NAME = "amount";
    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY portion_id";

    @Override
    protected void initMetadata() {
        this.tableName = PORTION_TABLE_NAME;
        this.idFieldName = PORTION_ID_FIELD_NAME;
        this.nameFieldName = DESCRIPTION_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected Portion newObject(ResultSet resultSet) throws SQLException {
        Portion result = new Portion();
        result.setPortionId(resultSet.getInt(PORTION_ID_FIELD_NAME));
        result.setPortionTypeId(resultSet.getInt(PORTION_TYPE_ID_FIELD_NAME));
        result.setMeasuringTypeId(resultSet.getInt(MEASURING_TYPE_FIELD_NAME));
        result.setAmount(resultSet.getFloat(AMOUNT_FIELD_NAME));
        result.setDescription(resultSet.getString(DESCRIPTION_FIELD_NAME));

        return result;
    }

    @Override
    protected Map<String, Object> objectToDBMap(Portion portion) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(PORTION_TYPE_ID_FIELD_NAME, portion.getPortionTypeId());
        result.put(MEASURING_TYPE_FIELD_NAME, portion.getMeasuringTypeId());
        result.put(AMOUNT_FIELD_NAME, portion.getAmount());
        result.put(DESCRIPTION_FIELD_NAME, portion.getDescription());

        return result;
    }

    @Override
    protected void setId(int id, Portion portion) {
        portion.setPortionId(id);
    }

    @Override
    public Portion addPortion(Portion portion) {
        return addRecord(portion);
    }

    @Override
    public String delPortion(Portion portion) {
        return delRecord(portion);
    }

    @Override
    public List<Portion> findAllPortions() {
        return findAllObjects();
    }

    @Override
    public Portion findPortionById(int portionId) {
        return findObjectById(portionId);
    }
}
