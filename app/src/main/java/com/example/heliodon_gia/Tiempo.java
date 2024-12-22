package com.example.heliodon_gia;

public class Tiempo {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    public Tiempo(int year, int month, int day, int hour, int minute, int second){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    public Tiempo(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
    }
    public static boolean mayor(Tiempo a, Tiempo b){
        if (a.year > b.year){
            return true;
        }else if (a.year < b.year){
            return false;
        }
        if (a.month > b.month) {
            return true;
        }
        else if(a.month < b.month){
            return false;
        }
        if (a.day > b.day){
            return true;
        } else if (a.day < b.day) {
            return false;
        }
        int hora_decimal_a = hora_dec(a.hour, a.minute, a.second);
        int hora_decimal_b = hora_dec(b.hour, b.minute, b.second);
        if (hora_decimal_a > hora_decimal_b){
            return true;
        }
        return false;
    }
    private static int hora_dec(int hora, int minute, int second){
        return hora * 24 *3600 + minute * 60 + second;
    }
    public float en_minutos(){
        return this.hour * 60 + this.minute + this.second / 60f;
    }
}
