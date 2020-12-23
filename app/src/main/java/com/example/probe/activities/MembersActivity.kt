package com.example.probe.activities

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probe.R
import com.example.probe.adapters.MemberListItemAdapter
import com.example.probe.firebase.FirestoreClass
import com.example.probe.models.Board
import com.example.probe.models.User
import com.example.probe.utils.Constants
import kotlinx.android.synthetic.main.activity_members.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.dialog_add_members.*

class MembersActivity : BaseActivity() {

    private lateinit var mBoardDetails : Board
    private lateinit var mAssignedMembersList: ArrayList<User>
    private var anyChangeMade: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        if(intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAIL)!!
        }

        setupActionBar()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)

        fab_creating_members.setOnClickListener {
            dialogSearchMember()
        }
    }

    private fun setupActionBar(){

        setSupportActionBar(toolbar_members_activity)
        val actionbar = supportActionBar
        if(actionbar!= null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_back_ios)
            actionbar.title = resources.getString(R.string.members)
        }

        toolbar_members_activity.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onBackPressed() {
        if(anyChangeMade){
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    fun setupMemberList(list: ArrayList<User>){

        mAssignedMembersList = list
        hideProgressDialog()

        rv_members_list.layoutManager = LinearLayoutManager(this)
        rv_members_list.setHasFixedSize(true)

        val adapter = MemberListItemAdapter(this, list)
        rv_members_list.adapter = adapter
    }

    fun memberDetails(user: User){
        mBoardDetails.assignedTo.add(user.id)
        FirestoreClass().assignMemberToBoard(this, mBoardDetails, user)
    }

    private fun dialogSearchMember(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_members)
        dialog.tv_add.setOnClickListener {
            val email = dialog.et_email_search_member.text.toString()
            if(email.isNotEmpty()) {
                dialog.dismiss()
                // TODO
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getMemberDetails(this, email)
            }else {
                Toast.makeText(this, "Isi Goblok Jangan Dikosongin", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.tv_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun memberAssignedSuccess(user: User){
        hideProgressDialog()
        mAssignedMembersList.add(user)

        anyChangeMade = true

        setupMemberList(mAssignedMembersList)
    }
}