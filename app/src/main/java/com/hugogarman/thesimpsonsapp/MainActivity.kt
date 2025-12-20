package com.hugogarman.thesimpsonsapp

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hugogarman.thesimpsonsapp.core.presentation.ext.hide
import com.hugogarman.thesimpsonsapp.core.presentation.ext.visible
import com.hugogarman.thesimpsonsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        binding.mainMenu.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.simpsonsListFragment,
                R.id.episodesFragment,
                R.id.quizFragment,
                R.id.settingsFragment -> showMenu()

                else -> hideMenu()
            }
        }
    }

    private fun showMenu() {
        if (binding.mainMenu.visibility != View.VISIBLE) {
            binding.mainMenu.visible()
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_menu)
            binding.mainMenu.startAnimation(animation)
        }
    }

    private fun hideMenu() {
        if (binding.mainMenu.visibility == View.VISIBLE) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_menu)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    binding.mainMenu.hide()
                }
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
            })
            binding.mainMenu.startAnimation(animation)
        }
    }
}