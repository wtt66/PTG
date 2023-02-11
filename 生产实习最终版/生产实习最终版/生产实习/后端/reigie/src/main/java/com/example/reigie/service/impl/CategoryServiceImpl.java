package com.example.reigie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reigie.common.CustomException;
import com.example.reigie.entity.Category;
import com.example.reigie.entity.Dish;
import com.example.reigie.entity.Setmeal;
import com.example.reigie.mapper.CategoryMapper;
import com.example.reigie.service.CategoryService;
import com.example.reigie.service.DishService;
import com.example.reigie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除前进行判断
     * @param id
     * @return
     */
    @Override
    public void remove(Long id) {
        //添加查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询分类是否关联菜品
        if (count1 > 0) {
            //已关联菜品，抛异常
            throw new CustomException("当前分类下存在菜品，不能删除");
        }
        //查询分类是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //已关联套餐，抛异常
            throw new CustomException("当前分类下存在套餐，不能删除");
        }
        //正常删除
        super.removeById(id);
    }

}
