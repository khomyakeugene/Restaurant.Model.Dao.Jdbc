package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.MenuCoursesViewDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoLinkTable;
import com.company.restaurant.dao.proto.SqlExpressions;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Menu;
import com.company.restaurant.model.MenuCourseView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yevhen on 21.05.2016.
 */
public class JdbcMenuCoursesViewDao extends JdbcDaoLinkTable<MenuCourseView> implements MenuCoursesViewDao {
    private static final String MENU_COURSES_LIST_TABLE_NAME = "menu_courses_list";
    private static final String MENU_COURSES_LIST_VIEW_NAME = "v_menu_courses_list";
    private static final String MENU_ID_FIELD_NAME = "menu_id";
    private static final String COURSE_ID_FIELD_NAME = "course_id";
    private static final String COURSE_NUMBER_FIELD_NAME = "course_number";
    private static final String COURSE_CATEGORY_ID_FIELD_NAME = "course_category_id";
    private static final String COURSE_NAME_FIELD_NAME = "course_name";
    private static final String COURSE_WEIGHT_FIELD_NAME = "course_weight";
    private static final String COURSE_COST_FIELD_NAME = "course_cost";
    private static final String COURSE_CATEGORY_NAME_FIELD_NAME = "course_category_name";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY menu_id, course_id";

    @Override
    protected void initMetadata() {
        this.tableName = MENU_COURSES_LIST_TABLE_NAME;
        this.viewName = MENU_COURSES_LIST_VIEW_NAME;
        this.firstIdFieldName = MENU_ID_FIELD_NAME;
        this.secondIdFieldName = COURSE_ID_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    private int getMaxCourseNumberInMenu(Menu menu) {
        String selectResult = getOneFieldByFieldCondition(
                SqlExpressions.maxFieldValueExpression(COURSE_NUMBER_FIELD_NAME),
                firstIdFieldName, menu.getId());

        return (selectResult == null) || selectResult.equals("") ? 0 : Integer.parseInt(selectResult);
    }

    @Override
    protected Map<String, Object> objectToDBMap(MenuCourseView menuCourseView) {
        HashMap<String, Object> result = new HashMap<>();

        result.put(COURSE_NUMBER_FIELD_NAME, menuCourseView.getCourseNumber());

        return result;
    }

    @Override
    protected MenuCourseView newObject(ResultSet resultSet) throws SQLException {
        MenuCourseView result = new MenuCourseView();
        result.setMenuId(resultSet.getInt(MENU_ID_FIELD_NAME));
        result.setCourseId(resultSet.getInt(COURSE_ID_FIELD_NAME));
        result.setCourseNumber(resultSet.getInt(COURSE_NUMBER_FIELD_NAME));
        result.setCourseCategoryId(resultSet.getInt(COURSE_CATEGORY_ID_FIELD_NAME));
        result.setCourseName(resultSet.getString(COURSE_NAME_FIELD_NAME));
        result.setCourseWeight(resultSet.getFloat(COURSE_WEIGHT_FIELD_NAME));
        result.setCourseCost(resultSet.getFloat(COURSE_COST_FIELD_NAME));
        result.setCourseCategoryName(resultSet.getString(COURSE_CATEGORY_NAME_FIELD_NAME));

        return result;
    }

    @Override
    public void addCourseToMenu(Menu menu, Course course) {
        int firstId = menu.getId();
        int secondId = course.getCourseId();

        MenuCourseView menuCourseList = new MenuCourseView();
        menuCourseList.setFirstId(firstId);
        menuCourseList.setSecondId(secondId);
        menuCourseList.setCourseNumber(getMaxCourseNumberInMenu(menu) + 1);

        addRecord(firstId, secondId, menuCourseList);
    }

    @Override
    public void delCourseFromMenu(Menu menu, Course course) {
        delRecord(menu.getId(), course.getCourseId());
    }

    @Override
    public List<MenuCourseView> findMenuCourses(Menu menu) {
        return findObjectsByFieldCondition(MENU_ID_FIELD_NAME, menu.getId());
    }

    @Override
    public MenuCourseView findMenuCourseByCourseId(Menu menu, int courseId) {
        return findObjectFromViewByTwoFieldCondition(MENU_ID_FIELD_NAME, menu.getId(), COURSE_ID_FIELD_NAME, courseId);
    }
}
