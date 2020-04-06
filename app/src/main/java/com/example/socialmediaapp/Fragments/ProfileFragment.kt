package com.example.socialmediaapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.R
import com.example.socialmediaapp.model.ImageDb
import com.example.socialmediaapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import android.widget.Button
import com.example.socialmediaapp.activities.AddingPictureActivity
import com.example.socialmediaapp.model.ImageItem


class ProfileFragment: Fragment() {
    internal var root: View? = view

    var uid=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root=inflater.inflate(R.layout.fragment_profile, container, false)


            if(arguments!!.getString("str")!=null){
            uid= arguments!!.getString("str")
        }
        else {
            uid = FirebaseAuth.getInstance().uid!!
        }

        loadProfile()
        loadImages()

        return root


    }



    fun loadProfile(){

        val desciptionprofile=root!!.findViewById(R.id.profileDescriptionTextView) as TextView
        val addphotobutton=root!!.findViewById(R.id.addPhotoButton) as Button
        val imageviewavatar=root!!.findViewById(R.id.imageViewProfilePicture) as ImageView
        val followbutton=root!!.findViewById(R.id.followButton) as Button
        val followedtextview=root!!.findViewById(R.id.followingTextView) as TextView
        val followstextview=root!!.findViewById(R.id.followsTextView) as TextView
        val usernametextview=root!!.findViewById(R.id.usernameTextView) as TextView
        var followerscounter=0
        var followingcounter=0
        if(uid!=FirebaseAuth.getInstance().uid) {
        addphotobutton.visibility=View.INVISIBLE
        }
        else{
            followbutton.visibility=View.INVISIBLE
        }
            val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        addphotobutton.setOnClickListener {
            val intent = Intent(context, AddingPictureActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val profile = p0.getValue(User::class.java)
                if(profile!!.avatarurl.isNotEmpty()){
                Picasso.get().load(profile.avatarurl).fit().into(imageviewavatar)
                }
                else{
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/pseudoinstagram-db4c7.appspot.com/o/images%2Fdefault-person.png?alt=media&token=a14541d4-994f-4468-8d4e-0ab60b9e736a").fit().into(imageviewavatar)
                }

                desciptionprofile.text=profile!!.description
                usernametextview.text=profile!!.username
            }

        })
        val referencefollowers=FirebaseDatabase.getInstance().getReference("users/$uid/following")
        val referencefollowed=FirebaseDatabase.getInstance().getReference("users/$uid/followed")
        val currentuserid=FirebaseAuth.getInstance().uid
        var isfollowed=false
        referencefollowers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                followerscounter=p0.childrenCount.toInt()
                followedtextview.text="Following"+followerscounter

            }

        })
        referencefollowed.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                followingcounter=p0.childrenCount.toInt()
                followstextview.text="Followed"+followingcounter
                p0.children.forEach {
                    val userchecked=it.getValue()
                    if(userchecked==currentuserid)
                        isfollowed=true
                }
            }

        })
        if(isfollowed==true){
            followbutton.text="Unfollow"
        }
        else{
            followbutton.text="Follow"
        }

        followbutton.setOnClickListener {
            if(isfollowed==false) {
                val reference = FirebaseDatabase.getInstance().getReference("users/$uid/followed/${FirebaseAuth.getInstance().uid}")
                val secondreference = FirebaseDatabase.getInstance().getReference("users/${FirebaseAuth.getInstance().uid}/following/$uid")
                reference.setValue(FirebaseAuth.getInstance().uid)
                secondreference.setValue(uid)
                loadProfile()
            }
            else{
                val reference = FirebaseDatabase.getInstance().getReference("users/$uid/followed/${FirebaseAuth.getInstance().uid}").removeValue()
                val secondreference = FirebaseDatabase.getInstance().getReference("users/${FirebaseAuth.getInstance().uid}/following/$uid").removeValue()
                loadProfile()
            }
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {


        super.onSaveInstanceState(outState)

    }

    fun loadImages() {
        val recyclerview=root!!.findViewById(R.id.recyclerViewMyPhotos) as RecyclerView



        recyclerview.layoutManager= GridLayoutManager(activity,2)
        val adapter = GroupAdapter<ViewHolder>()
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid/images")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {

                    val imagedb = it.getValue(ImageDb::class.java)
                    if (imagedb != null) {
                        adapter.add(ImageItem(imagedb,it.ref))


                    }
                }
                recyclerview.adapter = adapter



            }



        })



    }



}
