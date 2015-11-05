package com.calendar.koko.model.objects;

/**
 * Created by farouk on 11/5/15.
 */
public class ConfirmObject {
    Integer success;
    String result;
    String error;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
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
