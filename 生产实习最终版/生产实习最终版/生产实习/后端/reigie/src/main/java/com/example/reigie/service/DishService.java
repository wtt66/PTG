package com.example.reigie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reigie.dto.DishDto;
import com.example.reigie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    //新增菜品及对应口味
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应口味信息
    public void updateWithFlavor(DishDto dishDto);

    //删除菜品同时删除关联的口味信息
    public void removeWithFlavor(List<Long> ids);
}
