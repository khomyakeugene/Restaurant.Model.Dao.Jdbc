package com.company.restaurant.controllers;

import org.junit.BeforeClass;

/**
 * Created by Yevhen on 20.05.2016.
 */
public class RestaurantModelDaoJdbcTest extends RestaurantModelDaoTest {
    private final static String APPLICATION_CONTEXT_NAME = "restaurant-jdbc-context.xml";

    @BeforeClass
    public static void setUpClass() throws Exception {
        initDataSource(APPLICATION_CONTEXT_NAME);

        initEnvironment();
    }
}