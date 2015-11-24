package com.calendar.koko.model.objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by farouk on 11/5/15.
 */
public class CreateEventObject implements Serializable {
    private String email;
    private String password;
    private String name;
    private String desc;
    private Long sDate;
    private Long eDate;
    private String location;
    private Boolean notify;
    private Long nDate;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getsDate() {
        return sDate;
    }

    public void setsDate(Long sDate) {
        this.sDate = sDate;
    }

    public Long geteDate() {
        return eDate;
    }

    public void seteDate(Long eDate) {
        this.eDate = eDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public Long getnDate() {
        return nDate;
    }

    public void setnDate(Long nDate) {
        this.nDate = nDate;
    }
}
