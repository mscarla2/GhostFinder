package com.mscarla2.ghostfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GhostFinder)
        title = "Ghost Finder";
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, navHostFragment.navController)
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.nav_fragment).navigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.info_menuItem -> {
                navHostFragment.navController.navigate(R.id.action_mainFragment_to_infoFragment)
                true
            }
            R.id.artist_menuItem -> {
                navHostFragment.navController.navigate(R.id.action_mainFragment_to_artistFragment)
                true
            }
            R.id.settings_menuItem -> {
                navHostFragment.navController.navigate(R.id.action_mainFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}