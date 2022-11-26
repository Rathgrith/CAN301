package com.example.can301.entity;



import java.util.Date;


public class Seat {
    private int id;
    private int seat_id;
    private int user_id;
    private int status;
    private Date start_time;
    private Date end_time;

    public Seat(int id, int seat_id, int user_id, int status, Date start_time, Date end_time) {
        this.id = id;
        this.seat_id = seat_id;
        this.user_id = user_id;
        this.status = status;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Seat() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(int seat_id) {
        this.seat_id = seat_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}
