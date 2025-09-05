package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "菜品管理")
public class DishController {

    private final DishService dishService;

    @PostMapping("")
    @ApiOperation(value = "新增菜品")
    public Result<Void> addDish(@RequestBody DishDTO dishdto) {
        dishService.addDish(dishdto);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    public Result<PageResult> queryDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.queryDishByPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品")
    public Result<DishVO> queryDishById(@PathVariable Long id){
        return Result.success(dishService.queryDishById(id));
    }

    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<Dish>> queryDishByCategoryId(Dish dish){
        return Result.success(dishService.queryDishByCategoryId(dish));
    }

    @PutMapping("")
    @ApiOperation(value = "修改菜品")
    public Result<Void> updateDish(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @DeleteMapping("")
    @ApiOperation(value = "删除菜品")
    public Result<Void> deleteDish(@RequestParam String ids) {
        dishService.deleteDish(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation(value = "批量起售/停售菜品")
    public Result<Void> updateStatus(@PathVariable Integer status, @RequestParam Long id){
        dishService.changeStatus(status,id);
        return Result.success();
    }

}
