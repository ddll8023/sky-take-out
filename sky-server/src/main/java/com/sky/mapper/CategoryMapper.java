package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQueryCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据名称查找
     * @param name
     * @return
     */
    Category findByName(String name);

    /**
     * 新增分类
     * @param newcategory
     * @return
     */
    void insert(Category newcategory);

    /**
     * 修改分类
     * @param newcategory
     * @return
     */
    void changeCategory(Category newcategory);

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    Category findById(Long id);

    /**
     * 删除分类
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 判断分类下是否有菜品
     * @param id
     * @return
     */
    List<Dish> findDishAndCategory(Long id);
}
