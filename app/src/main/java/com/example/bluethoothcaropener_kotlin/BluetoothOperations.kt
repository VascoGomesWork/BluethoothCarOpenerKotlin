package com.example.bluethoothcaropener_kotlin

import android.bluetooth.BluetoothSocket
import java.io.IOException

class BluetoothOperations {

    /**
     * Resume: Constructor
     */
    init {

    }

    /**
     * Resume: Communicates with Arduino
     */
    fun communicateWithArduino(blutoothSocket: BluetoothSocket?) {
        //How to Send data to Arduino through OutputStream -> https://youtu.be/mHbHJsbgCNU
        if(blutoothSocket != null){
            try {
                blutoothSocket!!.outputStream.write("4+3".toByteArray())
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

}