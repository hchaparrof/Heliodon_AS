package com.example.heliodon_gia;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.TextView;

public class FirstFragment extends Fragment {
    private static final String TAG = "MainActivity_2";
    private TextView texto_monitor_serial;
    public FirstFragment() {
        // Required empty public constructor
    }
    public interface OnButtonClickListener_1 {
        void onButton_1_Click(int a);
    }
    private OnButtonClickListener_1 callback;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (OnButtonClickListener_1) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmento2ButtonClickListener");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        int[] buttonIds = {
                R.id.button_top1, R.id.button_up, R.id.button_top2,
                R.id.button_left, R.id.button_center, R.id.button_right,
                R.id.button_side1, R.id.button_down, R.id.button_side2
        };
        for (int i = 0; i < buttonIds.length; i++) {
            Button button = view.findViewById(buttonIds[i]);
            final int buttonNumber = i + 1;  // Esto va de 1 a 9

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Imprimir el número del botón cuando se presiona
//                    System.out.println("Botón presionado: " + buttonNumber);
                    Log.d("MainActivity", "se oprimio un boton_en fragmento");
                    callback.onButton_1_Click(buttonNumber);
                }
            });
        }
        texto_monitor_serial = view.findViewById(R.id.textView_2);
        return view;
    }
    public void updateTextView(String newText) {
        if (texto_monitor_serial != null) {
            texto_monitor_serial.setText(newText);
            Log.d("MainActivity", "ejecucion_funcion_cambiar_texto");
        }
    }
}
