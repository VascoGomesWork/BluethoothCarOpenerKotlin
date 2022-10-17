package com.example.bluethoothcaropener_kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bluethoothcaropener_kotlin.databinding.DeviceAvailableItemBinding
import com.example.bluethoothcaropener_kotlin.databinding.FragmentMainBinding
import java.io.IOException
import java.util.UUID


class MainFragment : Fragment() {

    private val REQUEST_ENABLE_BT: Int = 0

    //Failed to Initialize Editor -> https://youtu.be/VhQ2LFT4IG8
    private lateinit var binding: FragmentMainBinding

    private val adapter: MainFragment.DevicesAdapter = DevicesAdapter()

    private lateinit var bluetoothOperations: BluetoothOperations
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private var bluetoothPairedDevices: MutableList<String> = mutableListOf()

    //VIDEO
    companion object{
        private var myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private var blutoothSocket: BluetoothSocket? = null
        private var isConnected: Boolean = false
        private lateinit var address: String
        private lateinit var progress: ProgressDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater).let {
        this.binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bluetoothOperations = BluetoothOperations()
        //Fufills the adapter
        binding.availableDevicesRecyclerView.adapter = adapter


        //How to use Bluetooth in Kotlin -> https://youtu.be/PtN6UTIu7yw
        //Init Bluetooth Adapter

        //Turns Bluetooth ON
        turnOnBluetooth()

        binding.showCarsAvailableBtn.setOnClickListener(){
            //TODO - Pass to Another Fragment
            findNavController().navigate(R.id.action_mainFragment_to_carChooserFragment)
        }

    }

    private fun MainFragment.turnOnBluetooth() {
        binding.bluetoothSwitch.setOnCheckedChangeListener { _, isChecked ->

            //Bluetooth Adapter Initialization
            val bluetoothAdapter = initializeBluetoothAdapter()

            //Check if Device Supports Bluetooth
            checkDeviceSupportsBluetooth(bluetoothAdapter)

            //Enables or Disables Bluetooth Switch
            enablesDisablesBluetoothSwith(isChecked, bluetoothAdapter)
        }
    }

    private fun MainFragment.enablesDisablesBluetoothSwith(isChecked: Boolean, bluetoothAdapter: BluetoothAdapter?) {
        if (isChecked) {
            //Checks if Bluetooth is NOT Enabled
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

                if (ActivityCompat.checkSelfPermission(
                        super.requireContext(),
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // How to Request Missing Permissions -> https://www.tutorialspoint.com/how-to-request-location-permission-at-runtime-on-kotlin
                    requestMissingPermissions()
                    println("TESTE")
                }
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

                //If is Not Enable, Then Enables It
                Toast.makeText(super.requireContext(), "ON", Toast.LENGTH_LONG).show()
                println("ON")
            } else {
                //If it is ON
                Toast.makeText(super.requireContext(), "ALREADY ON", Toast.LENGTH_LONG).show()
                println("Already ON")
            }

            //Find Bluetooth Paired Devices -> https://developer.android.com/guide/topics/connectivity/bluetooth/find-bluetooth-devices
            //Shows the available Bluetooth Devices in an ArrayList
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address
                println("Device = " + deviceName + " MAC Address = " + deviceHardwareAddress)

                //Adds the devices to Adapter
                bluetoothPairedDevices.add(deviceName + " -> " + deviceHardwareAddress)
            }

            //Fuffills the devices list adapter
            adapter.devicesList = bluetoothPairedDevices
            //TODO - Fix Bug Paired Devices Appear When Bluetooth is already ON
            //Changes the Adapter
            adapter.notifyDataSetChanged()

            //Then Connects to Bluetooth Device and Sends Data to Arduino

        } else {
            //If Adapter is Enable Disables It
            if (bluetoothAdapter?.isEnabled == true) {
                Toast.makeText(super.requireContext(), "OFF", Toast.LENGTH_LONG).show()
                bluetoothAdapter.disable()
                println("OFF")
            }

        }
    }

    private fun checkDeviceSupportsBluetooth(bluetoothAdapter: BluetoothAdapter?) {
        if (bluetoothAdapter == null) {
            Toast.makeText(
                super.requireContext(),
                "Your Device Does Not Support Bluetooth!!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initializeBluetoothAdapter(): BluetoothAdapter {
        //Bluetooth Adapter Initialization
        val bluetoothManager =
            getSystemService(super.requireContext(), BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager!!.getAdapter()
        return bluetoothAdapter
    }

    inner class DevicesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = DeviceAvailableItemBinding.bind(view)
        init {
            binding.root.setOnClickListener{
                println("Clicked")

                // Gets the MAC Address to the address variable initializing it
                //TODO - Fix Temporary Fix
                //println("Address = " + adapter.devicesList.get(3).substring(adapter.devicesList.get(3).indexOf("> ") + 2))
                address = adapter.devicesList.get(3).substring(adapter.devicesList.get(3).indexOf("> ") + 2)
                //Connect to Bluetooth and Send Data -> https://youtu.be/eg-t_rhDSoM

                //How to Get Context -> https://stackoverflow.com/questions/32136973/how-to-get-a-context-in-a-recycler-view-adapter
                //Get it from any View object. In your case this might work for you
                ConnectToDevice(view.context).execute()

                //TODO - Light UP an LED for 2 seconds 10 times
                bluetoothOperations.communicateWithArduino(blutoothSocket)
            }
        }

        fun bind(device: String) {
            for (i in adapter.devicesList) {
                binding.deviceName.text = device
            }
        }
    }

    inner class DevicesAdapter : RecyclerView.Adapter<DevicesViewHolder>() {

        var devicesList: MutableList<String> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesViewHolder {
            val v: View = LayoutInflater.from(parent.context).inflate(R.layout.device_available_item, parent, false)
            return  DevicesViewHolder(v)
        }

        override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) {
            val device = devicesList[position]
            holder.bind(device)
        }

        override fun getItemCount() = devicesList.size
    }

    //Connect to Bluetooth and Send Data -> https://youtu.be/eg-t_rhDSoM
    inner class ConnectToDevice(c:Context) : AsyncTask<Void, Void, String>() {
        private var connectSucess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progress = ProgressDialog.show(context, "Connecting", "Please Wait")
        }

        @SuppressLint("MissingPermission")
        override fun doInBackground(vararg p0: Void?): String? {
            try{
                if(blutoothSocket == null || !isConnected){
                    //Initializes Bluetooth Adapter
                    bluetoothAdapter = initializeBluetoothAdapter()
                    println("Bluetooth Adapter = " + bluetoothAdapter)
                    println("ADDRESS = " + address)
                    val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
                    //if (bluetoothSocketPermissions()) return
                    blutoothSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    blutoothSocket!!.connect()
                }
            } catch (e: IOException){
                connectSucess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(!connectSucess){
                Toast.makeText(context, "Couldn't Connect to the Device", Toast.LENGTH_LONG).show()
            } else {
                isConnected = true
            }
            progress.dismiss()
        }
    }

    /**
     * Resume: How to Request Missing Permissions
     */
    private fun requestMissingPermissions() {
        // How to Request Missing Permissions -> https://www.tutorialspoint.com/how-to-request-location-permission-at-runtime-on-kotlin
        // here to request the missing permissions, and then overriding
        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            when (requestCode) {
                1 -> {
                    if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        if ((ContextCompat.checkSelfPermission(
                                super.requireContext(),
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) ===
                                    PackageManager.PERMISSION_GRANTED)
                        ) {
                            Toast.makeText(super.requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(super.requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                    return
                }
            }
        }
    }

}