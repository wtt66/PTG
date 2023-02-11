package com.example.reigie.dto;

import com.example.reigie.entity.Setmeal;
import com.example.reigie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
