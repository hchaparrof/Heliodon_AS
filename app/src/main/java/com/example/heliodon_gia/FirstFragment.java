package com.example.heliodon_gia;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import android.widget.TextView;

public class FirstFragment extends Fragment {
    private TextView texto_monitor_serial;
    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
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
