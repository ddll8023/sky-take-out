package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper {

    void insert(Dish dish);

    Page<Dish> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    Dish queryById(Long id);

    List<Dish> queryByCategoryId(Dish dish);

    String queryCategoryNameById(Long categoryId);

    void update(Dish newDish);

    void insertFlavor(List<DishFlavor> dishFlavors);

    void deleteFlavor(Long id);

    List<DishFlavor> queryFlavorByDishId(Long dishId);

    Long queryDishByName(String name);

    void batchDeleteDish(List<Long> dishIds);

    void batchDeleteFlavor(List<Long> dishIds);
}
