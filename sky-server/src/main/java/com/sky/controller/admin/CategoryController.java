package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分类分页查询")
    public Result<PageResult> pageQueryCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询:{};第{}页", categoryPageQueryDTO.getName(), categoryPageQueryDTO.getPage());
        PageResult pageResult = categoryService.pageQueryCategory(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据类型查询分类")
    public Result<List<Category>> typeQueryCategory(Integer type) {
        log.info("根据类型查询分类：{}", type);
        List<Category> Categorys = categoryService.typeQueryCategory(type);
        return Result.success(Categorys);
    }

    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping("")
    @ApiOperation(value = "新增分类")
    public Result<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类:{}", categoryDTO.getName());
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping("")
    @ApiOperation(value = "修改分类")
    public Result changeCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类:{}", categoryDTO.getName());
        categoryService.changeCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 启用、禁用分类
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启用、禁用分类")
    public Result changeStatusCategory(@PathVariable Integer status, Long id) {
        log.info("启用、禁用分类:{}", status);
        categoryService.changeStatusCategory(status, id);
        return Result.success();
    }

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping("")
    @ApiOperation(value = "根据id删除分类")
    public Result deleteCategory(Long id) {
        log.info("根据id删除分类:{}", id);
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
