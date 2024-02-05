package com.billing.mybilling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.billing.mybilling.base.BaseActivity
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.PageConfiguration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    lateinit var toolBar2: Toolbar
    private lateinit var navigationController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar2 = findViewById(R.id.toolBar2)
        setSupportActionBar(toolBar2)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navigationController = navHost.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        setupActionBarWithNavController(navigationController,appBarConfiguration)
        lifecycleScope.launchWhenResumed {
            navigationController.addOnDestinationChangedListener{_,destination,_->
                onDestinationChange(destination)
            }
        }


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
            while (navigationController.navigateUp()){navigationController.popBackStack()}
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
}