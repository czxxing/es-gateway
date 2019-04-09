package com.paic.dataplatform.es.model;

import java.io.Serializable;

/**
 * Created by czx on 4/2/19.
 */
public class EventDTO implements Serializable {

    private String eventName ;

    private Boolean open;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
}
