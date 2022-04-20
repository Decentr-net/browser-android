package net.decentr.module_decentr.presentation.login

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dagger.android.AndroidInjection
import net.decentr.module_decentr.NavDecentrDirections
import net.decentr.module_decentr.R
import net.decentr.module_decentr.databinding.ActivityLoginBinding
import net.decentr.module_decentr.presentation.base.NavHostActivity
import net.decentr.module_decentr.presentation.extensions.setNavigationIcon

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val navHost by lazy {
        supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
    }
    private lateinit var navigationToolbar: Toolbar
    private var isToolbarInflated = false

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        try {
            AndroidInjection.inject(this)
//                supportFragmentManager.fragmentFactory = fragmentFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)

        navigateToHome()
    }

    @Suppress("SpreadOperator")
    fun setupNavigationToolbar(vararg topLevelDestinationIds: Int) {
        NavigationUI.setupWithNavController(
            navigationToolbar,
            navHost.navController,
            AppBarConfiguration.Builder(*topLevelDestinationIds).build()
        )

        navigationToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun navigateToHome() {
        navHost.navController.navigate(NavDecentrDirections.actionStartupLogin())
    }
}