package com.example.reigie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reigie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}