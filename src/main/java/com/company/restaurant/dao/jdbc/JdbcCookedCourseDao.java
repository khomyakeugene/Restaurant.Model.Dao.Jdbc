package com.company.restaurant.dao.jdbc;

import com.company.restaurant.model.CookedCourse;
import com.company.restaurant.dao.CookedCourseDao;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 23.05.2016.
 */
public class JdbcCookedCourseDao extends JdbcDaoLinkTable<CookedCourse> implements CookedCourseDao {
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

    @Override
    protected void initMetadata() {
        this.tableName = COOKED_COURSE_TABLE_NAME;
        this.viewName = COOKED_COURSE_VIEW_NAME;
        this.firstIdFieldName = COURSE_ID_FIELD_NAME;
        this.secondIdFieldName = EMPLOYEE_ID_FIELD_NAME;
        this.thirdFieldName = WEIGHT_FIELD_NAME;
    }

    @Override
    protected CookedCourse newObject(ResultSet resultSet) throws SQLException {
        CookedCourse result = new CookedCourse();
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
    protected Map<String, Object> objectToDBMap(CookedCourse cookedCourse) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(WEIGHT_FIELD_NAME, cookedCourse.getCookWeight());

        return result;
    }

    @Override
    public void addCookedCourse(Course course, Employee employee, Float weight) {
        addRecord(course.getCourseId(), employee.getEmployeeId(), (weight == null) ? null : weight.toString());
    }

    @Override
    public String delCookedCourse(CookedCourse cookedCourse) {
        // 23.05.2016, 22:15 - TO DO!!!
        return null;
    }

    @Override
    public List<CookedCourse> findAllCookedCourses() {
        return findAllObjects();
    }
}
