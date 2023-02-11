package com.example.reigie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reigie.dto.SetmealDto;
import com.example.reigie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //新增套餐同时保存套餐和菜品的关联关系
    public void saveWithDish(SetmealDto setmealDto);

    //删除套餐同时删除关联的菜品
    public void removeWithDish(List<Long> ids);

    //更新套餐信息和对应菜品信息
    void updateWithDish(SetmealDto setmealDto);

    public SetmealDto getByIdWithDish(Long id);
}
