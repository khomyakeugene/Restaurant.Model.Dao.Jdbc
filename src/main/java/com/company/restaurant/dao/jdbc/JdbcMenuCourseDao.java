package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.MenuCourseDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoJoinCourse;
import com.company.restaurant.model.Course;
import com.company.restaurant.model.Menu;

import java.util.List;

/**
 * Created by Yevhen on 27.06.2016.
 */
public class JdbcMenuCourseDao extends JdbcDaoJoinCourse implements MenuCourseDao {
    private static final String MENU_COURSE_TABLE_NAME = "menu_course";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        this.tableName = MENU_COURSE_TABLE_NAME;
        this.secondIdFieldName = JdbcMenuDao.MENU_ID_FIELD_NAME;
        this.joinIdFieldName = JdbcMenuDao.MENU_ID_FIELD_NAME;
    }

    @Override
    public void addCourseToMenu(Menu menu, Course course) {
        addRecord(course.getCourseId(), menu.getMenuId());
    }

    @Override
    public void delCourseFromMenu(Menu menu, Course course) {
        delRecord(course.getCourseId(), menu.getMenuId());
    }

    @Override
    public List<Course> findMenuCourses(Menu menu) {
        return findJoinEntities(menu.getMenuId());
    }

    @Override
    public Course findMenuCourseByCourseId(Menu menu, int courseId) {
        return findJoinEntity(menu.getMenuId(), courseId);
    }
}
