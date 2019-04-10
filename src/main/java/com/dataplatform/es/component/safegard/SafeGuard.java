package com.dataplatform.es.component.safegard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by czx on 4/9/19.
 */

/**
 * 类有线程安全的问题
 * 有公共变量需要实现clone接口
 */
public abstract class SafeGuard {




    abstract public  boolean check(HttpServletRequest request,HttpServletResponse response);

    abstract public String checkMsg(HttpServletRequest request,HttpServletResponse response);


    /**
     * 有公共变量一定要重载这个函数，实现线程下对象
     * @return
     */
    abstract public Object clone();




    public boolean isPass(HttpServletRequest request,HttpServletResponse response){

        boolean isPass = check(request,response);

        return isPass;
    }



}
