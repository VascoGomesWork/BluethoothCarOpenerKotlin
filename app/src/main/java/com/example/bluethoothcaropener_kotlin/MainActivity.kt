package com.example.bluethoothcaropener_kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bluethoothcaropener_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)
    }

    //TODO - Fazer Video Explicativo do Funcionamento da aplicação
    fun getActivityContext(): Context? {
        return applicationContext
    }
    /*private val REQUEST_ENABLE_BT: Int = 0

    //Failed to Initialize Editor -> https://youtu.be/VhQ2LFT4IG8
    private lateinit var binding: ActivityMainBinding
    private val adapter: CarsAdapter = CarsAdapter()

    private lateinit var bluetoothOperations: BluetoothOperations

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendDataBtn.setOnClickListener(){
            binding.showDataWritten.text = binding.writeData.text
        }

        //How to use Bluetooth in Kotlin -> https://youtu.be/PtN6UTIu7yw
        //Init Bluetooth Adapter
        //bAdapter = BluetoothAdapter.getDefaultAdapter()

        //Turns Bluetooth ON
        binding.bluetoothSwitch.setOnCheckedChangeListener { _, isChecked ->

            //Bluetooth Adapter
            val bluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter = bluetoothManager.getAdapter()
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Your Device Does Not Support Bluetooth!!", Toast.LENGTH_LONG).show()
            }

            if(isChecked) {
                //Checks if Bluetooth is NOT Enabled
                if (bluetoothAdapter?.isEnabled == false) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // How to Request Missing Permissions -> https://www.tutorialspoint.com/how-to-request-location-permission-at-runtime-on-kotlin
                        requestMissingPermissions()
                    }
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

                    //If is Not Enable, Then Enables It
                    Toast.makeText(this, "ON", Toast.LENGTH_LONG).show()
                    println("ON")
                } else {
                    //If it is ON
                    Toast.makeText(this, "ALREADY ON", Toast.LENGTH_LONG).show()
                    println("Already ON")
                }

                //Find Bluetooth Paired Devices -> https://developer.android.com/guide/topics/connectivity/bluetooth/find-bluetooth-devices
                //Shows the available Bluetooth Devices in an ArrayList
                val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                pairedDevices?.forEach { device ->
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    println("Device = " + deviceName + " MAC Address = " + deviceHardwareAddress)
                }

                //Then Connects to Bluetooth Device and Sends Data to Arduino

            } else {
                //If Adapter is Enable Disables It
                if(bluetoothAdapter?.isEnabled == true){
                    Toast.makeText(this, "OFF", Toast.LENGTH_LONG).show()
                    bluetoothAdapter.disable()
                    println("OFF")
                }

            }
        }
    }

    inner class CarsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //private lateinit var cars : Cars
        init {
            binding.root.setOnClickListener{
                println("Clicked")
            }
        }

        fun bind(car: String) {
            TODO("Not yet implemented")
        }

    }

    inner class CarsAdapter : RecyclerView.Adapter<CarsViewHolder>() {

        var carsList: MutableList<String> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
            val v: View = LayoutInflater.from(parent.context).inflate(R.layout.car_available_item, parent, false)
            return  CarsViewHolder(v)
        }

        override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
            val car = carsList[position]
            holder.bind(car)
        }

        override fun getItemCount() = carsList.size
    }

    inner class AcceptThread : Thread() {

        /*private val mmServerSocket : BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestMissingPermissions()
            }
            bluetoothAdapter?.listenUsingRfcommWithServiceRecord("HC-05", 00:21:13:00:5C:7A)
        }*/

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
                                this@MainActivity,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) ===
                                    PackageManager.PERMISSION_GRANTED)
                        ) {
                            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                    return
                }
            }
        }
    }*/
}