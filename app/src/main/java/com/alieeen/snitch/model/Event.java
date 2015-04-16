package com.alieeen.snitch.model;

/**
 * Created by alinekborges on 09/04/15.
 */
public class Event {

    private long id;
    private String cameraName;
    private String dateTime;
    private String number;
    private String imageName;
    private int viewed;

    public Event() {

    }

    public Event(long id, String cameraName, String dateTime, String number, String imageName, int viewed) {
        this.id = id;
        this.cameraName = cameraName;
        this.dateTime = dateTime;
        this.number = number;
        this.imageName = imageName;
        this.viewed = viewed;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setCameraName(String camName) {
        this.cameraName = camName;
    }

    public String getCameraName() {
        return this.cameraName;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }

    public void setImageName(String imgName) {
        this.imageName = imgName;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setViewed(boolean viewed) {
        if (viewed)
            this.viewed = 1;
        else this.viewed = 0;
    }

    public boolean getViewed() {
        return this.viewed == 1 ? true : false;
    }

    //@Override
    public String ToString() {
        return this.imageName + "\r\n" + this.dateTime;
    }
}
