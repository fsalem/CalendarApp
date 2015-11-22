package com.calendar.koko.model.objects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by farouk on 11/5/15.
 */

public class EventSearchPeriodObject implements Serializable {

    Integer success;
    String error;
    List<EventPeriodResultObject> result;


    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void setResult(List<EventPeriodResultObject> result) {
        this.result = result;
    }

    public List<EventPeriodResultObject> getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return getSuccess().toString();
    }
}
