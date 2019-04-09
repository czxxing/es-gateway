package com.paic.dataplatform.es.component.safegard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by czx on 4/9/19.
 */
public class EmptySafeGuard extends SafeGuard {


    @Override
    boolean check(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    String checkMsg(HttpServletRequest request, HttpServletResponse response) {
        return "is a empty,can use for test,and it use for deal with empty list";
    }
}
