package com.paic.dataplatform.es.Util;

import java.util.UUID;

/**
 * Created by czx on 4/2/19.
 */
public class UuidUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
