package com.example.socialmediaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediaapp.R
import com.example.socialmediaapp.model.Comment
import com.example.socialmediaapp.model.CommentItem
import com.example.socialmediaapp.model.ImageDb
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_specific_photo.*

class SpecificPhotoActivity : AppCompatActivity() {
    val uid = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_photo)
        var imageobject=intent.getParcelableExtra<ImageDb>("Key")

        Picasso.get().load(imageobject.url).into(specificPhotoImageView)
        descirptionOfPhotoTextView.text=imageobject.description
        loadComments()
        addComment(imageobject)
        addLike(imageobject)
    }
    fun loadComments(){
        val ref = intent.getStringExtra("ref")
        val reference=FirebaseDatabase.getInstance().getReferenceFromUrl(ref)
        commentsPhotoRecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=GroupAdapter<ViewHolder>()

        reference.child("comments").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val comment=it.getValue(Comment::class.java)
                    if(comment!=null) {
                        Log.d("komenciki",comment.comment)
                        adapter.add(CommentItem(comment))
                    }
                }
                commentsPhotoRecyclerView.adapter=adapter
            }


        })
        adapter.setOnItemClickListener { item, view ->
            val commentitem =item as CommentItem
            val intent = Intent(this, appActivity::class.java)
            intent.putExtra("str",commentitem.comment.uid)

            startActivity(intent)

        }
    }
    fun addComment(imageobject:ImageDb){
        val ref = intent.getStringExtra("ref")

        val reference=FirebaseDatabase.getInstance().getReferenceFromUrl(ref)
        Log.d("sprawdzmy",reference.toString())
        addCommentButton.setOnClickListener {
            val textfromfield=commentText.text
            val comment=Comment(uid,textfromfield.toString())
            reference.child("comments").push().setValue(comment)
            loadComments()
        }


    }
    fun addLike(imageobject:ImageDb){
        val ref = intent.getStringExtra("ref")
        val reference=FirebaseDatabase.getInstance().getReferenceFromUrl(ref)
        val referencechild=reference.child("likes")
        var likesnumbernumber=0
        var isliked=false
        referencechild.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
               p0.children.forEach {
                   val refuid=it.getValue()
                   Log.d("najpierw",refuid.toString())
                   likesnumbernumber++
                   if(refuid==FirebaseAuth.getInstance().uid)
                   {
                       Picasso.get().load(R.drawable.icons8love502).fit().into(likeImageView)
                       isliked=true
                   }
                   likesNumberTextView.text=likesnumbernumber.toString()+" likes"

               }
            }

        }

        )
        likeImageView.setOnClickListener {
            if(isliked==false) {
                reference.child("likes/${FirebaseAuth.getInstance().uid}")
                    .setValue(FirebaseAuth.getInstance().currentUser!!.uid)
                Picasso.get().load(R.drawable.icons8love502).fit().into(likeImageView)
                addLike(imageobject)
            }
            else{
                reference.child("likes/${FirebaseAuth.getInstance().uid}").removeValue()
                Picasso.get().load(R.drawable.icons8love50).fit().into(likeImageView)
                isliked = false
                addLike(imageobject)

            }
        }
    }


}
