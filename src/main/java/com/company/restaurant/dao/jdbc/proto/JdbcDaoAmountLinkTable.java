package com.company.restaurant.dao.jdbc.proto;

import com.company.restaurant.model.LinkObject;

/**
 * Created by Yevhen on 24.05.2016.
 */
public abstract class JdbcDaoAmountLinkTable<T extends LinkObject> extends JdbcDaoLinkTable<T> {
    protected Float selectCurrentAmount(int firstId, int secondId) {
        String stringResult = getOneFieldByTwoFieldCondition(thirdFieldName, firstId, secondId);

        return (stringResult == null) ? null : Float.parseFloat(stringResult);
    }

    protected void increaseAmount(int firstId, int secondId, float increasePortion) {
        Float currentAmount = selectCurrentAmount(firstId, secondId);
        if (currentAmount == null) {
            if (increasePortion > 0) {
                addRecord(firstId, secondId, String.valueOf(increasePortion));
            }
        } else {
            currentAmount += increasePortion;
            if (currentAmount > 0) {
                updRecord(firstId, secondId, currentAmount);
            } else {
                delRecord(firstId, secondId);
            }
        }
    }

    protected void decreaseAmount(int firstId, int secondId, float decreasePortion) {
        increaseAmount(firstId, secondId, -decreasePortion);
    }
}
