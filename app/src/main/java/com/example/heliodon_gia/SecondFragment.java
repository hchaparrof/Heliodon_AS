package com.example.heliodon_gia;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {
//    private boolean mas_puntos = false;
    private static final String TAG = "MainActivity_2";
    private Carta carta_actual;
    private int repeticiones = 0;
    // lista de cartas
    private List<Carta> cartas;
    //lista de manejo
    private EditText latitud;
    private EditText longitud;
    private EditText fecha;
    private EditText hora;
    private EditText angulo;
    private EditText repeticiones_texto;
    private TextView texto_actual;
    private TextView texto_cantidad;
    private int id_carta_actual = -1;
    // botones de aniadir y eso
    private Button limpiar;
    private Button aniadir;
    private Button aceptar;
    // botones flechas
    private Button flecha_izq;
    private Button flecha_der;
    public SecondFragment() {
        // Required empty public constructor
    }
    public interface OnButtonClickListener_aceptar {
        void onButton_aceptar_Click(List<Carta> lista_posiciones);
    }
    private SecondFragment.OnButtonClickListener_aceptar callback;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (SecondFragment.OnButtonClickListener_aceptar) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmento2ButtonClickListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cartas = new ArrayList<>();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        // identificar los TextEdit
        latitud = view.findViewById(R.id.latitude_input);
        longitud = view.findViewById(R.id.longitude_input);
        fecha = view.findViewById(R.id.date_input);
        hora = view.findViewById(R.id.time_input);
        angulo = view.findViewById(R.id.angulo_input);
        repeticiones_texto = view.findViewById(R.id.repeticiones);
        repeticiones_texto.setEnabled(false);
        //

        texto_cantidad = view.findViewById(R.id.text_cantidad_cartas);
        texto_actual = view.findViewById(R.id.text_carta_actual);
        //id flechas
        flecha_izq = view.findViewById(R.id.button_izq_carta);
        flecha_der = view.findViewById(R.id.button_der_carta);
        flecha_izq.setOnClickListener(v -> cambiar_carta(-1));  // Incrementar
        flecha_der.setOnClickListener(v -> cambiar_carta(1)); // Decrementar

        //id botones normales
        limpiar = view.findViewById(R.id.button_limpiar);
        aceptar = view.findViewById(R.id.button_aceptar);
        aniadir = view.findViewById(R.id.button_aniadir);
        //listeners normales
        limpiar.setOnClickListener(v -> limpiarContenido());
        aniadir.setOnClickListener(v -> aniadirContenido());
        //
        aceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("MainActivity", "se oprimio un boton_en fragmento");
                callback.onButton_aceptar_Click(cartas);
            }
                                   });
        cambiar_cantidad(0);
        cambiar_actual(0);
        return view;
    }

    private void cambiar_carta(int i) {
        // Verificar si la lista tiene elementos
        if (cartas.isEmpty()) {
            System.out.println("La lista de cartas está vacía. No se puede cambiar.");
            return;
        }
        Log.d(TAG, "despues_de_is_empty");
        // Calcular el nuevo índice
        int nuevoIndice = id_carta_actual + i;

        // Verificar si está en los casos límite (primer o último elemento)
        if (nuevoIndice < 0) {
            System.out.println("Ya estás en la primera carta. No puedes retroceder.");
            return;
        } else if (nuevoIndice >= cartas.size()) {
            System.out.println("Ya estás en la última carta. No puedes avanzar.");
            return;
        }
        Log.d(TAG, "despues_de_lo_del_indice");
        // Actualizar el índice actual y cambiar la carta
        id_carta_actual = nuevoIndice;
        carta_actual = cartas.get(id_carta_actual);
        Log.d(TAG, "despues_de_lo_del_get");
//        System.out.println("Carta actual cambiada a: " + cartaActual);
        cambiar_texto_carta();
        Log.d(TAG, "despues_de_cambiar_texto_carta");
        cambiar_actual(id_carta_actual + 1);
        Log.d(TAG, "despues_de_lo_de_cambair_actual");
    }

    private void cambiar_actual(int i) {
        texto_actual.setText("actual " + i);
    }

    private void cambiar_texto_carta() {
        if (cartas.isEmpty()) {
//            System.out.println("La lista de cartas está vacía. No se puede cambiar texto.");
            return;
        }

        // Obtener la carta actual según el índice
//        Carta cartaActual = cartas.get(indiceActual);

        // Actualizar los valores en los TextEdit con los datos de cartaActual
        latitud.setText(String.valueOf(carta_actual.getLatitud()));
        longitud.setText(String.valueOf(carta_actual.getLongitud()));
        fecha.setText(carta_actual.getFecha());
        hora.setText(carta_actual.getHora());
        angulo.setText(String.valueOf(carta_actual.getAng()));
        Log.d(TAG, "despues_de_los_set_de_texto");

        // Activar o desactivar la edición según si es el último elemento
        boolean esUltimoElemento = (id_carta_actual == cartas.size() - 1);
        latitud.setEnabled(esUltimoElemento);
        longitud.setEnabled(esUltimoElemento);
        fecha.setEnabled(esUltimoElemento);
        hora.setEnabled(esUltimoElemento);
        angulo.setEnabled(esUltimoElemento);
        repeticiones_texto.setEnabled(esUltimoElemento);

        if (esUltimoElemento) {
            System.out.println("Modo edición activado: Se pueden editar los campos.");
        } else {
            System.out.println("Modo solo lectura: Los campos están bloqueados.");
        }
    }

    private void aniadirContenido() {
        Log.d(TAG, "oprimiendo_boton de aniadir");
        String latitudeText = latitud.getText().toString().trim();
        String longitudeText = longitud.getText().toString().trim();
        String dateText = fecha.getText().toString().trim();
        String timeText = hora.getText().toString().trim();
        String anguloText = angulo.getText().toString().trim();
        String repeticionesText = repeticiones_texto.getText().toString().trim();
        //todo quitar los toast
        Log.d(TAG, "intentando_parsear_las_cosas");
        if (TextUtils.isEmpty(latitudeText) || !isValidLatitude(latitudeText)) {
            Toast.makeText(getContext(), "Ingrese una latitud válida", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "parseado_latitud");
        if (TextUtils.isEmpty(longitudeText) || !isValidLongitude(longitudeText)) {
            Toast.makeText(getContext(), "Ingrese una longitud válida", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "parseado_longitud");
        if (TextUtils.isEmpty(dateText) || !isValidDate(dateText)) {
            Toast.makeText(getContext(), "Ingrese una fecha válida (dd/mm/yyyy)", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "parseado_fecha");
        if (TextUtils.isEmpty(timeText) || !isValidTime(timeText)) {
            Toast.makeText(getContext(), "Ingrese una hora válida (hh:mm:ss)", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "parseado_hora");
        if (TextUtils.isEmpty(anguloText) || !isValidAngle(anguloText)) {
            Toast.makeText(getContext(), "Ingrese un ángulo válido (0-360°)", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "parseado_angulo");
        if ( !isValidRepeticiones(repeticionesText)) {
            Toast.makeText(getContext(), "Ingrese un número entero válido mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "parseado_repeticiones");
        // Convertir los valores a los tipos adecuados
        double latitude = Double.parseDouble(latitudeText);
        double longitude = Double.parseDouble(longitudeText);
        int angulo = Integer.parseInt(anguloText);
        if (repeticionesText.isEmpty()){
            repeticiones =1;
        }else{
            repeticiones = Integer.parseInt(repeticionesText);
        }
        Log.d(TAG, "antes_de_aniadir");
        Carta carta_provisional = new Carta(latitude, longitude, dateText, timeText, angulo);
        if (cartas.isEmpty()){
            cartas.add(carta_provisional);
            cambiar_carta(1);
            cambiar_cantidad(cartas.size());
            repeticiones_texto.setEnabled(true);
        }else {
            if (repeticiones == 1){
                if (VerificadorCartas.difierenEnUnCampo(carta_actual, carta_provisional)) {
                    cartas.add(carta_provisional);
                    cambiar_carta(1);
                    cambiar_cantidad(cartas.size());
                    Toast.makeText(getContext(), "carta_nueva_aniadida", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "no_se_hizo_nada,cambie_solo_un_campo", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "despues_de_aniadir");
            }
            int campoDiferente = VerificadorCartas.primera_diferencia(carta_actual, carta_provisional);

            for (int i = 1; i <= repeticiones; i++) {
                double nuevaLatitud = carta_actual.getLatitud();
                double nuevaLongitud = carta_actual.getLongitud();
                String nuevaFecha = carta_actual.getFecha();
                String nuevaHora = carta_actual.getHora();
                int nuevoAngulo = carta_actual.getAng();

                // Modificar el campo que varía progresivamente
                switch (campoDiferente) {
                    case 1:
                        nuevaLatitud = carta_actual.getLatitud() +
                                (i * (carta_provisional.getLatitud() - carta_actual.getLatitud()) / repeticiones);
                        break;
                    case 2:
                        nuevaLongitud = carta_actual.getLongitud() +
                                (i * (carta_provisional.getLongitud() - carta_actual.getLongitud()) / repeticiones);
                        break;
                    case 3:
                        nuevaFecha = carta_provisional.getFecha(); // Aquí podríamos interpolar fechas si es necesario
                        break;
                    case 4:
                        // Convertir Strings a objetos Tiempo
                        Tiempo tiempoActual = Tiempo.horadesdeString(carta_actual.getHora());
                        Tiempo tiempoProvisional = Tiempo.horadesdeString(carta_provisional.getHora());

                        // Interpolar la hora
                        double t = (double) i / repeticiones;
                        Tiempo tiempoInterpolado = Tiempo.interpolarHora(tiempoActual, tiempoProvisional, t);

                        // Convertir de nuevo a String
                        nuevaHora = tiempoInterpolado.aString();
                        break;
                    case 5:
                        nuevoAngulo = carta_actual.getAng() +
                                (i * (carta_provisional.getAng() - carta_actual.getAng()) / repeticiones);
                        break;
                }
                Carta nuevaCarta = new Carta(nuevaLatitud, nuevaLongitud, nuevaFecha, nuevaHora, nuevoAngulo);
                cartas.add(nuevaCarta);
            }
        }
            cambiar_carta(1);
            cambiar_cantidad(cartas.size());
    }

    private void cambiar_cantidad(int numero) {
        texto_cantidad.setText("cantidad " + numero );
    }

    private boolean isValidLatitude(String latitude) {
        try {
            double lat = Double.parseDouble(latitude);
            return lat >= -90 && lat <= 90;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidLongitude(String longitude) {
        try {
            double lon = Double.parseDouble(longitude);
            return lon >= -180 && lon <= 180;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDate(String date) {
        return date.matches("^\\d{2}/\\d{2}/\\d{4}$");
    }

    private boolean isValidTime(String time) {
        return time.matches("^\\d{2}:\\d{2}:\\d{2}$");
    }

    private boolean isValidAngle(String angle) {
        try {
            int ang = Integer.parseInt(angle);
            return ang >= 0 && ang <= 360;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidRepeticiones(String repeticiones) {
        if (repeticiones.isEmpty()){
            return true;
        }
        try {
            int rep = Integer.parseInt(repeticiones);
            return rep > 0; // Verifica que sea mayor a 0
        } catch (NumberFormatException e) {
            return false; // No es un número entero válido
        }
    }
    private void limpiarContenido() {
        cartas = new ArrayList<>();
        carta_actual = null;
        id_carta_actual = -1;
        latitud.setText("");
        longitud.setText("");
        fecha.setText("");
        hora.setText("");
        angulo.setText("");
        cambiar_cantidad(cartas.size());
        cambiar_actual(0);
        latitud.setEnabled(true);
        longitud.setEnabled(true);
        fecha.setEnabled(true);
        hora.setEnabled(true);
        angulo.setEnabled(true);
        repeticiones_texto.setEnabled(false);
        repeticiones_texto.setText("");
        repeticiones_texto.setHint("##");
    }
}
