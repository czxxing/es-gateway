package com.dataplatform.es.component.aspectj;

import com.alibaba.fastjson.JSON;
import com.dataplatform.es.Util.DateUtils;
import com.dataplatform.es.Util.UuidUtils;
import com.dataplatform.es.Util.ThisWebUtils;
import com.dataplatform.es.bus.ListenBus;
import com.dataplatform.es.component.LocalConfig;
import com.dataplatform.es.component.pool.EsClient;
import com.dataplatform.es.component.pool.EsClientPool;
import com.dataplatform.es.component.safegard.EmptySafeGuard;
import com.dataplatform.es.component.safegard.SafeGuardException;
import com.dataplatform.es.component.safegard.SafeGuardGroup;
import com.dataplatform.es.component.safegard.ThreadLocalSafeGuardGroup;
import com.dataplatform.es.event.RequestEsEvent;
import com.dataplatform.es.event.ResposeEsEvent;
import com.dataplatform.es.model.ResultDTO;
import org.apache.http.util.EntityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.elasticsearch.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by czx on 3/28/19.
 */

@Component
@Aspect
@Order(1)
public class RecordEsQueryAspect {

    private static final Logger logger = LoggerFactory.getLogger(RecordEsQueryAspect.class);

    @Resource
    private ListenBus bus;

    @Resource
    private LocalConfig localConfig;

    @Resource
    private ThreadLocalSafeGuardGroup threadLocalSafeGuardGroup;


    @Pointcut("execution(* com.dataplatform.es.controller.EsQueryController.*(..))")
    public  void serviceAspect() { }




    /**
     * 通知方法会将目标方法封装起来
     *
     * @param pjp
     * @throws Throwable
     */
    @Around("serviceAspect()")
    public void around(ProceedingJoinPoint pjp) throws Throwable {

        String uuid = UuidUtils.generateUUID();

        logger.debug("请求{}，开始记录切面",uuid);



        long startTime = System.currentTimeMillis();

        Object[] args = pjp.getArgs();

        HttpServletRequest request = null;
        HttpServletResponse response = null;
        String body = null;

        if(args.length ==2){

            request = (HttpServletRequest) args[0];
            response = (HttpServletResponse) args[1];
            body = ThisWebUtils.getBody(request);

        }else if(args.length ==3){
            request = (HttpServletRequest) args[0];
            response = (HttpServletResponse) args[1];
            body = (String) args[2];

        }

        postRequestEvent(uuid,request,body);

        SafeGuardGroup safeGuardGroup=
                threadLocalSafeGuardGroup.getThreadSafeGuardGroup();

        try{
            safeGuardGroup.isAllPass(request,response);
            pjp.proceed(pjp.getArgs());

        }catch (SafeGuardException ex){
            ResultDTO result = new ResultDTO();
            result.setResultCode("1");
            result.setResultMessage(ex.getMessage());

            response.setContentType("application/json;charset=UTF-8");
            OutputStream stream = response.getOutputStream();
            stream.write(JSON.toJSONString(result).getBytes("UTF-8"));
        }



        long endTime = System.currentTimeMillis();
        double seconds = (endTime - startTime) / 1000F;

        postResposeEvent(uuid,request,response,seconds);

        logger.debug("请求{}，运行了： {} seconds",uuid,seconds);
    }





    private void postRequestEvent(String uuid,HttpServletRequest request, String body){

        try{

            String key = ThisWebUtils.getUrlKey(request);

            String rerouteUri = ThisWebUtils.getRerouteUri(request,localConfig.getFirstGateway());

            String method = request.getMethod();

            RequestEsEvent requestEsEvent =
                    new RequestEsEvent(uuid,localConfig.getHost(),localConfig.getPort(),key,rerouteUri,body,method,
                            DateUtils.getCurrentDateTime(), DateUtils.getCurrentDateTime(),null);

            bus.post(requestEsEvent);

        }catch (Exception e){
            logger.warn("切面发生错误",e);
        }

    }


    private void postResposeEvent(String uuid,HttpServletRequest request,HttpServletResponse response,  double ttl){

        try{
            ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
            if(wrapper != null) {
                byte[] buf = wrapper.getContentAsByteArray();
                String body = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());

                String key = ThisWebUtils.getUrlKey(request);

                ResposeEsEvent resposeEsEvent = new ResposeEsEvent(uuid,
                                                                    localConfig.getHost(),
                                                                    localConfig.getPort(),
                                                                    key,
                                                                    body,
                                                                    buf.length,
                                                                    ttl,
                                                                    DateUtils.getCurrentDateTime(),
                                                       null,
                                                                    geEsMemoryUse());
                bus.post(resposeEsEvent);
                wrapper.copyBodyToResponse();

            }else {
                throw new Exception("切面获取消息出错");
            }

        }catch (Exception e){
            logger.warn("切面发生错误",e);
        }

    }

    /*****正式应用下面的代码需要删除****************************/
    @Resource
    private EsClientPool pool;
    private Double geEsMemoryUse(){

        EsClient client = pool.getEsClient("local");

        try {
            Response esResponse= client.handleEsRequest("/_cat/nodes?h=hc","","GET");

            String hc = EntityUtils.toString(esResponse.getEntity());

            hc = hc.toLowerCase();

            if(hc.contains("mb")){
                hc = hc.substring(0,hc.indexOf("mb"));

                hc = String.valueOf(Double.valueOf(hc)/1000);
            }else{
                hc = hc.substring(0,hc.indexOf("g"));
            }

            return Double.valueOf(hc);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0.0;
    }







}
