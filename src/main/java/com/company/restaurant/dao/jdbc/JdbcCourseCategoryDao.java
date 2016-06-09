package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.CourseCategoryDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableSimpleDic;
import com.company.restaurant.model.CourseCategory;

import java.util.List;

/**
 * Created by Yevhen on 21.05.2016.
 */
public class JdbcCourseCategoryDao extends JdbcDaoTableSimpleDic<CourseCategory> implements CourseCategoryDao {
    private static final String COURSE_CATEGORY_DIC_TABLE_NAME = "course_category_dic";
    private static final String COURSE_CATEGORY_ID_FIELD_NAME = "course_category_id";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        this.tableName = COURSE_CATEGORY_DIC_TABLE_NAME;
        this.idFieldName = COURSE_CATEGORY_ID_FIELD_NAME;
    }

    @Override
    protected CourseCategory newObject() {
        return new CourseCategory();
    }

    @Override
    public CourseCategory addCourseCategory(String name) {
        return addRecord(name);
    }

    @Override
    public String delCourseCategory(String name) {
        return delRecordByName(name);
    }

    @Override
    public CourseCategory findCourseCategoryByName(String name) {
        return findObjectByName(name);
    }

    @Override
    public CourseCategory findCourseCategoryById(int CourseCategoryId) {
        return findObjectById(CourseCategoryId);
    }

    @Override
    public List<CourseCategory> findAllCourseCategories() {
        return findAllObjects();
    }
}
