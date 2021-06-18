package com.tic_tac_toe.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tic_tac_toe.view.TwoPlayerActivityBluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Created by Jay on 12/10/2015.
 */
public class BluetoothGameService {

    private static final String TAG = "BluetoothGameService";
    private static final boolean D = true;

    private static final String mName = "Tic-Hac-Toe";

    private static final UUID mUUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private final BluetoothAdapter mBluetoothAdapter;
    private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private String marker = "";

    private int mState;

    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_DISCONNECTED = 4;
    public static final int STATE_NEW_GAME = 5;

    public BluetoothGameService(Context mContext, Handler handler){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
    }

    private synchronized void setState(int state){
        if(D){
            Log.d(TAG, "setState() " + mState + " -> " + state);
        }
        mState = state;
        mHandler.obtainMessage(TwoPlayerActivityBluetooth.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public synchronized int getState(){
        return mState;
    }

    public synchronized void start(){
        if(D) Log.d(TAG, "start");

        if(mConnectThread != null){mConnectThread.cancel(); mConnectThread = null;}
        if(mConnectedThread != null){mConnectedThread.cancel(); mConnectedThread = null;}

        if(mAcceptThread == null){
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }

        setState(STATE_LISTEN);
    }

    public void setNewGame(){
        if(mState == STATE_CONNECTED) {
            setState(STATE_NEW_GAME);
        }
        setState(STATE_CONNECTED);
    }

    public synchronized void connect(BluetoothDevice device, String marker) {

        this.marker = marker;
        if (D) {Log.d(TAG, "connect to: " + device);}

        if(mState == STATE_CONNECTING){
            if(mConnectThread != null){mConnectThread.cancel(); mConnectThread = null;}
        }

        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);

    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "connected");
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(TwoPlayerActivityBluetooth.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(TwoPlayerActivityBluetooth.DEVICE_NAME, device.getName());
        bundle.putString(TwoPlayerActivityBluetooth.MARKER, marker);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(STATE_CONNECTED);
    }

    public synchronized void stop(){
        if(D) Log.d(TAG, "stop");
        if(mConnectThread != null){mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
        setState(STATE_NONE);
    }

    public void writeLocation(byte[] out){
        ConnectedThread r;
        synchronized (this){
            if(mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        r.writeLocation(out);
    }

   private void connectionFailed() {
       setState(STATE_LISTEN);

        // Send a failure message back to the Activity
       Message msg = mHandler.obtainMessage(TwoPlayerActivityBluetooth.MESSAGE_TOAST);
       Bundle bundle = new Bundle();
       bundle.putString(TwoPlayerActivityBluetooth.TOAST, "Unable to connect to device");
       msg.setData(bundle);
       mHandler.sendMessage(msg);


    }

    private void connectionLost() {
        setState(STATE_DISCONNECTED);
        setState(STATE_LISTEN);
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(TwoPlayerActivityBluetooth.MESSAGE_DISCONNECTED);
        Bundle bundle = new Bundle();
        bundle.putString(TwoPlayerActivityBluetooth.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

    }


    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mServerSocket;


        public AcceptThread(){
            BluetoothServerSocket temp = null;
            try{
                temp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(mName, mUUID);
            }catch (IOException e){
                Log.e(TAG, "listen() failed", e);
            }


            mServerSocket = temp;
        }

        public void run(){
            if(D) Log.d(TAG, "BEGIN mAcceptThread" + this);
            setName("AcceptThread");

            BluetoothSocket socket = null;

            while(mState != STATE_CONNECTED){
                try{
                    socket = mServerSocket.accept();
                }catch(IOException e){
                    Log.e(TAG, "accept() failed", e);
                    break;
                }


                if(socket != null){
                    synchronized (BluetoothGameService.this){
                        switch (mState){
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                try{
                                    socket.close();
                                }catch(IOException e){
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            if(D) Log.i(TAG, "End mAcceptThread");
        }

        public void cancel(){
            if(D) Log.d(TAG, "cancel " + this);
            try{
                mServerSocket.close();
            }catch(IOException e){
                Log.e(TAG, "close() of server failed", e);
            }
        }
    }

    private class ConnectThread extends Thread{

        private final BluetoothSocket mSocket;
        private final BluetoothDevice mDevice;

        public ConnectThread(BluetoothDevice device){

            BluetoothSocket temp = null;
            mDevice = device;

            try{
                temp = device.createRfcommSocketToServiceRecord(mUUID);
            }catch(IOException e){
                Log.e(TAG, "create() failed", e);
            }
            mSocket = temp;
        }

        public void run(){
            Log.i(TAG, "BEGIN mConnectTread");
            setName("ConnectThread");

            mBluetoothAdapter.cancelDiscovery();

            try{
                mSocket.connect();
            }catch(IOException e){
                connectionFailed();

                try{
                    mSocket.close();
                }catch(IOException closeException){
                    Log.e(TAG, "unable to close() socket during connection failure", closeException);
                }
                BluetoothGameService.this.start();
                return;
            }

            synchronized (BluetoothGameService.this){
                mConnectThread = null;
            }

            connected(mSocket, mDevice);

        }

        public void cancel(){
            try{
                mSocket.close();
            }catch(IOException e){
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }

    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mSocket;
        private final InputStream mInStream;
        private final OutputStream mOutStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG, "create ConnectedThread");

            mSocket = socket;
            InputStream tempInput = null;
            OutputStream tempOutput = null;

            try{
                tempInput = socket.getInputStream();
                tempOutput = socket.getOutputStream();
            }catch(IOException e){
                Log.e(TAG, "temp sockets not created", e);
            }

            mInStream = tempInput;
            mOutStream = tempOutput;
        }

        public void run(){
            Log.i(TAG, "BEGIN mConnectedThread");
            /*byte[] buffer = new byte[1024];
            int bytes;

            while(true){
                try{
                    bytes = mInStream.read(buffer);
                    mHandler.obtainMessage(TwoPlayerActivityBluetooth.PLAYER_READ, bytes, -1, buffer).sendToTarget();
                }catch(IOException e){
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }*/

            readLocation();
        }

        public void readLocation(){

            byte[] buffer = new byte[1024];
            int bytes;

            while(true){
                try{
                    bytes = mInStream.read(buffer);
                    mHandler.obtainMessage(TwoPlayerActivityBluetooth.PLAYER_LOCATION_READ, bytes, -1, buffer).sendToTarget();
                }catch(IOException e){
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        public void writeLocation(byte[] buffer){
            try{
                mOutStream.write(buffer);
                mHandler.obtainMessage(TwoPlayerActivityBluetooth.PLAYER_LOCATION_WRITE, -1 , -1 , buffer).sendToTarget();
            }catch(IOException e){
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel(){
            try{
                mSocket.close();
            }catch(IOException e){
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
