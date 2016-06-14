package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.CookedCourseViewDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTable;
import com.company.restaurant.model.CookedCourseView;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 23.05.2016.
 */
public class JdbcCookedCourseViewDao extends JdbcDaoTable<CookedCourseView> implements CookedCourseViewDao {
    private static final String COOKED_COURSE_TABLE_NAME = "cooked_course";
    private static final String COOKED_COURSE_VIEW_NAME = "v_cooked_course";
    private static final String COURSE_ID_FIELD_NAME = "course_id";
    private static final String EMPLOYEE_ID_FIELD_NAME = "employee_id";
    private static final String WEIGHT_FIELD_NAME = "weight";
    private static final String COOK_WEIGHT_FIELD_NAME = "cook_weight";
    private static final String COOK_DATETIME_FIELD_NAME = "cook_datetime";
    private static final String COURSE_CATEGORY_ID_FIELD_NAME = "course_category_id";
    private static final String COURSE_NAME_FIELD_NAME = "course_name";
    private static final String COURSE_WEIGHT_FIELD_NAME = "course_weight";
    private static final String COURSE_COST_FIELD_NAME = "course_cost";
    private static final String EMPLOYEE_JOB_POSITION_ID_FIELD_NAME = "employee_job_position_id";
    private static final String EMPLOYEE_FIRST_NAME_FIELD_NAME = "employee_first_name";
    private static final String EMPLOYEE_SECOND_NAME_FIELD_NAME = "employee_second_name";
    private static final String EMPLOYEE_PHONE_NUMBER_FIELD_NAME = "employee_phone_number";
    private static final String EMPLOYEE_SALARY_FIELD_NAME = "employee_salary";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY " + COOK_DATETIME_FIELD_NAME;

    @Override
    protected void initMetadata() {
        this.tableName = COOKED_COURSE_TABLE_NAME;
        this.viewName = COOKED_COURSE_VIEW_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected CookedCourseView newObject(ResultSet resultSet) throws SQLException {
        CookedCourseView result = new CookedCourseView();
        result.setCourseId(resultSet.getInt(COURSE_ID_FIELD_NAME));
        result.setEmployeeId(resultSet.getInt(EMPLOYEE_ID_FIELD_NAME));
        result.setCookDatetime(resultSet.getTimestamp(COOK_DATETIME_FIELD_NAME));
        result.setCookWeight(resultSet.getFloat(COOK_WEIGHT_FIELD_NAME));
        result.setCourseCategoryId(resultSet.getInt(COURSE_CATEGORY_ID_FIELD_NAME));
        result.setCourseName(resultSet.getString(COURSE_NAME_FIELD_NAME));
        result.setCourseWeight(resultSet.getFloat(COURSE_WEIGHT_FIELD_NAME));
        result.setCourseCost(resultSet.getFloat(COURSE_COST_FIELD_NAME));
        result.setEmployeeJobPositionId(resultSet.getInt(EMPLOYEE_JOB_POSITION_ID_FIELD_NAME));
        result.setEmployeeFirstName(resultSet.getString(EMPLOYEE_FIRST_NAME_FIELD_NAME));
        result.setEmployeeSecondName(resultSet.getString(EMPLOYEE_SECOND_NAME_FIELD_NAME));
        result.setEmployeePhoneNumber(resultSet.getString(EMPLOYEE_PHONE_NUMBER_FIELD_NAME));
        result.setEmployeeSalary(resultSet.getFloat(EMPLOYEE_SALARY_FIELD_NAME));

        return result;
    }

    @Override
    protected Map<String, Object> objectToDBMap(CookedCourseView cookedCourseView) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(EMPLOYEE_ID_FIELD_NAME, cookedCourseView.getEmployeeId());
        result.put(COURSE_ID_FIELD_NAME, cookedCourseView.getCourseId());
        result.put(COOK_DATETIME_FIELD_NAME, cookedCourseView.getCookDatetime());
        result.put(WEIGHT_FIELD_NAME, cookedCourseView.getCookWeight());

        return result;
    }

    @Override
    public CookedCourseView addCookedCourse(Course course, Employee employee, Float weight) {
        CookedCourseView cookedCourseView = new CookedCourseView();

        cookedCourseView.setCourseId(course.getCourseId());
        cookedCourseView.setEmployeeId(employee.getEmployeeId());
        cookedCourseView.setCookDatetime(new Timestamp(new Date().getTime()));
        cookedCourseView.setCookWeight(weight);

        return addRecord(cookedCourseView);
    }

    @Override
    public String delCookedCourse(CookedCourseView cookedCourse) {
        // 23.05.2016, 22:15 - TO DO!!!
        return null;
    }

    @Override
    public List<CookedCourseView> findAllCookedCourses() {
        return findAllObjects();
    }
}
