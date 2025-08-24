package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.exception.UsernameExistsException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        log.info("加密密码：{}", password);
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    /**
    * 新增员工
     * @param employeeDTO
    * */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {

        if(employeeMapper.getByUsername(employeeDTO.getUsername())!=null){
            throw new UsernameExistsException(MessageConstant.USERNAME_EXIST_ERROR);
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long currentId = BaseContext.getCurrentId();
        employee.setCreateUser(currentId);
        employee.setUpdateUser(currentId);

        employeeMapper.insert(employee);
    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQueryEmployee(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> employees = employeeMapper.pageQueryEmployee(employeePageQueryDTO);
        long total = employees.getTotal();
        List<Employee> records = employees.getResult();
        return new PageResult(total,records);
    }

    /**
     * 启用、禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void changeStatusEmployee(Integer status, Long id) {
        Employee employee = employeeMapper.getById(id);
        if(employee==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        employee.setStatus(status==1?1:0);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.changeEmployee(employee);

    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    @Override
    public void editEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.getById(employeeDTO.getId());
        if(employee==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        Employee newEmployee = new Employee();
        BeanUtils.copyProperties(employeeDTO,newEmployee);
        newEmployee.setUpdateUser(BaseContext.getCurrentId());
        newEmployee.setUpdateTime(LocalDateTime.now());
        employeeMapper.changeEmployee(newEmployee);
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Override
    public Employee queryEmployee(Long id) {
        return employeeMapper.getById(id);
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     * @return
     */
    @Override
    public void changePassword(PasswordEditDTO passwordEditDTO) {
        Employee employee = employeeMapper.getById(passwordEditDTO.getEmpId());
        if(employee==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        String newPassword = passwordEditDTO.getNewPassword();
        String oldPassword = passwordEditDTO.getOldPassword();
        if(newPassword.equals(oldPassword)){
            //TODO 当前使用密码错误代替密码重复
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        Employee newEmployee = new Employee();
        newEmployee.setId(employee.getId());
        newEmployee.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        newEmployee.setUpdateUser(BaseContext.getCurrentId());
        newEmployee.setUpdateTime(LocalDateTime.now());
        employeeMapper.changeEmployee(newEmployee);
    }

}
