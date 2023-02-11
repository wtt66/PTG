package com.example.reigie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.reigie.entity.ShoppingCart;
import com.example.reigie.mapper.ShoppingCartMapper;
import com.example.reigie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
