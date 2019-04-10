package com.dataplatform.es.component.safegard;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by czx on 4/9/19.
 */
@Component
public class SafeGuardGroup {

   /**
    * 需要保证单调递增
    */
   private  AtomicInteger version = new AtomicInteger(0);

   private static CopyOnWriteArrayList<SafeGuard> safeGuards = new CopyOnWriteArrayList<SafeGuard>();


   public SafeGuardGroup(){

      //at least one
      safeGuards.add(new EmptySafeGuard());
   }

   public void setVersion(int version){

      this.version = new AtomicInteger(version);
   }


   public boolean compareVersion(SafeGuardGroup g){
      return this.version.get() == g.version.get();
   }



   public boolean isAllPass(HttpServletRequest request, HttpServletResponse response) throws SafeGuardException {

      boolean isPass = true;
      for( SafeGuard guard : safeGuards){
         isPass = guard.isPass(request,response);
         if(!isPass){
            String msg = guard.checkMsg(request,response);
            throw new SafeGuardException(msg);
         }
      }
      return isPass;
   }




   public void addSafeGuard(SafeGuard guard ){

      safeGuards.add(guard);

   }

   public void addSafeGuardS(List<SafeGuard> guards){

      safeGuards.addAll(guards);

   }


   public SafeGuardGroup clone(){

      SafeGuardGroup sgroup = new SafeGuardGroup();

      synchronized(this) {
         this.safeGuards.forEach(guard -> {
            sgroup.addSafeGuard((SafeGuard) guard.clone());
         });

         sgroup.setVersion(this.version.get());
      }
      return sgroup;

   }





}
