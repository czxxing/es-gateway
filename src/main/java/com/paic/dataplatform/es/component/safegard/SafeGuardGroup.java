package com.paic.dataplatform.es.component.safegard;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by czx on 4/9/19.
 */
@Component
public class SafeGuardGroup {



   private static CopyOnWriteArrayList<SafeGuard> safeGuards = new CopyOnWriteArrayList<SafeGuard>();


   public SafeGuardGroup(){

      //at least one
      safeGuards.add(new EmptySafeGuard());
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





}
