package com.example.reigie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reigie.common.BaseContext;
import com.example.reigie.common.R;
import com.example.reigie.dto.OrdersDto;
import com.example.reigie.entity.*;
import com.example.reigie.service.OrderDetailService;
import com.example.reigie.service.OrderService;
import com.example.reigie.service.ShoppingCartService;
import com.example.reigie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

//    @Autowired
//    private OrderService orderService;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number,
                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime beginTime,
                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime endTime){
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime LocalTime = LocalDateTime.parse(beginTime,df);
        //构造分页构造器对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(number != null,Orders::getNumber,number);
        queryWrapper.ge(beginTime!=null, Orders::getOrderTime,beginTime);
        queryWrapper.le(endTime != null, Orders::getCheckoutTime,endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //执行分页查询
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

//
//    /**
//     * 订单状态更新
//     * @param orders
//     * @return
//     */
//    @PutMapping
//    public R<String> update(@RequestBody Orders orders){
//        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("id",orders.getId()).set("status",orders.getStatus());
//        orderService.update(null,updateWrapper);
//        return R.success("订单状态更新成功");
//    }

    @Autowired
    private OrderService orderService;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<IPage> userList(Integer page, Integer pageSize){
        IPage<Orders> pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.eq(Orders::getUserId,BaseContext.getCurrentId());


        IPage<Orders> Result = orderService.page(pageInfo,queryWrapper);

        return R.success(Result);
    }

//    /**
//     * 分页查询
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("/page")
//    public R<IPage> employeeList(Integer page,Integer pageSize){
//        IPage<Orders> pageInfo = new Page(page, pageSize);
//        IPage<Orders> Result = orderService.page(pageInfo);
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        LambdaQueryWrapper<User> lambda = queryWrapper.lambda();
////        queryWrapper.orderByDesc(Orders::getOrderTime);
//
//        List<Orders> collect = Result.getRecords().stream().peek(item -> {
//            LambdaQueryWrapper<User> eq = lambda.eq(User::getId, item.getUserId());
//            User one = userService.getOne(eq);
//            item.setUserName(item.getConsignee());
//
//        }).collect(Collectors.toList());
//
//        Result.setRecords(collect);
//
//        return R.success(Result);
//    }


    @PutMapping
    public R<String> orderStatus(@RequestBody Orders map){

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

        final LambdaQueryWrapper<Orders> lambda = queryWrapper.lambda();

        lambda.eq(Orders::getId,map.getId());

        final Orders one = orderService.getOne(queryWrapper);
        one.setStatus(map.getStatus());
        orderService.updateById(one);

        return R.success("派送成功");
    }

    @PostMapping("again")
    public R<String> again(@RequestBody Orders orders){
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();

        LambdaQueryWrapper<OrderDetail> lambda = queryWrapper.lambda();

        lambda.eq(OrderDetail::getOrderId,orders.getId());
        //获取到了上个订单的详细菜品id
        List<OrderDetail> list = orderDetailService.list(queryWrapper);
        //将上个订单的菜品添加进购物车（看看可不可以把之前对购物车直接搬过来）

        List<ShoppingCart> collect = list.stream().map(item -> {
            //这个对象是准备，然后从order——de那张表里查询菜品对数据，再次添加到购物车里要很多，是个集合，等会用map映射
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(item, shoppingCart);
            //设置用户id，指定当前是哪个用户的购物车数据
            Long currentId = BaseContext.getCurrentId();
            shoppingCart.setUserId(currentId);
            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartService.saveBatch(collect);

        return R.success("ok");
    }
    
    
    
}
