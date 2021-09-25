package com.service;

import com.dao.EmployeeDAO;
import com.daomain.Employee;

/**
 * 该类完成对employee表的操作（用过调用EmployeeDAO对象完成）
 */
public class EmployeeService {

    private EmployeeDAO employeeDAO=new EmployeeDAO();

    //方法，根据empId和empPwd返回一个Employee对象,若查询不到，返回null
    public Employee getEmployeeByIdAndPwd(String empId,String empPwd){

        Employee employee = employeeDAO.querySingle(
                "select * from employee where empId=? and empPwd=md5(?)",
                 Employee.class, empId, empPwd);
        return employee;
    }

}
