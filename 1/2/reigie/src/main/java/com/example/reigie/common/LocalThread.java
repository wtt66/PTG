package com.example.reigie.common;


import com.example.reigie.entity.Employee;
import com.example.reigie.entity.User;

public class LocalThread {
    private static final ThreadLocal<Employee> EmployeeThread = new ThreadLocal<>();
    private static final ThreadLocal<User> UserThread = new ThreadLocal<>();


    public static void setEmployeeThread(Long id){
        Employee employee = new Employee();
        employee.setId(id);
        EmployeeThread.set(employee);
    }

    public static Employee getEmployeeThread(){
        return EmployeeThread.get();
    }

    public static void RemoveEmployeeThreadInfo(){
        EmployeeThread.remove();
    }


    public static void setUserThread(Long id){
        User user = new User();
        user.setId(id);
        UserThread.set(user);
    }

    public static User getUserThread(){
        return UserThread.get();
    }

    public static void RemoveUserThreadInfo(){
        UserThread.remove();
    }

}
