package com.example.jiolauncher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.jiolauncher.adapter.AppListAdapter
import com.example.jiolauncher.aidl.DeviceRotationAidlInterface
import com.example.jiolauncher.aidl.RotationService
import com.example.jiolauncher.model.AppInfo
import com.example.jiolauncher.sdk.AppDetails
import java.util.*


class MainActivity : AppCompatActivity()/*, RotationService()*/ {

    private lateinit var radapter: AppListAdapter
    private var appList: MutableList<AppInfo>? = null
    private var appListTemp: MutableList<AppInfo>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        resetPreferredLauncherAndOpenChooser(this)
        //val client=RotationService.client
        appList =  AppDetails.getInstance(this).AppDetail()
        var recycler_app = findViewById<RecyclerView>(R.id.recycler_app)
         radapter = AppListAdapter()
        radapter.setAppList(appList)

        recycler_app.adapter=radapter
       val searchApp= findViewById<EditText>(R.id.search_app)
        searchApp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length == 0)
                    radapter.setAppList(appList)
                appListTemp = ArrayList()
                if (s.toString().length >=3) {
                    for (appInfo in (appList as MutableList<AppInfo>?)!!) {
                        if (appInfo.label.toString().toLowerCase().contains(
                                s.toString().toLowerCase()
                            )
                        ) {
                            (appListTemp as ArrayList<AppInfo>).add(appInfo)
                        }

                    }
                    radapter.setAppList(appListTemp)
                }

            }
        })

        connectToRemoteService()
    }


    fun resetPreferredLauncherAndOpenChooser(context: Context) {
        val packageManager: PackageManager = context.getPackageManager()
        val componentName =
            ComponentName(context, com.example.jiolauncher.MainActivity::class.java)
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val selector = Intent(Intent.ACTION_MAIN)
        selector.addCategory(Intent.CATEGORY_HOME)
        selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(selector)
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun connectToRemoteService() {
        val intent = Intent("aidlexample")
        /*val pack = RotationService::class.java.`package`
        pack?.let {
            intent.setPackage(pack.name)
            this?.applicationContext?.bindService(
                intent, this, Context.BIND_AUTO_CREATE
            )
        }*/

        if(calService==null){

            val serviceIntent = Intent(this, RotationService::class.java)
            this.startService(serviceIntent)
            //val it = Intent("rotationService")
          /*  val it = Intent("aidlexample")
            bindService(it, connection, BIND_AUTO_CREATE)*/
        }
    }

    protected var calService: DeviceRotationAidlInterface? = null

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            calService = DeviceRotationAidlInterface.Stub.asInterface(service)
            Toast.makeText(applicationContext, "Service Connected", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            calService = null
            Toast.makeText(applicationContext, "Service Disconnected", Toast.LENGTH_SHORT).show()
        }
    }


}