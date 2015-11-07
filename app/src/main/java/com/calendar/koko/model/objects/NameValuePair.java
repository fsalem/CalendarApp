package com.calendar.koko.model.objects;

/**
 * Created by farouk on 11/5/15.
 */
public class NameValuePair {
    private String name;
    private String value;

    public NameValuePair(){
        setName(null);
        setValue(null);
    }

    public NameValuePair(String name, String value){
        setName(name);
        setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
