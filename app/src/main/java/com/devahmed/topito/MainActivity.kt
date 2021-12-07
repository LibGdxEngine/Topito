package com.devahmed.topito

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.devahmed.topito.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var navView: NavigationView
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        drawerLayout = binding.drawerLayout

        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_search, R.id.nav_about_us), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_search -> {
                    supportActionBar?.hide() // to hide
                    navView.visibility = View.GONE
                }
                R.id.placeDetails -> {
                    supportActionBar?.hide() // to hide
                    navView.visibility = View.GONE
                }
                else -> {
                    supportActionBar?.show() // to show
                    navView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searchIcon) {
            findNavController(R.id.nav_host_fragment_content_main)
                .navigate(
                    R.id.action_nav_home_to_nav_search,
                )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        val id = item.itemId
        when (id) {
            R.id.nav_home -> {
                findNavController(R.id.nav_host_fragment_content_main)
                    .navigate(R.id.nav_home)
            }
            R.id.nav_search -> {
                findNavController(R.id.nav_host_fragment_content_main)
                    .navigate(
                        R.id.nav_search,
                    )

            }
            R.id.RateApp -> {
                val uri: Uri = Uri.parse("market://details?id=$packageName")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
                }
            }
            R.id.shareApp -> {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBody =
                    "Application Link : https://play.google.com/store/apps/details?id=${applicationContext.getPackageName()}"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "App link")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Share App Link Via :"))
            }
            R.id.contactUs -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "libgdxengine@gmail.com"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail from Topito user")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi Topito developers,")
                //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

                //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text
                startActivity(Intent.createChooser(emailIntent, "Contact Topito developers"))
            }
            R.id.nav_about_us -> {
                findNavController(R.id.nav_host_fragment_content_main)
                    .navigate(R.id.nav_about_us)
            }
        }
        drawerLayout.close()
        return false
    }

}
