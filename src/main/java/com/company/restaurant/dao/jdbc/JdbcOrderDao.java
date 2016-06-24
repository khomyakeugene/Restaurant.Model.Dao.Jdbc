package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.*;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableWithId;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 24.06.2016.
 */
public class JdbcOrderDao extends JdbcDaoTableWithId<Order> implements OrderDao {
    private static final String ORDER_TABLE_NAME = "order";
    public static final String ORDER_ID_FIELD_NAME = "order_id";
    private static final String TABLE_ID_FIELD_NAME = "table_id";
    private static final String STATE_TYPE_FIELD_NAME = "state_type";
    private static final String EMPLOYEE_ID_FIELD_NAME = "employee_id";
    private static final String ORDER_NUMBER_FIELD_NAME = "order_number";
    private static final String ORDER_DATETIME_FIELD_NAME = "order_datetime";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY order_id";

    private OrderCourseDao orderCourseDao;
    private TableDao tableDao;
    private EmployeeDao employeeDao;
    private StateDao stateDao;

    public void setOrderCourseDao(OrderCourseDao orderCourseDao) {
        this.orderCourseDao = orderCourseDao;
    }

    public void setTableDao(TableDao tableDao) {
        this.tableDao = tableDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public void setStateDao(StateDao stateDao) {
        this.stateDao = stateDao;
    }

    @Override
    protected void initMetadata() {
        this.tableName = ORDER_TABLE_NAME;
        this.idFieldName = ORDER_ID_FIELD_NAME;
        this.nameFieldName = ORDER_NUMBER_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected Map<String, Object> objectToDBMap(Order order) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(TABLE_ID_FIELD_NAME, order.getTable().getId());
        result.put(STATE_TYPE_FIELD_NAME, order.getStateType());
        result.put(EMPLOYEE_ID_FIELD_NAME, order.getWaiter().getEmployeeId());
        result.put(ORDER_NUMBER_FIELD_NAME, order.getOrderNumber());
        result.put(ORDER_DATETIME_FIELD_NAME, order.getOrderDatetime());

        return result;
    }

    @Override
    protected Order newObject(ResultSet resultSet) throws SQLException {
        Order result = new Order();
        result.setOrderId(resultSet.getInt(ORDER_ID_FIELD_NAME));
        result.setOrderNumber(resultSet.getString(ORDER_NUMBER_FIELD_NAME));
        result.setOrderDatetime(resultSet.getTimestamp(ORDER_DATETIME_FIELD_NAME));

        result.setTable(tableDao.findTableById(resultSet.getInt(TABLE_ID_FIELD_NAME)));
        result.setWaiter(employeeDao.findEmployeeById(resultSet.getInt(EMPLOYEE_ID_FIELD_NAME)));
        result.setState(stateDao.findStateByType(resultSet.getString(STATE_TYPE_FIELD_NAME)));

        return result;
    }

    @Override
    public String orderEntityName() {
        return tableName;
    }


    @Override
    public Order addOrder(Order order) {
        return addRecord(order);
    }

    @Override
    public void delOrder(Order order) {
        delRecord(order);
    }

    @Override
    public Order findOrderById(int id) {
        return findObjectById(id);
    }

    @Override
    public List<Order> findOrderByNumber(String orderNumber) {
        return findObjectsByFieldCondition(ORDER_NUMBER_FIELD_NAME, orderNumber);
    }

    @Override
    public List<Order> findAllOrders() {
        return findAllObjects();
    }

    @Override
    public List<Order> findAllOrders(String stateType) {
        return findObjectsByFieldCondition(STATE_TYPE_FIELD_NAME, stateType) ;
    }

    @Override
    public Order updOrderState(Order order, String stateType) {
        return updRecord(order, STATE_TYPE_FIELD_NAME, stateType);
    }

    @Override
    public void addCourseToOrder(Order order, Course course) {
        orderCourseDao.addCourseToOrder(order, course);

    }

    @Override
    public void takeCourseFromOrder(Order order, Course course) {
        orderCourseDao.takeCourseFromOrder(order, course);
    }

    @Override
    public List<Course> findAllOrderCourses(Order order) {
        return orderCourseDao.findAllOrderCourses(order);
    }

    @Override
    public Course findOrderCourseByCourseId(Order order, int courseId) {
        return orderCourseDao.findOrderCourseByCourseId(order, courseId);
    }

}
