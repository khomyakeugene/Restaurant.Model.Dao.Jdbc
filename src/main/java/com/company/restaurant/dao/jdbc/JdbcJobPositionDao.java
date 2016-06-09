package com.company.restaurant.dao.jdbc;

import com.company.restaurant.dao.JobPositionDao;
import com.company.restaurant.dao.jdbc.proto.JdbcDaoTableSimpleDic;
import com.company.restaurant.model.JobPosition;

import java.util.List;

/**
 * Created by Yevhen on 21.05.2016.
 */
public class JdbcJobPositionDao extends JdbcDaoTableSimpleDic<JobPosition> implements JobPositionDao {
    private static final String JOB_POSITION_DIC_TABLE_NAME = "job_position_dic";
    private static final String JOB_POSITION_ID_FIELD_NAME = "job_position_id";
    private static final String DEFAULT_ORDER_BY_CONDITION = "ORDER BY job_position_id";

    @Override
    protected void initMetadata() {
        super.initMetadata();

        this.tableName = JOB_POSITION_DIC_TABLE_NAME;
        this.idFieldName = JOB_POSITION_ID_FIELD_NAME;
        this.orderByCondition = DEFAULT_ORDER_BY_CONDITION;
    }

    @Override
    public JobPosition addJobPosition(String name) {
        return addRecord(name);
    }

    @Override
    public String delJobPosition(String name) {
        return delRecordByName(name);
    }

    @Override
    public JobPosition findJobPositionByName(String name) {
        return findObjectByName(name);
    }

    @Override
    public JobPosition findJobPositionById(int jobPositionId) {
        return findObjectById(jobPositionId);
    }

    @Override
    public List<JobPosition> findAllJobPositions() {
        return findAllObjects();
    }

    @Override
    protected JobPosition newObject() {
        return new JobPosition();
    }
}
