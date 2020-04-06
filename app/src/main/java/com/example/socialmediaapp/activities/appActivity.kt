package com.example.socialmediaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.socialmediaapp.Fragments.HomeFragment
import com.example.socialmediaapp.Fragments.ProfileFragment
import com.example.socialmediaapp.Fragments.SearchFragment
import com.example.socialmediaapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_app.*


class appActivity : AppCompatActivity() {
    val uid = FirebaseAuth.getInstance().uid
    override fun onBackPressed() {
        return
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        val profilepassedid=intent.getStringExtra("str")
        if(profilepassedid!=null){
            Log.d("acotusiedzieje",profilepassedid)
            val frag=ProfileFragment()
            val bundle=Bundle()
            bundle.putString("str",profilepassedid)
            frag.arguments=bundle
            replaceFragment(frag)
        }
        else {
            replaceFragment(HomeFragment())
        }
        bottomNav.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_profile ->{
                    val frag=ProfileFragment()
                    val bundle=Bundle()
                    bundle.putString("str",null)
                    frag.arguments=bundle
                    replaceFragment(frag)
                }
                R.id.nav_search->replaceFragment(SearchFragment())
            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}











