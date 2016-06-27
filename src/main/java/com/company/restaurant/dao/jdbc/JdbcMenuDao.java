package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.MenuCourseDao;
import com.company.restaurant.dao.MenuDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableSimpleDic;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Menu;

import java.util.List;
import java.util.Set;

/**
 * Created by Yevhen on 20.05.2016.
 */
public class JdbcMenuDao extends JdbcDaoTableSimpleDic<Menu> implements MenuDao {
    private static final String MENU_TABLE_NAME = "menu";
    public static final String MENU_ID_FIELD_NAME = "menu_id";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY menu_id";

    private MenuCourseDao menuCourseDao;

    public void setMenuCourseDao(MenuCourseDao menuCourseDao) {
        this.menuCourseDao = menuCourseDao;
    }

    @Override
    protected void initMetadata() {
        super.initMetadata();

        this.tableName = MENU_TABLE_NAME;
        this.idFieldName = MENU_ID_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    protected Menu newObject() {
        return new Menu();
    }

    @Override
    public Menu addMenu(String name) {
        return addRecord(name);
    }

    @Override
    public void delMenu(String name) {
        delRecordByName(name);
    }

    @Override
    public void delMenu(Menu menu) {
        delRecord(menu);
    }

    @Override
    public Menu findMenuByName(String name) {
        return findObjectByName(name);
    }

    @Override
    public Menu findMenuById(int menuId) {
        return findObjectById(menuId);
    }

    @Override
    public List<Menu> findAllMenus() {
        return findAllObjects();
    }

    @Override
    public void addCourseToMenu(Menu menu, Course course) {
        menuCourseDao.addCourseToMenu(menu, course);
    }

    @Override
    public void delCourseFromMenu(Menu menu, Course course) {
        menuCourseDao.delCourseFromMenu(menu, course);
    }

    @Override
    public Set<Course> findMenuCourses(Menu menu) {
        return menuCourseDao.findMenuCourses(menu);
    }

    @Override
    public Course findMenuCourseByCourseId(Menu menu, int courseId) {
        return menuCourseDao.findMenuCourseByCourseId(menu, courseId);
    }
}
