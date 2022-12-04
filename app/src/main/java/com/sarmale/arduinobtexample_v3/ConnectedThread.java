package com.sarmale.arduinobtexample_v3;


import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ConnectedThread extends Thread {
    //private static final String TAG = "MY_APP_DEBUG_TAG";
    // handler that gets info from Bluetooth service
    private static final String TAG = "FrugalLogs";
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer; // mmBuffer store for the stream
    private final static int MESSAGE_READ = 0;
    private String valueRead;

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public String getValueRead(){
        return valueRead;
    }

    public void run() {
        mmBuffer = new byte[1024];
        byte[] buffer = new byte[1024];
        int bytes = 0; // bytes returned from read()
        int numberOfReadings = 0; //to control the number of readinds from the Arduino

        // Keep listening to the InputStream until an exception occurs.

        //We just want to gt 3 temperatures readings and then return it to the UI

        while (numberOfReadings < 1) {
            try {

                buffer[bytes] = (byte) mmInStream.read();
                String readMessage;
                if (buffer[bytes] == '\n') {
                    readMessage = new String(buffer, 0, bytes);
                    Log.e(TAG, readMessage);
                    //Value to be read by the
                    valueRead=readMessage;
                    bytes = 0;
                    numberOfReadings++;
                } else {
                    bytes++;
                }


            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }

    }



    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}