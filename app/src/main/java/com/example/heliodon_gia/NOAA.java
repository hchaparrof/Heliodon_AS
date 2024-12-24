package com.example.heliodon_gia;

public class NOAA {
    public static Coordenadas calc_Noaa(Carta carta){//int anio, int mes, int dia, int hora, int minutos, int segundos, float latitud, float longitud, float time_zone) {
        String[] fechaPartes = carta.getFecha().split("/"); // Asumiendo formato "yyyy-MM-dd"
        int anio = Integer.parseInt(fechaPartes[0]);
        int mes = Integer.parseInt(fechaPartes[1]);
        int dia = Integer.parseInt(fechaPartes[2]);
        String[] horaPartes = carta.getHora().split(":"); // Asumiendo formato "HH:mm:ss"
        int hora = Integer.parseInt(horaPartes[0]);
        int minutos = Integer.parseInt(horaPartes[1]);
        int segundos = horaPartes.length > 2 ? Integer.parseInt(horaPartes[2]) : 0;
        double latitud = carta.getLatitud();
        double longitud = carta.getLongitud();
        int angulo = carta.getAng();
        int time_zone = -6;
//        int anio = 2019;
//        int mes = 1;
//        int dia = 1;
//        int hora = 12;
//        int minutos = 0;
//        int segundos = 0;
//        float latitud = 40;
//        float longitud = -105;
//        float time_zone = -6;
        Tiempo fecha = new Tiempo(anio, mes, dia, hora, minutos, segundos);
        float juliano = anio_juliano(fecha);
        System.out.println(juliano);
        float julian_century = (juliano - 2451545)/36525f;
        float Geom_Mean_Long_Sun = (float)residuo(280.46646f + julian_century *
                (36000.76983f + julian_century * 0.0003032f),360);
        float Geom_Mean_Anom_Sun = 357.52911f + julian_century * (35999.05029f - 0.0001537f * julian_century);
        float Eccent_Earth_Orbit = 0.016708634f - julian_century*(0.000042037f + 0.0000001267f * julian_century);
        float Sun_Eq_of_Ctr = (float)( Math.sin(Math.toRadians(Geom_Mean_Anom_Sun)) * (1.914602f - julian_century *
                (0.004817f + 0.000014f * julian_century)) + Math.sin(Math.toRadians(2 * Geom_Mean_Anom_Sun))
                * (0.019993f - 0.000101f * julian_century) + Math.sin(Math.toRadians(3 * Geom_Mean_Anom_Sun)) * 0.000289f);
        float Sun_True_Long = Geom_Mean_Long_Sun + Sun_Eq_of_Ctr;
        float Sun_True_Anom = Geom_Mean_Anom_Sun +  Sun_Eq_of_Ctr;
        float Sun_Rad_Vector = (float)( (1.000001018f * (1 - Eccent_Earth_Orbit * Eccent_Earth_Orbit)) /
                (1 + Eccent_Earth_Orbit * Math.cos(Math.toRadians(Sun_True_Anom))));
        float Sun_App_Long = (float)(Sun_True_Long - 0.00569f - 0.00478f *
                Math.sin(Math.toRadians(125.04f - 1934.136f * julian_century)));
        float Mean_Obliq_Ecliptic = 23 + (26 + ((21.448f - julian_century *
                (46.815f + julian_century * (0.00059f - julian_century * 0.001813f)))) / 60) / 60;
        float Obliq_Corr = (float)(Mean_Obliq_Ecliptic + 0.00256f * Math.cos(Math.toRadians(125.04f -
                1934.136f * julian_century)));
        float Sun_Rt_Ascen = (float)(Math.toDegrees(atan2_2((float)Math.cos(Math.toRadians(Sun_App_Long)),
                (float)(Math.cos(Math.toRadians(Obliq_Corr))*Math.sin(Math.toRadians(Sun_App_Long))))));
        float Sun_Declin = (float)(Math.toDegrees(Math.asin(Math.sin(Math.toRadians(Obliq_Corr)) *
                Math.sin(Math.toRadians(Sun_App_Long)))));
        float var_y = (float) (Math.tan(Math.toRadians(Obliq_Corr / 2)) * Math.tan(Math.toRadians(Obliq_Corr / 2)));
        float Eq_of_Time = (float)(4 * Math.toDegrees(var_y * Math.sin(2 * Math.toRadians(Geom_Mean_Long_Sun)) - 2 * Eccent_Earth_Orbit *
                Math.sin(Math.toRadians(Geom_Mean_Anom_Sun)) + 4 * Eccent_Earth_Orbit *
                var_y * Math.sin(Math.toRadians(Geom_Mean_Anom_Sun)) * Math.cos(2 * Math.toRadians(Geom_Mean_Long_Sun)) - 0.5f * var_y * var_y * Math.sin(4 *
                Math.toRadians(Geom_Mean_Long_Sun)) - 1.25f * Eccent_Earth_Orbit * Eccent_Earth_Orbit * Math.sin(2 * Math.toRadians(Geom_Mean_Anom_Sun))));
        float HA_Sunrise = (float) (Math.toDegrees(Math.acos(Math.cos(Math.toRadians(90.833f)) /
                (Math.cos(Math.toRadians(latitud)) * Math.cos(Math.toRadians(Sun_Declin))) -
                Math.tan(Math.toRadians(latitud)) * Math.tan(Math.toRadians(Sun_Declin)))));
        float Solar_Noon = (float) ((720 - 4 * longitud - Eq_of_Time + time_zone * 60) / 1440);
        float Sunrise_Time = Solar_Noon - HA_Sunrise * 4 / 1440;
        float Sunset_Time = Solar_Noon + HA_Sunrise * 4 / 1440;
        float Sunlight_Duration = 8 * HA_Sunrise;
        float True_Solar_Time = (float) (residuo((float) (fecha.en_minutos() + Eq_of_Time + 4 *
                        longitud - 60 * time_zone), 1440));
        float Hour_Angle = 0;
        if(True_Solar_Time / 4 < 0){
            Hour_Angle = True_Solar_Time / 4 + 180;
        }else {//; True_Solar_Time / 4 + 180 ; True_Solar_Time / 4 - 180);
            Hour_Angle = True_Solar_Time / 4 - 180;
        }
        float Solar_Zenith_Angle = (float)(Math.toDegrees(Math.acos(Math.sin(Math.toRadians(latitud)) * Math.sin(Math.toRadians(Sun_Declin)) +
                Math.cos(Math.toRadians(latitud)) * Math.cos(Math.toRadians(Sun_Declin)) * Math.cos(Math.toRadians(Hour_Angle)))));
        float Solar_Elevation_Angle = 90 - Solar_Zenith_Angle;
        float Approx_Atmospheric_Refraction = 0;
        if(Solar_Elevation_Angle > 85 ){
            Approx_Atmospheric_Refraction = 0;
        } else if (Solar_Elevation_Angle > 5 ) {
            Approx_Atmospheric_Refraction = (float) (58.1f / Math.tan(Math.toRadians(Solar_Elevation_Angle)) -0.07f /
                    Math.pow(Math.tan(Math.toRadians(Solar_Elevation_Angle)), 3) + 0.000086f /
                    Math.pow(Math.tan(Math.toRadians(Solar_Elevation_Angle)), 5));
        }else if (Solar_Elevation_Angle > -0.575f){
            Approx_Atmospheric_Refraction = 1735 + Solar_Elevation_Angle * (-518.2f + Solar_Elevation_Angle * (103.4f + Solar_Elevation_Angle *
                    (-12.79f + Solar_Elevation_Angle * 0.711f)));
        }else{
            Approx_Atmospheric_Refraction = (float)(-20.772f /Math.tan(Math.toRadians(Solar_Elevation_Angle)));
        }
        Approx_Atmospheric_Refraction = Approx_Atmospheric_Refraction / 3600;

//
//        SI(Solar_Elevation_Angle > 85 ;
//            0
//        ; SI(Solar_Elevation_Angle > 5 ;
//            58.1f / Math.tan(Math.toRadians(Solar_Elevation_Angle)) -0.07f / Math.pow(Math.tan(Math.toRadians(Solar_Elevation_Angle)), 3) + 0.000086f /
//                Math.pow(Math.tan(Math.toRadians(Solar_Elevation_Angle)), 5); //
//        SI(Solar_Elevation_Angle > -0.575f;
//        1735 + Solar_Elevation_Angle * (-518.2f + Solar_Elevation_Angle * (103.4f + Solar_Elevation_Angle * (-12.79f + Solar_Elevation_Angle * 0.711f)));
//        -20.772f /Math.tan(Math.toRadians(Solar_Elevation_Angle))))) / 3600;
        float Solar_Elevation_corrected_for_atm_refraction = Solar_Elevation_Angle + Approx_Atmospheric_Refraction;
        float Solar_Azimuth_Angle = 0;
        if (Hour_Angle > 0 ){
            Solar_Azimuth_Angle = (float)(Math.IEEEremainder(Math.toDegrees(Math.acos(((Math.sin(Math.toRadians(latitud))
                    * Math.cos(Math.toRadians(Solar_Zenith_Angle))) - Math.sin(Math.toRadians(Sun_Declin))) / (Math.cos(Math.toRadians(latitud))
                    * Math.sin(Math.toRadians(Solar_Zenith_Angle))))) + 180, 360));
        }else{
            Solar_Azimuth_Angle = (float)(Math.IEEEremainder(540 - Math.toDegrees(Math.acos(((Math.sin(Math.toRadians(latitud)) *
                    Math.cos(Math.toRadians(Solar_Zenith_Angle))) - Math.sin(Math.toRadians(Sun_Declin))) /
                    (Math.cos(Math.toRadians(latitud)) * Math.sin(Math.toRadians(Solar_Zenith_Angle))))), 360));
        }
//        SI( Hour_Angle > 0 ; Math.IEEEremainder(Math.toDegrees(Math.acos(((Math.sin(Math.toRadians(latitud))
//                * Math.cos(Math.toRadians(Solar_Zenith_Angle))) - Math.sin(Math.toRadians(Sun_Declin))) / (Math.cos(Math.toRadians(latitud))
//                * Math.sin(Math.toRadians(Solar_Zenith_Angle))))) + 180, 360);
//        Math.IEEEremainder(540 - Math.toDegrees(Math.acos(((Math.sin(Math.toRadians(latitud)) * Math.cos(Math.toRadians(Solar_Zenith_Angle))) - Math.sin(Math.toRadians(Sun_Declin))) /
//                (Math.cos(Math.toRadians(latitud)) * Math.sin(Math.toRadians(Solar_Zenith_Angle))))), 360));
        System.out.println(Solar_Azimuth_Angle);
        System.out.println(Solar_Zenith_Angle);
        return new Coordenadas(Solar_Zenith_Angle, Solar_Azimuth_Angle + angulo, false);
    }
    private static float anio_juliano(Tiempo fecha){
        int julian_month = 0;
        int julian_year = 0;
        float julian_day = 0;
        if (fecha.month < 3){
            julian_month = fecha.month + 12;
            julian_year = fecha.year - 1;
        }
        if (fecha.year <1){
            julian_year++;
        }
        float v = (float)(fecha.hour) / 24.0f;
        julian_day = fecha.day + v + fecha.minute/1440f + fecha.second/86400;
        int A = (int)(julian_year/ 100f);
        float B = 0;
        if (Tiempo.mayor(fecha,new Tiempo(1582, 10, 15))){
            B = (2 - A) + (int) (A / 4f);
        }
        float dia_final = (float) (Math.floor(365.25f * (julian_year+ 4716)) + Math.floor(30.6001f * (julian_month + 1))
                + julian_day + B - 1_524.5f);
        System.out.println(julian_year);
        System.out.println(julian_month);
        System.out.println(julian_day);

        return dia_final;
    }
    public static float residuo(float a, float b){
        float division = a / b;
        int division_entera = (int) division;
        float residuo_division = division - (float)division_entera;
        float residuo_final = residuo_division * b;
        return residuo_final;
    }
    public static float atan2_2(float a, float b){
        return (float)Math.atan2(b, a);
    }
}
