package com.avanade.starwarsbio

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent?.handleIntent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.handleIntent()
    }

    private fun handleDeepLink(data: Uri?){
        when(data?.path){
            DeepLink.BIO -> {
                var character = data.getQueryParameter(DeepLink.Params.CHARACTER).orEmpty()
                character = character.lowercase()

                when(character){
                    StartWars.LUKE -> updateView(LukeFragment::class.java)
                    StartWars.DARTH_MAUL, "darth maul" -> updateView(DarthMaulFragment::class.java)
                    StartWars.DARTH_VADER, "darth vader" -> updateView(DarthVaderFragment::class.java)
                    StartWars.OBI_WAN, "obi wan" -> updateView(ObiWanFragment::class.java)
                    StartWars.YODA -> updateView(YodaFragment::class.java)
                    else -> showDefaultView()
                }
            }
            else -> showDefaultView()
        }
    }

    private fun Intent.handleIntent(){
        when(action){
            Intent.ACTION_VIEW -> handleDeepLink(data)
            else -> showDefaultView()
        }
    }

    private fun showDefaultView(){
        updateView(LukeFragment::class.java)
    }

    private fun updateView(newFragment: Class<out Fragment>, arguments: Bundle? = null, toBackStack: Boolean = false){
        val currentFragment = supportFragmentManager.fragments.firstOrNull()
        if(currentFragment != null && currentFragment == newFragment){
            return
        }

        val fragment = supportFragmentManager.fragmentFactory.instantiate(newFragment.classLoader!!, newFragment.name)
        fragment.arguments = arguments

        supportFragmentManager.beginTransaction().run {
            replace(R.id.fitActivityContainer, fragment)
            if(toBackStack){
                addToBackStack(null)
            }
            commit()
        }
    }
}