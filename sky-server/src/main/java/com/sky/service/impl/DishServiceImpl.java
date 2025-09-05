package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DishAddException;
import com.sky.exception.DishNotFoundException;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;

    @Transactional
    @Override
    public void addDish(DishDTO dishdto) {
        log.info("新增菜品: {}", dishdto);
        Dish newDish = new Dish();
        BeanUtils.copyProperties(dishdto, newDish);
        newDish.setCreateTime(LocalDateTime.now());
        newDish.setUpdateTime(LocalDateTime.now());
        newDish.setCreateUser(BaseContext.getCurrentId());
        newDish.setUpdateUser(BaseContext.getCurrentId());
        try {
            dishMapper.insert(newDish);
            List<DishFlavor> dishFlavors = dishdto.getFlavors();
            Long dishId = dishMapper.queryDishByName(newDish.getName());
            if(dishFlavors != null && !dishFlavors.isEmpty()){
                for (DishFlavor dishFlavor : dishFlavors) {
                    dishFlavor.setDishId(dishId);
                }
                dishMapper.insertFlavor(dishFlavors);
            }
        } catch (Exception e) {
            log.error("新增菜品失败: {}", dishdto.getId());
            throw new DishAddException(MessageConstant.DISH_ADD_FAILED+e);
        }
        log.info("新增菜品成功: {}", dishdto.getId());
    }

    @Override
    public PageResult queryDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询: {}", dishPageQueryDTO);
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<Dish> Dishes = dishMapper.pageQuery(dishPageQueryDTO);
        long total = Dishes.getTotal();
        List<Dish> records = Dishes.getResult();
        List<DishVO> dishVOS = records.stream().map(this::convertToDishVO).collect(Collectors.toList());
        return new PageResult(total,dishVOS);
    }

    @Override
    public DishVO queryDishById(Long id) {
        log.info("根据id查询菜品: {}", id);
        Dish dish = dishMapper.queryById(id);
        if (dish == null) {
            log.error("菜品不存在: {}", id);
            throw new DishNotFoundException(MessageConstant.DISH_NOT_FOUND);
        }
        return convertToDishVO(dish);
    }

    @Override
    public List<Dish> queryDishByCategoryId(Dish dish) {
        log.info("根据分类id查询菜品: {}", dish);
        List<Dish> dishes = dishMapper.queryByCategoryId(dish);
        log.info("根据分类id查询菜品结果: {}", dishes);
        return dishes;
    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);
        Dish dish = dishMapper.queryById(dishDTO.getId());
        if(dish == null){
            log.error("菜品不存在: {}", dishDTO.getId());
            throw new DishNotFoundException(MessageConstant.DISH_NOT_FOUND);
        }
        Dish newDish = new Dish();
        BeanUtils.copyProperties(dishDTO, newDish);
        newDish.setUpdateTime(LocalDateTime.now());
        newDish.setUpdateUser(BaseContext.getCurrentId());
        dishMapper.update(newDish);

        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        dishMapper.deleteFlavor(dishDTO.getId());
        if(dishFlavors != null && !dishFlavors.isEmpty()){
            for (DishFlavor dishFlavor : dishFlavors) {
                dishFlavor.setDishId(dishDTO.getId());
            }
            dishMapper.insertFlavor(dishFlavors);
        }





        log.info("修改菜品成功: {}", dishDTO.getId());

    }

    @Override
    @Transactional
    public void deleteDish(String ids) {
        log.info("删除菜品: {}", ids);
        String[] idArray = ids.split(",");
        List<Long> dishIds = new ArrayList<>();
        for(String id:idArray){
            Long dishId = Long.valueOf(id.trim());
            dishIds.add(dishId);
        }
        dishMapper.batchDeleteDish(dishIds);
        dishMapper.batchDeleteFlavor(dishIds);
        log.info("删除菜品成功: {}", ids);
    }

    @Override
    public void changeStatus(Integer status, Long id) {
        log.info("修改菜品状态: {}, {}", status, id);
        Dish dish = dishMapper.queryById(id);
        if(dish == null){
            log.error("菜品不存在: {}", id);
            throw new DishNotFoundException(MessageConstant.DISH_NOT_FOUND);
        }
        dish.setStatus(status);
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(BaseContext.getCurrentId());
        dishMapper.update(dish);
        log.info("修改菜品状态成功: {}, {}", status, id);
    }

    private DishVO convertToDishVO(Dish dish){
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        String categoryName = dishMapper.queryCategoryNameById(dish.getCategoryId());
        dishVO.setCategoryName(categoryName);
        List<DishFlavor> dishFlavors = dishMapper.queryFlavorByDishId(dish.getId());
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }
}
