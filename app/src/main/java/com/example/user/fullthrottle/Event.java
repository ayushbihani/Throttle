package com.example.user.fullthrottle;

/**
 * Created by bihaniayush on 13/4/17.
 */

public class Event {
    String date;
    String destination;
    String start;
    Event(String date,String start,String destination)
    {
        this.date=date;
        this.destination=destination;
        this.start=start;
    }
    Event(){}

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
