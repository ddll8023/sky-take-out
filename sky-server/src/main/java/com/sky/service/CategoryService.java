package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQueryCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> typeQueryCategory(Integer type);

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    void changeCategory(CategoryDTO categoryDTO);

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     */
    void changeStatusCategory(Integer status, Long id);

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteCategory(Long id);
}
