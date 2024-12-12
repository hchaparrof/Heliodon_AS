package com.example.heliodon_gia;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, FirstFragment.OnButtonClickListener_1 {
    private static final String TAG = "MainActivity_2";
    public DeviceManager device_manager;
    public SerialManager serialManager;
    private ViewPager2 viewPager;

    private TabLayout tabLayout;

    private String ultima_llegada = "";
    private int ultimo_envio = -1;
    private boolean connected = false;
    public MainActivity(){


    }



    SerialManager crear_serial_m(DeviceManager dev_man){
        Log.d(TAG, "funcion_serial_manager_crear");
        return new SerialManager(this, dev_man.primer_dispo.device.getDeviceId(),dev_man.primer_dispo.port, dev_man.baudRate, dev_man.withIoManager);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            device_manager = new DeviceManager(this);
            manejar_onresume_device();
        }else {
            onBackStackChanged();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        viewPager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Pestaña 1");
                    } else {
                        tab.setText("Pestaña 2");
                    }
                }).attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment fragment = getCurrentFragment();
                if (fragment instanceof FirstFragment) {
                    Log.d(TAG, "FirstFragment encontrado_callback");
                    ((FirstFragment) fragment).updateTextView("Nuevo texto");
                } else if (fragment instanceof SecondFragment) {
                    Log.d(TAG, "SecondFragment encontrado_callback");
                } else {
                    Log.d(TAG, "Ningún fragmento encontrado_callback");
                }
            }
        });
        Fragment fragment_2 = getCurrentFragment();
        if (fragment_2 instanceof FirstFragment) {
            Log.d("MainActivity", "FirstFragment encontrado");
            ((FirstFragment) fragment_2).updateTextView("Nuevo texto");
        } else if (fragment_2 instanceof SecondFragment) {
            Log.d("MainActivity", "SecondFragment encontrado");
        }else {
            Log.d("MainActivity", "ningun fragmento encontrado");
        }
        FirstFragment fragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Log.d("MainActivity", "msg_antes");
        if (fragment != null) {
            Log.d("MainActivity", "msg_durante");
            fragment.updateTextView("Nuevo texto");
        }else{
            Log.d("MainActivity", "msg_despues");
        }
    }
    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentByTag("f" + viewPager.getCurrentItem());
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ejecutando_onresume");
        manejar_onresume_device();
        manejar_onresume_serial();
    }
    private void manejar_onresume_device(){
        device_manager.refresh();
        if (device_manager.primer_dispo != null){
            Log.d(TAG, "el dispositivo es no nulo");
            this.serialManager = crear_serial_m(this.device_manager);
        }
    }
    private void manejar_onresume_serial(){
        if (this.serialManager != null){
            Log.d(TAG, "serial_manager_no_nuelo");
            this.serialManager.usb_resume();
        }
    }
    @Override
    public void onPause() {
//        manejar_onpause_serial();
        super.onPause();
    }
    @Override
    public void onStop() {
//        manejar_onstop_serial();
        super.onStop();
    }
    private void manejar_onpause_serial(){
        if (this.serialManager != null){
            Log.d(TAG, "serial_manager_no_nulo, pausa");
            this.serialManager.usb_pause();
        }
    }
    //onStop
    private void manejar_onstop_serial(){
        if (this.serialManager != null){
            Log.d(TAG, "serial_manager_no_nulo, stop");
            this.serialManager.usb_close();
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "ejecutando onnewintent");
        if("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
            SerialManager ser_man = (SerialManager) this.serialManager;
            if (ser_man != null){
                ser_man.status("USB device detected");
            }else{
                manejar_onresume_device();
                Log.d(TAG, "ejecutando el else de onnewintent");
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }

    @Override
    public void onButton_1_Click(int a) {
        Log.d(TAG, "se oprimio un boton en main" + String.valueOf(a));
        if(this.serialManager != null) {
            this.serialManager.send(String.valueOf(a));
        }
    }
}
