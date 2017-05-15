package com.example.user.fullthrottle;

/**
 * Created by bihaniayush on 13/4/17.
 */

public class Event {
    String date;
    String destination;
    String start;
    String name;
    String phone;
    String startlatitude,startlongitude,destnlatitude,destnlongitude;
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartlatitude() {
        return startlatitude;
    }

    public void setStartlatitude(String startlatitude) {
        this.startlatitude = startlatitude;
    }

    public String getStartlongitude() {
        return startlongitude;
    }

    public void setStartlongitude(String startlongitude) {
        this.startlongitude = startlongitude;
    }

    public String getDestnlatitude() {
        return destnlatitude;
    }

    public void setDestnlatitude(String destnlatitude) {
        this.destnlatitude = destnlatitude;
    }

    public String getDestnlongitude() {
        return destnlongitude;
    }

    public void setDestnlongitude(String destnlongitude) {
        this.destnlongitude = destnlongitude;
    }



    Event(String date,String start,String destination,String name,String phone,String startlatitude,String startlongitude,String destnlatitude,String destnlongitude)
    {

        this.date=date;
        this.destination=destination;
        this.start=start;
        this.startlatitude = startlatitude;
        this.startlongitude = startlongitude;
        this.destnlatitude = destnlatitude;
        this.destnlongitude = destnlongitude;
    }
    Event(){}
    Event(String date,String start,String destination)
    {
        this.date=date;
        this.destination=destination;
        this.start=start;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDestn() {
        return destination;
    }

    public void setDestn(String destn) {
        this.destination = destn;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }


}
