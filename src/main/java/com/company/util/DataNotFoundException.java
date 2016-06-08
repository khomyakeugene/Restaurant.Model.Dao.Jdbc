package com.company.util;

import java.sql.SQLException;

/**
 * Created by Yevhen on 19.05.2016.
 */
public class DataNotFoundException extends SQLException {
    public DataNotFoundException(String reason) {
        super(reason);
    }
}
