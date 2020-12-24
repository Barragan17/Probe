package com.example.probe.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probe.R
import com.example.probe.adapters.MemberListItemAdapter
import com.example.probe.models.User
import kotlinx.android.synthetic.main.dialog_list.view.*

abstract class MemberListDialog(
    context: Context,
    private var list: ArrayList<User>,
    private val title: String = ""
): Dialog(context) {

    private var adapter: MemberListItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)
    }

    private fun setUpRecyclerView(view: View){
        view.tvTitle.text = title

        if(list.size > 0){
            view.rvList.layoutManager = LinearLayoutManager(context)
            adapter = MemberListItemAdapter(context, list)
            view.rvList.adapter = adapter

            adapter!!.setOnClickListener(object :
                MemberListItemAdapter.OnClickListener{
                override fun onClick(position: Int, user: User, action: String) {
                    dismiss()
                    onItemSelected(user, action)
                }
            })
        }
    }

    protected abstract fun onItemSelected(user: User, action: String)
}