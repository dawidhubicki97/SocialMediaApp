package com.example.socialmediaapp.model

import com.example.socialmediaapp.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.searchrow.view.*

class SearchItem(val user: User?): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.searchrow
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.searchTextViewItem.text=user!!.username
    }
}