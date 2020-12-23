package com.example.probe.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.probe.R
import com.example.probe.firebase.FirestoreClass
import com.example.probe.models.User
import com.example.probe.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.iv_edit_image
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException
import java.util.jar.Manifest

class EditActivity : BaseActivity() {


    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mProfileImageURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        FirestoreClass().loadUserData(this)

        setUpActionBar()


        iv_edit_image.setOnClickListener {

            if(ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        btn_update.setOnClickListener {
            if(mSelectedImageFileUri != null){
                uploadUserImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))

                updateUserProfileData()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }
        }else{
            Toast.makeText(
                this,
            "Permission for storage is denied",
                Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null){
            mSelectedImageFileUri = data.data
            try{
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_edit_image)
            }catch (e: IOException){
                e.printStackTrace()
            }

        }
    }

    private fun setUpActionBar(){

        setSupportActionBar(toolbar_edit_profile_activity)

        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_back_ios)
            actionbar.title = "Edit Profile"
        }

        toolbar_edit_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun setDatainEdit(user: User){

        mUserDetails = user

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_edit_image)

        edit_name.setText(user.name)
        edit_email.setText(user.email)
        if(user.mobile != 0L){
            edit_mobile.setText(user.mobile.toString())
        }
    }

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()

        if(mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageURL
        }

        if(edit_name.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = edit_name.text.toString()
        }

        if(edit_mobile.text.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = edit_mobile.text.toString().toLong()
        }

        FirestoreClass().updateUserProfileData(this@EditActivity, userHashMap)
    }

    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        if(mSelectedImageFileUri != null){
            val sRef : StorageReference =
                FirebaseStorage.getInstance().reference.child(
                    "USER_IMAGE" + System.currentTimeMillis()
                            + "." + Constants.getFileExtension(this, mSelectedImageFileUri))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                taskSnapshot ->
                Log.e("Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.e("Downlaodable Image URL", uri.toString())
                    mProfileImageURL = uri.toString()

                    updateUserProfileData()
                }
            }.addOnFailureListener {
                exception ->
                Toast.makeText(this@EditActivity,
                "Error Cokkkkk", Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }
        }
    }


    fun profileUpdateSuccess(){
        hideProgressDialog()
        finish()
        startActivity(Intent(this, ProfileActivity::class.java))
    }
}