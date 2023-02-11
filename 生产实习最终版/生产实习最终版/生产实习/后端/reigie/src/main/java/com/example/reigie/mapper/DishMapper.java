package com.example.reigie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reigie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
