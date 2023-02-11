package com.example.reigie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reigie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
