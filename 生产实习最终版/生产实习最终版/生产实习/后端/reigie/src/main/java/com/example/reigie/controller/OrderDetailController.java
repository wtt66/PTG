package com.example.reigie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reigie.common.R;
import com.example.reigie.entity.OrderDetail;
import com.example.reigie.entity.Orders;
import com.example.reigie.entity.ShoppingCart;
import com.example.reigie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单明细
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;


    @GetMapping("/list")
    public R<List<OrderDetail>> getOrderDetails(OrderDetail orderDetail){
        log.info("orderDetail:{}",orderDetail);
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,orderDetail.getOrderId());

        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        return R.success(orderDetailList);
    }

//    @GetMapping("/history/list")
//    public R<List<OrderDetail>> getOrderDetailsHistory(){
////        log.info("orderDetail:{}",orderDetail);
//        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(OrderDetail::get);
//
//        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
//        return R.success(orderDetailList);
//    }

}