package com.example.socialmediaapp.model

import com.example.socialmediaapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commentsunderphoto.view.*


import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class CommentItem(val comment:Comment): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.commentsunderphoto
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val ref = FirebaseDatabase.getInstance().getReference("users/${comment.uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                 val user= p0.getValue(User::class.java)!!
                if(user!!.avatarurl.isNotEmpty()){
                    Picasso.get().load(user.avatarurl).fit().into(viewHolder.itemView.imageViewComment)
                }
                else{
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/pseudoinstagram-db4c7.appspot.com/o/images%2Fdefault-person.png?alt=media&token=a14541d4-994f-4468-8d4e-0ab60b9e736a").fit().into(viewHolder.itemView.imageViewComment)
                }
                viewHolder.itemView.userCommentText.text=user.username
                viewHolder.itemView.actualCommentText.text=comment.comment
            }

        })

    }
}