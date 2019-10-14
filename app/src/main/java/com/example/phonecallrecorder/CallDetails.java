package com.example.phonecallrecorder;

/**
 * Created by SonuShaikh on 01-08-2019.
 */

public class CallDetails {

    private int serial;
    private String num;
  //  private String name;
    private String time;
    private String date;

    public CallDetails(){

    }

    public CallDetails(int serial,String num,String time,String date)
    {
        this.serial=serial;
        this.num=num;
        this.time=time;
        this.date=date;
    }

    public int getSerial()
    {
        return serial;
    }

    public void setSerial(int serial)
    {
        this.serial=serial;
    }

    public String getNum()
    {
        return num;
    }

    public void setNum(String num)
    {
        this.num=num;
    }

    public String getTime1()
    {
        return time;
    }

    public void setTime1(String time)
    {
        this.time=time;
    }

    public String getDate1()
    {
        return date;
    }

    public void setDate1(String date)
    {
        this.date=date;
    }
}
