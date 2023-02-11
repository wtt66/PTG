package com.example.reigie.filter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.reigie.common.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException,CustomException, NotAuthenticationException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截请求：{}",requestURI);

        //不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/user/login",
                "/user/logout",
                "/user/sendMsg",
                "/common/**"
        };

        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.如果不需要处理，放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }


        //if(1==1) throw new CustomException("测试通用异常处理类");




        String token = request.getHeader("Token");
        if(!StringUtils.hasText(token)){
            //如果未携带Token则返回未登录结果，通过输出流响应数据
            log.info("用户未登录");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }

        try {
            TokenUser tokenUser = TokenUtils.verify(token);
        } catch(TokenExpiredException e){
            //如果Token过时则返回未登录结果，通过输出流响应数据
            log.info("用户登录过期了");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }catch (Exception e) {
            //如果Token验证失败则返回未登录结果，通过输出流响应数据
            log.info("用户未登录");
            log.info("出现了什么异常===="+e);
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }
        //用户存在token，且通过验证了，返回用户所调用后台资源
        if(true){
            TokenUser tokenUser = TokenUtils.getTokenUser(token);
            Long Id = tokenUser.getId();
            //Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(Id);
            long id = Thread.currentThread().getId();
            log.info("线程id：{}",id);
            filterChain.doFilter(request,response);
            return;
        }

//        //4.判断登录状态，已登录则放行
//        if(request.getSession().getAttribute("employee")!=null){
//            log.info("用户已登录，用户ID为{}",request.getSession().getAttribute("employee"));
//
//            Long empId = (Long)request.getSession().getAttribute("employee");
//            BaseContext.setCurrentId(empId);
////            long id = Thread.currentThread().getId();
////            log.info("线程id：{}",id);
//
//            filterChain.doFilter(request,response);
//            return;
//        }

        //5.如果未登录则返回未登录结果，通过输出流响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
