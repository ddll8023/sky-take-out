package com.sky.controller.admin;

import com.alibaba.druid.sql.visitor.functions.IfNull;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags="员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);


        EmployeeLoginVO employeeLoginVO= new EmployeeLoginVO();
        employeeLoginVO.setToken(token);
        employeeLoginVO.setId(employee.getId());
        employeeLoginVO.setName(employee.getName());
        employeeLoginVO.setUserName(employee.getUsername());

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
    * 新增员工
    * @param employeeDTO
    * */
    @PostMapping("")
    @ApiOperation(value = "新增员工")
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO.getUsername());
        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询员工")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询:{};第{}页",employeePageQueryDTO.getName(),employeePageQueryDTO.getPage());
        PageResult pageResult = employeeService.pageQueryEmployee(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用、禁用员工账号
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启用、禁用员工账号")
    public Result changeStatusEmployee(@PathVariable Integer status, Long id){
        log.info("{}员工账号:{}",status==1?"禁用":"启用",id);
        employeeService.changeStatusEmployee(status,id);
        return Result.success();
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping("")
    @ApiOperation(value = "编辑员工信息")
    public Result editEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息：{}",employeeDTO.getUsername());
        employeeService.editEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询员工")
    public Result<Employee> queryEmployee(@PathVariable Long id){
        log.info("根据id查询员工：{}",id);
        return Result.success(employeeService.queryEmployee(id));
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("/editPassword")
    @ApiOperation(value = "修改密码")
    public Result changePassword(@RequestBody PasswordEditDTO passwordEditDTO){
        log.info("修改密码:{}",passwordEditDTO.getEmpId());
        employeeService.changePassword(passwordEditDTO);
        return  Result.success();
    }
}
