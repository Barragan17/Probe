package com.example.probe.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.probe.R
import com.example.probe.firebase.FirestoreClass
import com.example.probe.models.User
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupActionBar()

        FirestoreClass().loadUserData(this)

        btn_edit.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    private fun setupActionBar(){

        setSupportActionBar(toolbar_my_profile_activity)

        val actionbar = supportActionBar
        if(actionbar!= null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_back_ios)
            actionbar.title = "My Profile"
        }

        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }

    }

    fun setUserDataInUI(user: User){
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_user_image)

        et_name.setText(user.name)
        et_email.setText(user.email)
        if(user.mobile != 0L){
            et_mobile.setText(user.mobile.toString())
        }
    }

}