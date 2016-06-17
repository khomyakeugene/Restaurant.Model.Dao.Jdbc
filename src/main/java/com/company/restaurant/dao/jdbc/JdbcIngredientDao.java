package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.IngredientDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableSimpleDic;
import com.company.restaurant.model.Ingredient;

import java.util.List;

/**
 * Created by Yevhen on 24.05.2016.
 */
public class JdbcIngredientDao extends JdbcDaoTableSimpleDic<Ingredient> implements IngredientDao {
    private static final String INGREDIENT_DIC_TABLE_NAME = "ingredient";
    private static final String INGREDIENT_ID_FIELD_NAME = "ingredient_id";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY ingredient_id";

    @Override
    public Ingredient addIngredient(String name) {
        return addRecord(name);
    }

    @Override
    public void delIngredient(String name) {
        delRecordByName(name);
    }

    @Override
    public List<Ingredient> findAllIngredients() {
        return findAllObjects();
    }

    @Override
    public Ingredient findIngredientById(int ingredientId) {
        return findObjectById(ingredientId);
    }

    @Override
    protected Ingredient newObject() {
        return new Ingredient();
    }

    @Override
    protected void initMetadata() {
        super.initMetadata();

        this.tableName = INGREDIENT_DIC_TABLE_NAME;
        this.idFieldName = INGREDIENT_ID_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }
}
