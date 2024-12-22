package com.example.heliodon_gia;

import android.util.Log;

public class VerificadorCartas{
    private static final String TAG = "MainActivity_2";
    public static boolean difierenEnUnCampo(Carta carta1, Carta carta2) {
        int diferencias = 0;

        if (Double.compare(carta1.getLatitud(), carta2.getLatitud()) != 0) {
            diferencias++;
        }
        if (Double.compare(carta1.getLongitud(), carta2.getLongitud()) != 0) {
            diferencias++;
        }
        if (!carta1.getFecha().equals(carta2.getFecha())) {
            diferencias++;
        }
        if (!carta1.getHora().equals(carta2.getHora())) {
            diferencias++;
        }
        if (carta1.getAng() != carta2.getAng()) {
            diferencias++;
        }

        // Devuelve true si solo hay una diferencia
        return diferencias == 1;
    }
}
