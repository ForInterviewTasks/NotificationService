package com.tasks.notificationservice.entity;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Comparable, Serializable {

    private String external_id;
    private String message;
    private Date time;
    private NotificationType notification_type;
    private String extra_param;

    public Notification(String external_id, String message, Date time, NotificationType notification_type, String extra_param) {

        if (external_id == null
                || message == null
                || time == null
                || notification_type == null
                || extra_param == null) {
            throw new NullPointerException();
        }

        this.external_id = external_id;
        this.message = message;
        this.time = time;
        this.notification_type = notification_type;
        this.extra_param = extra_param;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public NotificationType getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(NotificationType notification_type) {
        this.notification_type = notification_type;
    }

    public String getExtra_param() {
        return extra_param;
    }

    public void setExtra_param(String extra_param) {
        this.extra_param = extra_param;
    }

    public int compareTo(Object o) {

        if (o instanceof Notification) {
            Notification n = (Notification) o;
            return this.getTime().compareTo(n.getTime());
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (external_id != null ? !external_id.equals(that.external_id) : that.external_id != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (notification_type != that.notification_type) return false;
        return extra_param != null ? extra_param.equals(that.extra_param) : that.extra_param == null;
    }

    @Override
    public int hashCode() {
        int result = external_id != null ? external_id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (notification_type != null ? notification_type.hashCode() : 0);
        result = 31 * result + (extra_param != null ? extra_param.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "external_id='" + external_id + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", notification_type=" + notification_type +
                ", extra_param='" + extra_param + '\'' +
                '}';
    }
}
