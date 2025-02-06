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
    public Tiempo(int hour, int minute, int second, boolean a) {
        this.year = 0;  // Valores por defecto
        this.month = 0;
        this.day = 0;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public static Tiempo horadesdeString(String horaStr) {
        String[] partes = horaStr.split(":");
        int hour = Integer.parseInt(partes[0]);
        int minute = Integer.parseInt(partes[1]);
        int second = partes.length > 2 ? Integer.parseInt(partes[2]) : 0;
        return new Tiempo(hour, minute, second);
    }
    public String aString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
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
    public static Tiempo interpolarHora(Tiempo a, Tiempo b, double t) {
        if (t < 0 || t > 1) throw new IllegalArgumentException("t debe estar entre 0 y 1");

        int segundosA = hora_dec(a.hour, a.minute, a.second);
        int segundosB = hora_dec(b.hour, b.minute, b.second);
        int segundosInterpolados = (int) (segundosA + t * (segundosB - segundosA));

        int nuevaHora = segundosInterpolados / 3600;
        int nuevoMinuto = (segundosInterpolados % 3600) / 60;
        int nuevoSegundo = segundosInterpolados % 60;

        return new Tiempo(a.year, a.month, a.day, nuevaHora, nuevoMinuto, nuevoSegundo);
    }
}
