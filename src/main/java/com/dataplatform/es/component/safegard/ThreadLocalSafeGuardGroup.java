package com.dataplatform.es.component.safegard;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by czx on 4/10/19.
 *
 * 增加线程安全的原因是，safeguard载多线程访问的时候会存在有公共变量的情况
 */
@Component
public class ThreadLocalSafeGuardGroup {

    public static ThreadLocal<SafeGuardGroup> threadLocalSafeGuardGroups = new ThreadLocal<SafeGuardGroup>();


    @Resource
    private SafeGuardGroup safeGuardGroup;


    public SafeGuardGroup getThreadSafeGuardGroup(){

        SafeGuardGroup sgroup = threadLocalSafeGuardGroups.get();

        if(sgroup==null){
            sgroup = safeGuardGroup.clone();
            threadLocalSafeGuardGroups.set(sgroup);

        }else{

            if(!sgroup.compareVersion(safeGuardGroup)){
                sgroup = safeGuardGroup.clone();
                threadLocalSafeGuardGroups.set(sgroup);
            }
        }

        return sgroup;

    }


}
