package com.example.heliodon_gia;

import androidx.annotation.NonNull;

import lombok.Getter;



import java.lang.Math;

@Getter
public class Coordenadas {
    private final double zenit;
    private final double azimut;
    private final double radian_1;
    private final double radian_2;

    public Coordenadas(double valor_1, double valor_2, boolean radianes) {
        if (radianes) {
            this.radian_1 = valor_1;
            this.radian_2 = valor_2;
            this.zenit = 90.0 - Math.toDegrees(this.radian_1); // Convertir zenit
            this.azimut = Math.toDegrees(this.radian_2) - 180.0; // Convertir azi
        }else{
            this.zenit = valor_1;
            this.azimut = valor_2;
            this.radian_1 = Math.toRadians(90.0 - this.zenit);
            this.radian_2 = Math.toRadians(180 + azimut);
        }

    }

    @NonNull
    @Override
    public String toString() {
        return "Coordenadas{zenit=" + zenit + ", azimut=" + azimut + "}";
    }
}
