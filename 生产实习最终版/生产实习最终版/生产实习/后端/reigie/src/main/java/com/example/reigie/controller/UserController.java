package com.example.reigie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reigie.common.*;
import com.example.reigie.entity.User;
import com.example.reigie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user){
        //获取手机号
        log.info("user={}",user);
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = String.valueOf((System.currentTimeMillis()%10000000000L)/1000000+Integer.valueOf(phone.substring(0,4)));
            //String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            //需要将生成的验证码保存到Session

            //HttpSession session = request.getSession();
            //session.setAttribute(phone,code);
            //log.info("获取验证码时的sessionid:{}",session.getId());

            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }



    /**
     * 移动端用户登录
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();

        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        //HttpSession session = request.getSession();
        //log.info("登录时的sessionid:{}",session.getId());



        String tCode = String.valueOf((System.currentTimeMillis()%10000000000L)/1000000+Integer.valueOf(phone.substring(0,4)));
        log.info("验证码收的为：{}",tCode);


        if(tCode.equals(code)){
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            TokenUser tokenUser = null;

            if(user == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
                LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(User::getPhone,phone);
                user = userService.getOne(lambdaQueryWrapper);
                tokenUser = new TokenUser(user.getId(), "test");
            }else{
                tokenUser = new TokenUser(user.getId(), "test");
                LocalThread.setUserThread(user.getId());
            }


            String token = TokenUtils.loginSign(tokenUser);

            log.info("token:{}",token);
            log.info("user:{}",user);


            return R.success(user).add("Token",token);

            //session.setAttribute("user",user.getId());
            //return R.success(user);
        }
        return R.error("验证码错误");
    }

    @PostMapping("loginout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前登录员工的id
        log.info("{}已退出登录", request.getSession().getAttribute("user"));
        request.getSession().invalidate();
        return R.success("退出成功");
    }

}
