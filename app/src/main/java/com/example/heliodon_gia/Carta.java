package com.example.heliodon_gia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
//import lombok.With;
@Value
//@With
@AllArgsConstructor
@Getter
public class Carta {
    double latitud;
    double longitud;
    String fecha;
    String hora;
    int ang;
}



