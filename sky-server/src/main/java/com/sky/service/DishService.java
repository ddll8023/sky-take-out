package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    void addDish(DishDTO dishdto);

    PageResult queryDishByPage(DishPageQueryDTO dishPageQueryDTO);

    DishVO queryDishById(Long id);

    List<Dish> queryDishByCategoryId(Dish dish);

    void updateDish(DishDTO dishDTO);

    void deleteDish(String ids);

    void changeStatus(Integer status, Long id);
}
