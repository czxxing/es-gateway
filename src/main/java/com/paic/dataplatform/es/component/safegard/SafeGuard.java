package com.paic.dataplatform.es.component.safegard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by czx on 4/9/19.
 */

/**
 * 类有线程安全的问题
 */
public abstract class SafeGuard {




    abstract boolean check(HttpServletRequest request,HttpServletResponse response);

    abstract String checkMsg(HttpServletRequest request,HttpServletResponse response);


    public boolean isPass(HttpServletRequest request,HttpServletResponse response){

        boolean isPass = check(request,response);

        return isPass;
    }

}
