package com.company.restaurant.dao.jdbc;

import com.company.restaurant.model.IntegerLinkObject;

/**
 * Created by Yevhen on 23.05.2016.
 */
public abstract class JdbcDaoQuantityLinkTable<T extends IntegerLinkObject> extends JdbcDaoAmountLinkTable<T> {
    private Integer selectCurrentQuantity(int firstId, int secondId) {
        return selectCurrentAmount(firstId, secondId).intValue();
    }

    public void increaseQuantity(int firstId, int secondId, int increasePortion) {
        increaseAmount(firstId, secondId, increasePortion);
    }

    public void decreaseQuantity(int firstId, int secondId, int decreasePortion) {
        decreaseAmount(firstId, secondId, decreasePortion);
    }
}
