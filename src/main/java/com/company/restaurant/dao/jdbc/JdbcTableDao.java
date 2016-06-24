package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.TableDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableWithId;
import com.company.restaurant.model.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 22.05.2016.
 */
public class JdbcTableDao extends JdbcDaoTableWithId<Table> implements TableDao {
    private static final String TABLE_TABLE_NAME = "table";
    private static final String TABLE_ID_FIELD_NAME = "table_id";
    private static final String NUMBER_FIELD_NAME = "number";
    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY table_id";

    @Override
    protected void initMetadata() {
        this.tableName = TABLE_TABLE_NAME;
        this.idFieldName = TABLE_ID_FIELD_NAME;
        this.nameFieldName = NUMBER_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    public Table addTable(Table table) {
        return addRecord(table);
    }

    @Override
    public void delTable(Table table) {
        delRecord(table);
    }

    @Override
    public Table findTableById(int tableId) {
        return findObjectById(tableId);
    }

    @Override
    public Table findTableByNumber(int number) {
        return findObjectByName(Integer.toString(number));
    }

    @Override
    public List<Table> findAllTables() {
        return findAllObjects();
    }

    @Override
    protected Map<String, Object> objectToDBMap(Table table) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(NUMBER_FIELD_NAME, table.getNumber());
        result.put(DESCRIPTION_FIELD_NAME, table.getDescription());

        return result;
    }

    @Override
    protected Table newObject(ResultSet resultSet) throws SQLException {
        Table result = new Table();
        result.setTableId(resultSet.getInt(TABLE_ID_FIELD_NAME));
        result.setNumber(resultSet.getInt(NUMBER_FIELD_NAME));
        result.setDescription(resultSet.getString(DESCRIPTION_FIELD_NAME));

        return result;
    }
}
