package com.example.heliodon_gia;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import android.os.Looper;
import android.hardware.usb.UsbDevice;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import android.hardware.usb.UsbDeviceConnection;
import android.os.Build;
import android.app.PendingIntent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import android.content.IntentFilter;
import java.io.IOException;
import androidx.core.content.ContextCompat;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.hoho.android.usbserial.util.HexDump;

public class SerialManager implements SerialInputOutputManager.Listener {
    private static final String TAG = "MainActivity_2";
    private final BroadcastReceiver broadcastReceiver;
//    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private StringBuilder messageBuffer = new StringBuilder();

    public void send(List<Coordenadas> puntosCoordenados) {
        // todo la comunicación con esto
    }


    private enum UsbPermission { Unknown, Requested, Granted, Denied }
    private static final String INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB";
    private UsbPermission usbPermission = UsbPermission.Unknown;
    private final Handler mainLooper;
    private Context context;
    private int deviceId, portNum, baudRate;
    private UsbSerialPort usbSerialPort;
    private boolean withIoManager;
    private SerialInputOutputManager usbIoManager;
    private boolean connected = false;
    private static final int WRITE_WAIT_MILLIS = 30;
    private static final int READ_WAIT_MILLIS = 2000;
    private static final String MESSAGE_DELIMITER = "\n";

    public SerialManager(Context context, int deviceId_arg, int portNum_arg, int baudRate_arg, boolean withIoManager_arg) {
        this.context = context;
        this.deviceId = deviceId_arg;
        this.portNum = portNum_arg;
        this.baudRate = baudRate_arg;
        this.withIoManager = withIoManager_arg;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(INTENT_ACTION_GRANT_USB.equals(intent.getAction())) {
                    usbPermission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            ? UsbPermission.Granted : UsbPermission.Denied;
                    connect();
                }
            }
        };
        mainLooper = new Handler(Looper.getMainLooper());
//        usb_init(); // todo probar si esto hace lo que yo pienso que hace ->
//         no, no hace eso, arruina algo pero no se que.
    }

    private void connect() {
        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        for(UsbDevice v : usbManager.getDeviceList().values())
            if(v.getDeviceId() == deviceId)
                device = v;
        if(device == null) {
            status("connection failed: device not found");
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if(driver == null) {
            status("connection failed: no driver for device");
            return;
        }
        if(driver.getPorts().size() < portNum) {
            status("connection failed: not enough ports at device");
            return;
        }
        usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if(usbConnection == null && usbPermission == UsbPermission.Unknown && !usbManager.hasPermission(driver.getDevice())) {
            usbPermission = UsbPermission.Requested;
            int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_MUTABLE : 0;
            Intent intent = new Intent(INTENT_ACTION_GRANT_USB);
            intent.setPackage(context.getPackageName());
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(context, 0, intent, flags);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if(usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice()))
                status("connection failed: permission denied");
            else
                status("connection failed: open failed");
            return;
        }

        try {
            usbSerialPort.open(usbConnection);
            try{
                usbSerialPort.setParameters(baudRate, 8, 1, UsbSerialPort.PARITY_NONE);
            }catch (UnsupportedOperationException e){
                status("unsupport setparameters");
            }
            if(withIoManager) {
                usbIoManager = new SerialInputOutputManager(usbSerialPort, this);
                usbIoManager.start();
            }
            status("connected");
            connected = true;
//            controlLines.start();
        } catch (Exception e) {
            status("connection failed: " + e.getMessage());
            disconnect();
        }
    }
    private void disconnect() {
        status("connection desconectada: ");
        connected = false;
//        controlLines.stop();
        if(usbIoManager != null) {
            usbIoManager.setListener(null);
            usbIoManager.stop();
        }
        usbIoManager = null;
        try {
            usbSerialPort.close();
        } catch (IOException ignored) {}
        usbSerialPort = null;
    }
//    public void usb_init() {
//        ContextCompat.registerReceiver(context, broadcastReceiver, new IntentFilter(INTENT_ACTION_GRANT_USB), ContextCompat.RECEIVER_NOT_EXPORTED);
//    }

    public void usb_close() {
        context.unregisterReceiver(broadcastReceiver);
    }

    public void usb_resume() {
        if(!connected && (usbPermission == UsbPermission.Unknown || usbPermission == UsbPermission.Granted))
            mainLooper.post(this::connect);
    }

    public void usb_pause() {
        if(connected) {
            status("disconnected");
            disconnect();
        }
    }
    public void status(String str){
        Log.d(TAG, str);
    }
    @Override
    public void onNewData(byte[] data) {
//        Log.d(TAG, "ejecutando_on_new_data" + "" + "");
        mainLooper.post(() -> {
            String receivedData = new String(data);
            messageBuffer.append(receivedData);
            int delimiterIndex;
            while ((delimiterIndex = messageBuffer.indexOf(MESSAGE_DELIMITER)) != -1) {
                String message = messageBuffer.substring(0, delimiterIndex);
                status(message);
                messageBuffer.delete(0, delimiterIndex + MESSAGE_DELIMITER.length());
            }
        });
    }

    @Override
    public void onRunError(Exception e) {
        mainLooper.post(() -> {
            status("connection lost: " + e.getMessage());
            disconnect();
        });
    }
    public void send(String str) {
        status("enviando_mensaje" + str);
        if(!connected) {
            status("not connected");
            return;
        }
        try {
            byte[] data = (str + '\n').getBytes();
            usbSerialPort.write(data, 0);//WRITE_WAIT_MILLIS); #todo, probar esto así a ver que pasa
        } catch (Exception e) {
            status("no se que paso: ");
            onRunError(e);
        }
    }

    private void read() {
        if(!connected) {
            status("not connected");
            return;
        }
        try {
            byte[] buffer = new byte[8192];
            int len = usbSerialPort.read(buffer, READ_WAIT_MILLIS);
            receive(Arrays.copyOf(buffer, len));
        } catch (IOException e) {
            // when using read with timeout, USB bulkTransfer returns -1 on timeout _and_ errors
            // like connection loss, so there is typically no exception thrown here on error
            status("connection lost: " + e.getMessage());
            disconnect();
        }
    }

    private void receive(byte[] data) {
        Log.d(TAG, "ejecutando_receive" );
        if(data.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(HexDump.dumpHexString(data));
            sb.append("\n");
            String decodedString = new String(data, StandardCharsets.UTF_8);
            status(decodedString);
            // la primera llegada es temperatura,
            //separada por un hola y luego humedad
            // chekear tamaño memoria
        }
    }

}
