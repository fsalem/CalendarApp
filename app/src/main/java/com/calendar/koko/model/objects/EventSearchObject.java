package com.calendar.koko.model.objects;

import java.util.List;

/**
 * Created by farouk on 11/5/15.
 */
public class EventSearchObject {
    Integer success;
    String error;
    List<EventObject> result;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void setResult(List<EventObject> result) {
        this.result = result;
    }

    public List<EventObject> getResult() {
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
