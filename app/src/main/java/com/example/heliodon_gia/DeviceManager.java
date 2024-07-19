package com.example.heliodon_gia;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import java.util.ArrayList;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.hardware.usb.UsbManager;
import com.hoho.android.usbserial.driver.UsbSerialProber;

public class DeviceManager {
    private static final String TAG = "MainActivity";;

    DeviceManager(Context context){
        this.context = context;
    }
    private final ArrayList<ListItem> listItems = new ArrayList<>();
    private Context context;
    public ListItem primer_dispo;
    public int baudRate = 9600;
    public boolean withIoManager = true;
    static class ListItem {
        UsbDevice device;
        int port;
        UsbSerialDriver driver;

        ListItem(UsbDevice device, int port, UsbSerialDriver driver) {
            this.device = device;
            this.port = port;
            this.driver = driver;
        }
        private final ArrayList<ListItem> listItems = new ArrayList<>();
        private ArrayAdapter<ListItem> listAdapter;


    }
    public void onResume() {
        refresh();
    }
    void refresh() {
        Log.d(TAG, "ejecutando refresh");
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        UsbSerialProber usbCustomProber = CustomProber.getCustomProber();
        listItems.clear();
        for(UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if(driver == null) {
                driver = usbCustomProber.probeDevice(device);
            }
            if(driver != null) {
                for(int port = 0; port < driver.getPorts().size(); port++)
                    listItems.add(new ListItem(device, port, driver));
                primer_dispo = listItems.get(0);
                Log.d(TAG, "hola,encontre_algo");
            } else {
                listItems.add(new ListItem(device, 0, null));
            }
        }
    }
}
