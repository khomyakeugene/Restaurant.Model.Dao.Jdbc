package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.OrderCourseDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoJoinCourse;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Order;

import java.util.List;

/**
 * Created by Yevhen on 23.05.2016.
 */
public class JdbcOrderCourseDao extends JdbcDaoJoinCourse implements OrderCourseDao {
    private static final String ORDER_COURSE_TABLE_NAME = "order_course";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        this.tableName = ORDER_COURSE_TABLE_NAME;
        this.secondIdFieldName = JdbcOrderDao.ORDER_ID_FIELD_NAME;
        this.joinIdFieldName = JdbcOrderDao.ORDER_ID_FIELD_NAME;
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
