package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.OrderCourseViewDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoQuantityLinkTable;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.OrderCourseView;
import com.company.restaurant.model.OrderView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 23.05.2016.
 */
public class JdbcOrderCourseViewDao extends JdbcDaoQuantityLinkTable<OrderCourseView> implements OrderCourseViewDao {
    private static final String ORDER_COURSE_TABLE_NAME = "order_course";
    private static final String ORDER_COURSE_VIEW_NAME = "v_order_course";
    private static final String COURSE_ID_FIELD_NAME = "course_id";
    private static final String ORDER_ID_FIELD_NAME = "order_id";
    private static final String QUANTITY_FIELD_NAME = "quantity";
    private static final String COURSE_QUANTITY_FIELD_NAME = "course_quantity";
    private static final String COURSE_CATEGORY_ID_FIELD_NAME = "course_category_id";
    private static final String COURSE_NAME_FIELD_NAME = "course_name";
    private static final String COURSE_WEIGHT_FIELD_NAME = "course_weight";
    private static final String COURSE_COST_FIELD_NAME = "course_cost";
    private static final String COURSE_CATEGORY_NAME_FIELD_NAME = "course_category_name";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY order_id, course_id";

    @Override
    protected void initMetadata() {
        this.tableName = ORDER_COURSE_TABLE_NAME;
        this.viewName = ORDER_COURSE_VIEW_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
        this.firstIdFieldName = COURSE_ID_FIELD_NAME;
        this.secondIdFieldName = ORDER_ID_FIELD_NAME;
        this.thirdFieldName = QUANTITY_FIELD_NAME;
    }

    @Override
    protected Map<String, Object> objectToDBMap(OrderCourseView orderCourse) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(QUANTITY_FIELD_NAME, orderCourse.getCourseQuantity());

        return result;
    }

    @Override
    protected OrderCourseView newObject(ResultSet resultSet) throws SQLException {
        OrderCourseView result = new OrderCourseView();
        result.setCourseId(resultSet.getInt(COURSE_ID_FIELD_NAME));
        result.setOrderId(resultSet.getInt(ORDER_ID_FIELD_NAME));
        result.setCourseQuantity(resultSet.getInt(COURSE_QUANTITY_FIELD_NAME));
        result.setCourseCategoryId(resultSet.getInt(COURSE_CATEGORY_ID_FIELD_NAME));
        result.setCourseName(resultSet.getString(COURSE_NAME_FIELD_NAME));
        result.setCourseWeight(resultSet.getFloat(COURSE_WEIGHT_FIELD_NAME));
        result.setCourseCost(resultSet.getFloat(COURSE_COST_FIELD_NAME));
        result.setCourseCategoryName(resultSet.getString(COURSE_CATEGORY_NAME_FIELD_NAME));

        return result;
    }

    @Override
    public void addCourseToOrder(OrderView orderView, Course course, int quantity) {
        increaseQuantity(course.getCourseId(), orderView.getOrderId(), quantity);
    }

    @Override
    public void takeCourseFromOrder(OrderView orderView, Course course, int quantity) {
        decreaseQuantity(course.getCourseId(), orderView.getOrderId(), quantity);
    }

    @Override
    public List<OrderCourseView> findAllOrderCourses(OrderView orderView) {
        return findObjectsByFieldCondition(ORDER_ID_FIELD_NAME, orderView.getOrderId());
    }

    @Override
    public OrderCourseView findOrderCourseByCourseId(OrderView orderView, int courseId) {
        return findObjectFromViewByTwoFieldCondition(COURSE_ID_FIELD_NAME, courseId, ORDER_ID_FIELD_NAME,
                orderView.getOrderId());
    }
}
