package com.calendar.koko.model.objects;

import java.util.Date;

/**
 * Created by farouk on 11/5/15.
 */
public class EventObject {
    private String _id;
    private String title;
    private String description;
    private Long start;
    private Long end;
    private String location;
    private Boolean notify;
    private Date creationDate;
    private Long notificationDate;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Long notificationDate) {
        this.notificationDate = notificationDate;
    }
}
