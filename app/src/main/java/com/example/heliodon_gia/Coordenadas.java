package com.example.heliodon_gia;

public class Coordenadas {
    private final double zenit;
    private final double azimut;

    public Coordenadas(double zenit, double azimut) {
        this.zenit = zenit;
        this.azimut = azimut;
    }

    public double getZenit() {
        return zenit;
    }

    public double getAzimut() {
        return azimut;
    }

    @Override
    public String toString() {
        return "Coordenadas{zenit=" + zenit + ", azimut=" + azimut + "}";
    }
}
