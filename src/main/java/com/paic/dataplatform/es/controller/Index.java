package com.paic.dataplatform.es.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by czx on 3/28/19.
 */
@RestController
public class Index {


    @RequestMapping(value = "/index")
    public String index() {


        return "hello world!!!";
    }

}
