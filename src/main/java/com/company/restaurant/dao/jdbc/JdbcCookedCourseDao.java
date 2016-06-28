package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.CookedCourseDao;
import com.company.restaurant.dao.CourseDao;
import com.company.restaurant.dao.EmployeeDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableWithId;
import com.company.restaurant.model.CookedCourse;
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
public class JdbcCookedCourseDao extends JdbcDaoTableWithId<CookedCourse> implements CookedCourseDao {
    private static final String COOKED_COURSE_TABLE_NAME = "cooked_course";
    private static final String COOKED_COURSE_VIEW_NAME = "v_cooked_course";
    private static final String COOKED_COURSE_ID_FIELD_NAME = "cooked_course_id";
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
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY " + COOKED_COURSE_ID_FIELD_NAME;

    private CourseDao courseDao;
    private EmployeeDao employeeDao;

    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    protected void initMetadata() {
        this.tableName = COOKED_COURSE_TABLE_NAME;
        this.viewName = COOKED_COURSE_VIEW_NAME;
        this.idFieldName = COOKED_COURSE_ID_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected CookedCourse newObject(ResultSet resultSet) throws SQLException {
        CookedCourse result = new CookedCourse();
        result.setCookedCourseId(resultSet.getInt(COOKED_COURSE_ID_FIELD_NAME));
        result.setCookDatetime(resultSet.getTimestamp(COOK_DATETIME_FIELD_NAME));
        result.setCookWeight(resultSet.getFloat(COOK_WEIGHT_FIELD_NAME));

        Course course = result.getCourse();
        course.setCourseId(resultSet.getInt(COURSE_ID_FIELD_NAME));
        course.setName(resultSet.getString(COURSE_NAME_FIELD_NAME));
        Float weight = resultSet.getFloat(COURSE_WEIGHT_FIELD_NAME);
        if (!resultSet.wasNull()) {
            course.setWeight(weight);
        }
        Float cost = resultSet.getFloat(COURSE_COST_FIELD_NAME);
        if (!resultSet.wasNull()) {
            course.setCost(cost);
        }
        course.getCourseCategory().setId(resultSet.getInt(COURSE_CATEGORY_ID_FIELD_NAME));

        Employee employee = result.getEmployee();
        employee.setEmployeeId(resultSet.getInt(EMPLOYEE_ID_FIELD_NAME));
        employee.getJobPosition().setId(resultSet.getInt(EMPLOYEE_JOB_POSITION_ID_FIELD_NAME));
        employee.setFirstName(resultSet.getString(EMPLOYEE_FIRST_NAME_FIELD_NAME));
        employee.setSecondName(resultSet.getString(EMPLOYEE_SECOND_NAME_FIELD_NAME));
        employee.setPhoneNumber(resultSet.getString(EMPLOYEE_PHONE_NUMBER_FIELD_NAME));
        Float salary = resultSet.getFloat(EMPLOYEE_SALARY_FIELD_NAME);
        if (!resultSet.wasNull()) {
            employee.setSalary(salary);
        }

        return result;
    }

    @Override
    protected Map<String, Object> objectToDBMap(CookedCourse cookedCourse) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(EMPLOYEE_ID_FIELD_NAME, cookedCourse.getEmployee().getEmployeeId());
        result.put(COURSE_ID_FIELD_NAME, cookedCourse.getCourse().getCourseId());
        result.put(COOK_DATETIME_FIELD_NAME, cookedCourse.getCookDatetime());
        result.put(WEIGHT_FIELD_NAME, cookedCourse.getCookWeight());

        return result;
    }

    @Override
    public CookedCourse addCookedCourse(Course course, Employee employee, Float weight) {
        CookedCourse cookedCourse = new CookedCourse();

        cookedCourse.getCourse().setCourseId(course.getCourseId());
        cookedCourse.getEmployee().setEmployeeId(employee.getEmployeeId());
        cookedCourse.setCookDatetime(new Timestamp(new Date().getTime()));
        cookedCourse.setCookWeight(weight);

        return addRecord(cookedCourse);
    }

    @Override
    public void delCookedCourse(CookedCourse cookedCourse) {
        delRecordById(cookedCourse.getCookedCourseId());
    }

    @Override
    public List<CookedCourse> findAllCookedCourses() {
        return findAllObjects();
    }
}
