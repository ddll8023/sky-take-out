package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.exception.CategoryExistException;
import com.sky.exception.CategoryNotFoundException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQueryCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> categories = categoryMapper.pageQueryCategory(categoryPageQueryDTO);
        long total = categories.getTotal();
        List<Category> records = categories.getResult();
        return new PageResult(total,records);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> typeQueryCategory(Integer type) {
        CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
        categoryPageQueryDTO.setType(type);
        return categoryMapper.pageQueryCategory(categoryPageQueryDTO);
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.findByName(categoryDTO.getName());
        if(category!=null){
            throw new CategoryExistException(MessageConstant.CATEGORY_NAME_EXIST_ERROR);
        }
        Category newcategory = new Category();
        BeanUtils.copyProperties(categoryDTO,newcategory);
        newcategory.setStatus(StatusConstant.ENABLE);
        newcategory.setCreateTime(LocalDateTime.now());
        newcategory.setUpdateTime(LocalDateTime.now());
        newcategory.setCreateUser(BaseContext.getCurrentId());
        newcategory.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insert(newcategory);
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @Override
    public void changeCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.findByName(categoryDTO.getName());
        if(category!=null){
            throw new CategoryExistException(MessageConstant.CATEGORY_NAME_EXIST_ERROR);
        }
        Category newCategory = new Category();
        BeanUtils.copyProperties(categoryDTO,newCategory);
        newCategory.setUpdateTime(LocalDateTime.now());
        newCategory.setCreateUser(BaseContext.getCurrentId());
        categoryMapper.changeCategory(newCategory);
    }

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     */
    @Override
    public void changeStatusCategory(Integer status, Long id) {
        Category category = categoryMapper.findById(id);
        if (category != null) {
            throw  new CategoryNotFoundException(MessageConstant.CATEGORY_NOT_FOUND);
        }
        Category newCategory = new Category();
        newCategory.setId(id);
        newCategory.setStatus(status);
        newCategory.setUpdateTime(LocalDateTime.now());
        newCategory.setCreateUser(BaseContext.getCurrentId());
        categoryMapper.changeCategory(newCategory);
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryMapper.findById(id);
        if (category == null) {
            throw  new CategoryNotFoundException(MessageConstant.CATEGORY_NOT_FOUND);
        }
        List<Dish> dishes = categoryMapper.findDishAndCategory(id);
        if(dishes!=null&&dishes.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        else{
            categoryMapper.deleteCategory(id);
        }

    }


}
