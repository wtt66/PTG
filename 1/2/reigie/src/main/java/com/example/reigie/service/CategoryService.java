package com.example.reigie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reigie.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
