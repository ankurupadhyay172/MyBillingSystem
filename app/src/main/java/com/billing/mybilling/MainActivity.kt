package com.billing.mybilling

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.billing.mybilling.base.BaseActivity
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.database.DatabaseManager
import com.billing.mybilling.presentation.HomeViewModel
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.ActiveStatus
import com.billing.mybilling.utils.PageConfiguration
import com.billing.mybilling.utils.showForceWarningDialog
import com.billing.mybilling.utils.showWarningDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    lateinit var toolBar2: Toolbar
    private lateinit var navigationController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var databaseManager: DatabaseManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar2 = findViewById(R.id.toolBar2)
        setSupportActionBar(toolBar2)
        EventBus.getDefault().register(this)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navigationController = navHost.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.pendingOrders))
        setupActionBarWithNavController(navigationController,appBarConfiguration)
        lifecycleScope.launchWhenResumed {
            navigationController.addOnDestinationChangedListener{_,destination,_->
                onDestinationChange(destination)
            }
        }

        requestPermission()


    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
        R.id.logout->{
            sessionManager.deleteUser()
            databaseManager.clearPendingOrders()
            while (navigationController.navigateUp()){navigationController.popBackStack()}
            unSubscribeToTopic()
            Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
            navigationController.navigate(R.id.loginFragment)


        }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onDestinationChange(destination: NavDestination){
        val config = PageConfiguration.getConfiguration(destination.id)
        toolBar2.isVisible = config.toolbarVisible

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()||navigationController.navigateUp()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

        }

    override fun onDestroy() {
        super.onDestroy()
        try {
            EventBus.getDefault().unregister(this)
        } catch (e: Exception) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RemoteMessage?) {
        val pendingIntent =
            navigationController.createDeepLink().setDestination(R.id.homeFragment)
                .createPendingIntent()
        Log.d("mylog", "onMessageEvent: message received")
        setNotification(event?.data?.get("title"),event?.data?.get("body"),pendingIntent)
    }

}