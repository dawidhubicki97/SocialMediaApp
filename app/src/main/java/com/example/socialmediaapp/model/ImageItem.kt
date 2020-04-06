package com.example.socialmediaapp.model

import android.content.Intent
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import com.example.socialmediaapp.R
import com.example.socialmediaapp.activities.SpecificPhotoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.image_row.view.*

class ImageItem(val imagedb: ImageDb, val ref:DatabaseReference): Item<ViewHolder>(),View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {


    override fun onMenuItemClick(p0: MenuItem?): Boolean {

        when(p0!!.itemId){
            0-> {
                val uid = FirebaseAuth.getInstance().uid ?: ""
                val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/avatarurl")
                ref.setValue(imagedb.url)
            }
            1->{
                ref.removeValue()
            }
        }

        return true
    }


    override fun onCreateContextMenu(p0: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val userphotokey=ref.parent!!.parent!!.key
        if(userphotokey==uid) {
        val profilepicmenuitem= p0!!.add(0, 0, 0, "Set as Profile Photo")
            val deletepic= p0!!.add(0,1,1,"Delete pic")
        profilepicmenuitem.setOnMenuItemClickListener(this)
            deletepic.setOnMenuItemClickListener(this)
        }

    }


    override fun getLayout(): Int {
        return R.layout.image_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val uri=imagedb.url
        viewHolder.itemView.imageViewRows.setOnClickListener {

            val intent= Intent(viewHolder.itemView.context, SpecificPhotoActivity::class.java)
            intent.putExtra("Key",imagedb)
            intent.putExtra("ref",ref.toString())

            viewHolder.itemView.context.startActivity(intent)

        }

        Picasso.get().load(uri).fit().into(viewHolder.itemView.imageViewRows)

        viewHolder.itemView.imageViewRows.setOnCreateContextMenuListener(this)
    }
}