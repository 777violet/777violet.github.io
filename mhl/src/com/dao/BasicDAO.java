package com.dao;

import com.utils.JDBCUtilsByDruid;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BasicDAO<T> {//泛型制定具体的类型

    private QueryRunner queryRunner=new QueryRunner();

    //通用的dml方法
    public int update(String sql,Object... parameters){
        Connection connection=null;
        try {
            connection= JDBCUtilsByDruid.getConnection();
            int update = queryRunner.update(connection, sql, parameters);
            return update;

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

    //返回多个对象（即查询结果为多行），针对任意表
    public List<T> queryMulti(String sql,Class<T> clazz,Object... parameters){
        Connection connection=null;
        try {
            connection=JDBCUtilsByDruid.getConnection();
            List<T> query = queryRunner.query(connection, sql, new BeanListHandler<T>(clazz), parameters);
            return query;

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

    //查询单行的通用方法。
    public T querySingle(String sql,Class<T> clazz,Object... parameters){
        Connection connection=null;
        try {
            connection=JDBCUtilsByDruid.getConnection();
            return queryRunner.query(connection,sql,new BeanHandler<T>(clazz),parameters);

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

    //查询单行单列的通用方法，即返回值为单值的方法
    public Object queryScalar(String sql,Object... parameters){
        Connection connection=null;
        try {
            connection=JDBCUtilsByDruid.getConnection();
            return queryRunner.query(connection,sql,new ScalarHandler(),parameters);

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

}

