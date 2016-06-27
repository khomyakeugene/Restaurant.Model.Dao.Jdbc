package com.company.restaurant.dao.jdbc.proto;

import com.company.restaurant.dao.jdbc.JdbcCourseDao;
import com.company.restaurant.model.Course;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Yevhen on 27.06.2016.
 */
public class JdbcDaoJoinCourse extends JdbcDaoJoinTable<Course>  {
    @Override
    protected void initMetadata() {
        this.orderByCondition = JdbcCourseDao.DEFAULT_ORDER_BY_CONDITION;
        this.firstIdFieldName = JdbcCourseDao.COURSE_ID_FIELD_NAME;
        this.entityTableName = JdbcCourseDao.COURSE_VIEW_NAME;
        this.entityIdFieldName = JdbcCourseDao.COURSE_ID_FIELD_NAME;
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
}
