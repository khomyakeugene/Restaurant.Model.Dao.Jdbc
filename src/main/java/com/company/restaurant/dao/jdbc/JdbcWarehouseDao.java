package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.WarehouseDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoAmountLinkTable;
import com.company.restaurant.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Yevhen on 24.05.2016.
 */
public class JdbcWarehouseDao extends JdbcDaoAmountLinkTable<Warehouse> implements WarehouseDao {
    private static final String WAREHOUSE_TABLE_NAME = "warehouse";
    private static final String WAREHOUSE_VIEW_NAME = "v_warehouse";
    private static final String INGREDIENT_ID_FIELD_NAME = "ingredient_id";
    private static final String PORTION_ID_FIELD_NAME = "portion_id";
    private static final String AMOUNT_FIELD_NAME = "amount";
    private static final String INGREDIENT_NAME_FIELD_NAME = "ingredient_name";
    private static final String PORTION_TYPE_ID_FIELD_NAME = "portion_type_id";
    private static final String MEASURING_TYPE_ID_FIELD_NAME = "measuring_type_id";
    private static final String PORTION_AMOUNT_FIELD_NAME = "portion_amount";
    private static final String PORTION_DESCRIPTION_FIELD_NAME = "portion_description";
    private static final String PORTION_TYPE_NAME_FIELD_NAME = "portion_type_name";
    private static final String MEASURING_TYPE_CODE_FIELD_NAME = "measuring_type_code";
    private static final String MEASURING_TYPE_NAME_FIELD_NAME = "measuring_type_name";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY ingredient_name";
    private static final String SQL_SELECT_ELAPSING_WAREHOUSE_INGREDIENTS =
            "SELECT * FROM v_warehouse WHERE (amount < %f)";

    @Override
    protected void initMetadata() {
        this.tableName = WAREHOUSE_TABLE_NAME;
        this.viewName = WAREHOUSE_VIEW_NAME;
        this.firstIdFieldName = INGREDIENT_ID_FIELD_NAME;
        this.secondIdFieldName = PORTION_ID_FIELD_NAME;
        this.thirdFieldName = AMOUNT_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected Map<String, Object> objectToDBMap(Warehouse object) {
        return null;
    }

    @Override
    protected Warehouse newObject(ResultSet resultSet) throws SQLException {
        Warehouse result = new Warehouse();
        result.setAmount(resultSet.getFloat(AMOUNT_FIELD_NAME));

        Ingredient ingredient = result.getIngredient();
        ingredient.setIngredientId(resultSet.getInt(INGREDIENT_ID_FIELD_NAME));
        ingredient.setName(resultSet.getString(INGREDIENT_NAME_FIELD_NAME));

        Portion portion = result.getPortion();
        portion.setPortionId(resultSet.getInt(PORTION_ID_FIELD_NAME));
        portion.setAmount(resultSet.getFloat(PORTION_AMOUNT_FIELD_NAME));
        portion.setDescription(resultSet.getString(PORTION_DESCRIPTION_FIELD_NAME));

        PortionType portionType = portion.getPortionType();
        portionType.setPortionTypeId(resultSet.getInt(PORTION_TYPE_ID_FIELD_NAME));
        portionType.setName(resultSet.getString(PORTION_TYPE_NAME_FIELD_NAME));

        MeasuringType measuringType = portion.getMeasuringType();
        measuringType.setMeasuringTypeId(resultSet.getInt(MEASURING_TYPE_ID_FIELD_NAME));
        measuringType.setMeasuringTypeCode(resultSet.getString(MEASURING_TYPE_CODE_FIELD_NAME));
        measuringType.setName(resultSet.getString(MEASURING_TYPE_NAME_FIELD_NAME));

        return result;
    }

    @Override
    public void addIngredientToWarehouse(Ingredient ingredient, Portion portion, float amount) {
        if (amount > 0.0) {
            increaseAmount(ingredient.getIngredientId(), portion.getPortionId(), amount);
        }
    }

    @Override
    public void takeIngredientFromWarehouse(Ingredient ingredient, Portion portion, float amount) {
        if (amount > 0.0) {
            decreaseAmount(ingredient.getIngredientId(), portion.getPortionId(), amount);
        }
    }

    @Override
    public Warehouse findIngredientInWarehouse(Ingredient ingredient, Portion portion) {
        return getObjectFromViewByTwoFieldCondition(ingredient.getIngredientId(),
                portion.getPortionId());
    }

    @Override
    public List<Warehouse> findIngredientInWarehouseByName(String name) {
        return findObjectsByFieldCondition(INGREDIENT_NAME_FIELD_NAME, name);
    }

    @Override
    public List<Warehouse> findIngredientInWarehouseById(int ingredientId) {
        return findObjectsByFieldCondition(INGREDIENT_ID_FIELD_NAME, ingredientId);
    }

    @Override
    public List<Warehouse> findAllWarehouseIngredients() {
        return findAllObjects();
    }

    @Override
    public List<Warehouse> findAllElapsingWarehouseIngredients(float limit) {
        return createObjectListFromQuery(String.format(Locale.US,
                SQL_SELECT_ELAPSING_WAREHOUSE_INGREDIENTS, limit));
    }
}
