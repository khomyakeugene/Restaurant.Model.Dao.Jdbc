package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.OrderCourseDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoJoinTable;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Yevhen on 23.05.2016.
 */
public class JdbcOrderCourseDao extends JdbcDaoJoinTable<Course> implements OrderCourseDao {
    private static final String ORDER_COURSE_TABLE_NAME = "order_course";

    @Override
    protected void initMetadata() {
        this.tableName = ORDER_COURSE_TABLE_NAME;
        this.orderByCondition = JdbcCourseDao.DEFAULT_ORDER_BY_CONDITION;
        this.firstIdFieldName = JdbcCourseDao.COURSE_ID_FIELD_NAME;
        this.secondIdFieldName = JdbcOrderDao.ORDER_ID_FIELD_NAME;
        this.entityTableName = JdbcCourseDao.COURSE_VIEW_NAME;
        this.entityIdFieldName = JdbcCourseDao.COURSE_ID_FIELD_NAME;
        this.joinIdFieldName = JdbcOrderDao.ORDER_ID_FIELD_NAME;
    }

    @Override
    protected Course newObject(ResultSet resultSet) throws SQLException {
        Course result = new Course();
        result.setCourseId(resultSet.getInt(JdbcCourseDao.COURSE_ID_FIELD_NAME));
        result.setName(resultSet.getString(JdbcCourseDao.NAME_FIELD_NAME));
        Float weight = resultSet.getFloat(JdbcCourseDao.WEIGHT_FIELD_NAME);
        if (!resultSet.wasNull()) {
            result.setWeight(weight);
        }
        Float cost = resultSet.getFloat(JdbcCourseDao.COST_FIELD_NAME);
        if (!resultSet.wasNull()) {
            result.setCost(cost);
        }
        result.getCourseCategory().setId(resultSet.getInt(JdbcCourseDao.COURSE_CATEGORY_ID_FIELD_NAME));
        result.getCourseCategory().setName(resultSet.getString(JdbcCourseDao.COURSE_CATEGORY_NAME_FIELD_NAME));

        return result;
    }

    @Override
    public void addCourseToOrder(Order order, Course course) {
        addRecord(course.getCourseId(), order.getOrderId());
    }

    @Override
    public void takeCourseFromOrder(Order order, Course course) {
        delRecord(course.getCourseId(), order.getOrderId());
    }

    @Override
    public List<Course> findAllOrderCourses(Order order) {
        return findJoinEntities(order.getOrderId());
    }

    @Override
    public Course findOrderCourseByCourseId(Order order, int courseId) {
        return findJoinEntity(order.getOrderId(), courseId);
    }
}
