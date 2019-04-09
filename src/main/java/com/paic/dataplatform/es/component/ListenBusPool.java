package com.paic.dataplatform.es.component;

import com.paic.dataplatform.es.Util.DateUtils;
import com.paic.dataplatform.es.Util.UuidUtils;
import com.paic.dataplatform.es.bus.ListenBus;
import com.paic.dataplatform.es.component.pool.EsClientPool;
import com.paic.dataplatform.es.event.AllEsEvent;
import com.paic.dataplatform.es.event.PeriodEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * Created by czx on 4/2/19.
 */
@Component
public class ListenBusPool implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(ListenBusPool.class);

    @Resource
    private ListenBus bus;


    @Override
    public void run(String... args) throws Exception {

        logger.info("启动 listen bus");
        startListenBus();

    }

    public void startListenBus(){

        ExecutorService listenBusThreadPool = Executors.newFixedThreadPool(3, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "listen-bus" + r.hashCode());
            }
        });

        listenBusThreadPool.execute(bus);
        listenBusThreadPool.execute(bus);
        listenBusThreadPool.execute(bus);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "listen-bus-timer-task" + r.hashCode());
            }
        });

        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                logger.info("开始上报节点信息");

                String uuid = UuidUtils.generateUUID();
                PeriodEvent event =
                        new PeriodEvent(uuid,
                                ((LocalConfig)SpringContext.getBean("localConfig")).getHost(),
                                ((LocalConfig)SpringContext.getBean("localConfig")).getPort(),
                                AllEsEvent.eventMap(),EsClientPool.getEsClientPool(), DateUtils.getCurrentDateTime(),null);

                bus.post(event);
            }
        }, 10000, 60000, TimeUnit.MILLISECONDS);


    }



}
