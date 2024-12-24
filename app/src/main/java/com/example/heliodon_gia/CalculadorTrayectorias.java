package com.example.heliodon_gia;

import static java.lang.Math.asin;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.List;

public class CalculadorTrayectorias{
    public static List<Coordenadas> calcular_puntos(Coordenadas punto1, Coordenadas punto2){
        double zen1 = punto1.getZenit();
        double zen2 = punto2.getZenit();
        double az1 = punto1.getAzimut();
        double az2 = punto2.getAzimut();
        double angulo_entre_bolas = 2 * asin(sqrt(pow(sin((zen1 - zen2) / 2), 2) + cos(zen1) * cos(zen2) * pow(sin((az1 - az2) / 2), 2)));
        double distancia = 0.0872665;
        int cantidad_bolitas = (int) ceil(angulo_entre_bolas / distancia);
        List<Coordenadas> lista_bolitas = new ArrayList<>();
        // distancia angular entre los dos
        for (int i=0;i<cantidad_bolitas;i++){// in range(cantidad_bolitas){ //:#(#int i = 0; i <= cantidadPuntos; i++){
            double f = (double)i / cantidad_bolitas;
            double A = sin((1. - f) * angulo_entre_bolas) / sin(angulo_entre_bolas);
            double B = sin(f * angulo_entre_bolas) / sin(angulo_entre_bolas);
            double x = A * cos(zen1) * cos(az1) + B * cos(zen2) * cos(az2);
            double y = A * cos(zen1) * sin(az1) + B * cos(zen2) * sin(az2);
            double z = A * sin(zen1) + B * sin(zen2);
            double az = atan2_2(y, x);
            double zen = atan2_2(z, sqrt(x * x + y * y));
            lista_bolitas.add(new Coordenadas(zen,az,true));
        }
        return lista_bolitas;
    }
    public static double atan2_2(double a, double b){
        return Math.atan2(b, a);
    }
}
