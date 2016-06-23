package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.OrderCourseViewDao;
import com.company.restaurant.dao.OrderViewDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableWithId;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Order;
import com.company.restaurant.model.OrderView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 22.05.2016.
 */
public class JdbcOrderViewDao extends JdbcDaoTableWithId<OrderView> implements OrderViewDao {
    private static final String ORDER_TABLE_NAME = "order";
    private static final String ORDER_VIEW_NAME = "v_order";
    private static final String ORDER_ID_FIELD_NAME = "order_id";
    private static final String TABLE_ID_FIELD_NAME = "table_id";
    private static final String STATE_TYPE_FIELD_NAME = "state_type";
    private static final String EMPLOYEE_ID_FIELD_NAME = "employee_id";
    private static final String ORDER_NUMBER_FIELD_NAME = "order_number";
    private static final String ORDER_DATETIME_FIELD_NAME = "order_datetime";
    private static final String STATE_TYPE_NAME_FIELD_NAME = "state_type_name";
    private static final String EMPLOYEE_JOB_POSITION_ID_FIELD_NAME = "employee_job_position_id";
    private static final String EMPLOYEE_FIRST_NAME_FIELD_NAME = "employee_first_name";
    private static final String EMPLOYEE_SECOND_NAME_FIELD_NAME = "employee_second_name";
    private static final String EMPLOYEE_PHONE_NUMBER_FIELD_NAME = "employee_phone_number";
    private static final String EMPLOYEE_SALARY_FIELD_NAME = "employee_salary";
    private static final String EMPLOYEE_JOB_POSITION_NAME_FIELD_NAME = "employee_job_position_name";
    private static final String TABLE_NUMBER_FIELD_NAME = "table_number";
    private static final String TABLE_DESCRIPTION_FIELD_NAME = "table_description";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY order_id";

    private OrderCourseViewDao orderCourseViewDao;

    public void setOrderCourseViewDao(OrderCourseViewDao orderCourseViewDao) {
        this.orderCourseViewDao = orderCourseViewDao;
    }

    @Override
    protected void initMetadata() {
        this.tableName = ORDER_TABLE_NAME;
        this.viewName = ORDER_VIEW_NAME;
        this.idFieldName = ORDER_ID_FIELD_NAME;
        this.nameFieldName = ORDER_NUMBER_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected void setId(int id, OrderView orderView) {
        orderView.setOrderId(id);
    }

    @Override
    protected Map<String, Object> objectToDBMap(OrderView orderView) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(TABLE_ID_FIELD_NAME, orderView.getTableId());
        result.put(STATE_TYPE_FIELD_NAME, orderView.getStateType());
        result.put(EMPLOYEE_ID_FIELD_NAME, orderView.getEmployeeId());
        result.put(ORDER_NUMBER_FIELD_NAME, orderView.getOrderNumber());
        result.put(ORDER_DATETIME_FIELD_NAME, orderView.getOrderDatetime());

        return result;
    }

    @Override
    protected OrderView newObject(ResultSet resultSet) throws SQLException {
        OrderView result = new OrderView();
        result.setOrderId(resultSet.getInt(ORDER_ID_FIELD_NAME));
        result.setTableId(resultSet.getInt(TABLE_ID_FIELD_NAME));
        result.setStateType(resultSet.getString(STATE_TYPE_FIELD_NAME));
        result.setEmployeeId(resultSet.getInt(EMPLOYEE_ID_FIELD_NAME));
        result.setOrderNumber(resultSet.getString(ORDER_NUMBER_FIELD_NAME));
        result.setOrderDatetime(resultSet.getTimestamp(ORDER_DATETIME_FIELD_NAME));
        result.setStateTypeName(resultSet.getString(STATE_TYPE_NAME_FIELD_NAME));
        result.setEmployeeJobPositionId(resultSet.getInt(EMPLOYEE_JOB_POSITION_ID_FIELD_NAME));
        result.setEmployeeFirstName(resultSet.getString(EMPLOYEE_FIRST_NAME_FIELD_NAME));
        result.setEmployeeSecondName(resultSet.getString(EMPLOYEE_SECOND_NAME_FIELD_NAME));
        result.setEmployeePhoneNumber(resultSet.getString(EMPLOYEE_PHONE_NUMBER_FIELD_NAME));
        Float salary = resultSet.getFloat(EMPLOYEE_SALARY_FIELD_NAME);
        if (!resultSet.wasNull()) {
            result.setEmployeeSalary(salary);
        }
        result.setEmployeeJobPositionName(resultSet.getString(EMPLOYEE_JOB_POSITION_NAME_FIELD_NAME));
        result.setTableNumber(resultSet.getInt(TABLE_NUMBER_FIELD_NAME));
        result.setTableDescription(resultSet.getString(TABLE_DESCRIPTION_FIELD_NAME));


        return result;
    }

    @Override
    public String orderEntityName() {
        return tableName;
    }


    @Override
    public OrderView addOrder(OrderView orderView) {
        return addRecord(orderView);
    }

    @Override
    public void delOrder(OrderView orderView) {
        delRecord(orderView);
    }

    @Override
    public OrderView findOrderById(int id) {
        return findObjectById(id);
    }

    @Override
    public List<OrderView> findOrderByNumber(String orderNumber) {
        return findObjectsByFieldCondition(ORDER_NUMBER_FIELD_NAME, orderNumber);
    }

    @Override
    public List<OrderView> findAllOrders() {
        return findAllObjects();
    }

    @Override
    public List<OrderView> findAllOrders(String stateType) {
        return findObjectsByFieldCondition(STATE_TYPE_FIELD_NAME, stateType) ;
    }

    @Override
    public OrderView updOrderState(OrderView orderView, String stateType) {
        return updRecord(orderView, STATE_TYPE_FIELD_NAME, stateType);
    }

    @Override
    public void addCourseToOrder(Order order, Course course) {

    }

    @Override
    public void takeCourseFromOrder(Order order, Course course) {

    }

    @Override
    public List<Course> findAllOrderCourses(Order order) {
        return null;
    }

    @Override
    public Course findOrderCourseByCourseId(Order order, int courseId) {
        return null;
    }
}
