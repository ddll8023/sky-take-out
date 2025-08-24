package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
//    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * @param employee
     */
    void insert(Employee employee);

    /**
     *
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQueryEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据id查找员工
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 修改员工信息
     * @param employee
     */
    void changeEmployee(Employee employee);
}
