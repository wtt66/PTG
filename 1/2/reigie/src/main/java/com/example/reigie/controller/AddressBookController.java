package com.example.reigie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.reigie.common.BaseContext;
import com.example.reigie.common.R;
import com.example.reigie.entity.AddressBook;
import com.example.reigie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();

        if (BaseContext.getCurrentId() != null) {

            final LambdaQueryWrapper<AddressBook> lambda = queryWrapper.lambda();

            lambda.eq(AddressBook::getUserId, BaseContext.getCurrentId());
            List<AddressBook> list = addressBookService.list(queryWrapper);
            return R.success(list);
        }
        return R.error("查询成功");
    }


    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     *
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);

        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();

        lambdaUpdateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lambdaUpdateWrapper.eq(AddressBook::getId,addressBook.getId());
        lambdaUpdateWrapper.set(AddressBook::getIsDefault,addressBook.getIsDefault());
        addressBookService.update(lambdaUpdateWrapper);

        return R.success(addressBook);
    }

    @PutMapping()
    public R<String> update(@RequestBody AddressBook addressBook){
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBook.setUserId(BaseContext.getCurrentId());
        lambdaUpdateWrapper.eq(AddressBook::getId, addressBook.getId());

        lambdaUpdateWrapper.set(AddressBook::getPhone,addressBook.getPhone());
        lambdaUpdateWrapper.set(AddressBook::getLabel,addressBook.getLabel());
        lambdaUpdateWrapper.set(AddressBook::getConsignee,addressBook.getConsignee());
        lambdaUpdateWrapper.set(AddressBook::getSex,addressBook.getSex());
        lambdaUpdateWrapper.set(AddressBook::getDetail,addressBook.getDetail());
        addressBookService.update(lambdaUpdateWrapper);

        return R.success("成功修改地址");

    }
    @DeleteMapping()
    public R<String> delete(@RequestParam Long addressBookId){
        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AddressBook::getId, addressBookId);
        //lambdaUpdateWrapper.eq(AddressBook::getIsDeleted,Integer.valueOf(1));
        addressBookService.remove(lambdaUpdateWrapper);
        return R.success("删除成功");
    }


    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }


}
