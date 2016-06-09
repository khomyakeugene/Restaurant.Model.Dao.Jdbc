package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.EmployeeDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableWithId;
import com.company.restaurant.model.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 19.05.2016.
 */
public class JdbcEmployeeDao extends JdbcDaoTableWithId<Employee> implements EmployeeDao {
    private static final String EMPLOYEE_TABLE_NAME = "employee";
    private static final String EMPLOYEE_VIEW_NAME = "v_employee";
    private static final String EMPLOYEE_ID_FIELD_NAME = "employee_id";
    private static final String JOB_POSITION_ID_FIELD_NAME = "job_position_id";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";
    private static final String PHONE_NUMBER_FIELD_NAME = "phone_number";
    private static final String SALARY_FIELD_NAME = "salary";
    private static final String JOB_POSITION_NAME_FIELD_NAME = "job_position_name";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY employee_id";

    @Override
    protected void initMetadata() {
        this.tableName = EMPLOYEE_TABLE_NAME;
        this.viewName = EMPLOYEE_VIEW_NAME;
        this.idFieldName = EMPLOYEE_ID_FIELD_NAME;
        this.nameFieldName = SECOND_NAME_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected Employee newObject(ResultSet resultSet) throws SQLException {
        Employee result = new Employee();
        result.setEmployeeId(resultSet.getInt(EMPLOYEE_ID_FIELD_NAME));
        result.setJobPositionId(resultSet.getInt(JOB_POSITION_ID_FIELD_NAME));
        result.setFirstName(resultSet.getString(FIRST_NAME_FIELD_NAME));
        result.setSecondName(resultSet.getString(SECOND_NAME_FIELD_NAME));
        result.setPhoneNumber(resultSet.getString(PHONE_NUMBER_FIELD_NAME));
        Float salary = resultSet.getFloat(SALARY_FIELD_NAME);
        if (!resultSet.wasNull()) {
            result.setSalary(salary);
        }
        result.setJobPositionName(resultSet.getString(JOB_POSITION_NAME_FIELD_NAME));

        return result;
    }

    @Override
    public List<Employee> findEmployeeByFirstName(String firstName) {
        return findObjectsByFieldCondition(FIRST_NAME_FIELD_NAME, firstName);
    }

    @Override
    public List<Employee> findEmployeeBySecondName(String secondName) {
        return findObjectsByFieldCondition(SECOND_NAME_FIELD_NAME, secondName);
    }

    @Override
    public List<Employee> findEmployeeByFirstAndSecondName(String firstName, String secondName) {
        List<Employee> result;

        if (firstName != null && !firstName.isEmpty() && secondName != null && !secondName.isEmpty()) {
            result = findObjectsFromViewByTwoFieldCondition(FIRST_NAME_FIELD_NAME, firstName,
                    SECOND_NAME_FIELD_NAME, secondName);
        } else if (firstName != null && !firstName.isEmpty()) {
            result = findEmployeeByFirstName(firstName);
        } else if (secondName != null && !secondName.isEmpty()) {
            result = findEmployeeBySecondName(secondName);
        } else {
            result = findAllEmployees();
        }

        return result;
    }

    @Override
    public List<Employee> findAllEmployees() {
        return findAllObjects();
    }

    @Override
    protected void setId(int id, Employee employee) {
        employee.setEmployeeId(id);
    }

    @Override
    protected Map<String, Object> objectToDBMap(Employee employee) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(JOB_POSITION_ID_FIELD_NAME, employee.getJobPositionId());
        result.put(FIRST_NAME_FIELD_NAME, employee.getFirstName());
        result.put(SECOND_NAME_FIELD_NAME, employee.getSecondName());
        result.put(PHONE_NUMBER_FIELD_NAME, employee.getPhoneNumber());
        result.put(SALARY_FIELD_NAME, employee.getSalary());

        return result;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return addRecord(employee);
    }

    @Override
    public String delEmployee(Employee employee) {
        return delRecord(employee);
    }

    @Override
    public String delEmployee(int employeeId) {
        return delRecordById(employeeId);
    }

    @Override
    public Employee findEmployeeById(int id) {
        return findObjectById(id);
    }
}
