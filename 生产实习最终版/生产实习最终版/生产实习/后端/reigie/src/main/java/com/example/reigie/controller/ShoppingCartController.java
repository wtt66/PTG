package com.example.reigie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.reigie.common.BaseContext;
//import com.example.reigie.common.LocalThread;
import com.example.reigie.common.R;
import com.example.reigie.entity.ShoppingCart;
import com.example.reigie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据:{}", shoppingCart);

        //设置用户id，指定当前是哪个用户的购物车数据
//        Long currentId = LocalThread.getUserThread().getId();
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车中
        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null) {
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车...");

        //if (LocalThread.getUserThread() != null) {
        if (BaseContext.getCurrentId() != null) {
            LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

            //queryWrapper.eq(ShoppingCart::getUserId, LocalThread.getUserThread().getId());
            queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
            queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

            List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
            return R.success(list);
        }
        return R.error("查询成功");
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        //SQL:delete from shopping_cart where user_id = ?

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //queryWrapper.eq(ShoppingCart::getUserId, LocalThread.getUserThread().getId());
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }

    /**
     * 增加或减少购物车中菜品的数量
     *
     * @param map
     * @return
     */
    @PostMapping("/sub")
    public R<String> subShopping(@RequestBody ShoppingCart map) {
        if (map.getDishId() != null) {
            QueryWrapper<ShoppingCart> DishQueryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<ShoppingCart> lambda = DishQueryWrapper.lambda();
            //lambda.eq(ShoppingCart::getUserId, LocalThread.getUserThread().getId());
            lambda.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
            lambda.eq(ShoppingCart::getDishId, map.getDishId());
            ShoppingCart dish = shoppingCartService.getOne(DishQueryWrapper);
            log.info("需要修改数量的的菜品为{}", dish.getName());
            if (dish.getNumber() > 1) {
                dish.setNumber(dish.getNumber() - 1);
                shoppingCartService.updateById(dish);
            } else {
                shoppingCartService.removeById(dish.getId());
            }
        }

        if (map.getSetmealId() != null) {
            QueryWrapper<ShoppingCart> SetmealQueryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<ShoppingCart> lambda = SetmealQueryWrapper.lambda();
            //lambda.eq(ShoppingCart::getUserId, LocalThread.getUserThread().getId());
            lambda.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
            lambda.eq(ShoppingCart::getSetmealId, map.getSetmealId());
            ShoppingCart setmeal = shoppingCartService.getOne(SetmealQueryWrapper);
            log.info("需要修改数量的的套餐为{}", setmeal.getName());
            if (setmeal.getNumber() > 1) {
                setmeal.setNumber(setmeal.getNumber() - 1);
                shoppingCartService.updateById(setmeal);
            } else {
                shoppingCartService.removeById(setmeal.getId());
            }
        }
        return R.success("OK");
    }

}