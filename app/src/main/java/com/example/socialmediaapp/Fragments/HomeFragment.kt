package com.example.socialmediaapp.Fragments

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.R

import com.example.socialmediaapp.model.ImageDb
import com.example.socialmediaapp.model.ImageItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class HomeFragment: Fragment() {
    internal var root: View? = null
    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        root = inflater.inflate(R.layout.fragment_home, container, false)


        loadImages()
        return root
    }


    fun addImageToAdapter( userforpic:String) {
        val ref = FirebaseDatabase.getInstance().getReference("users/$userforpic/images")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {

                        Log.d("drugi", it.value.toString())
                        val imagedb = it.getValue(ImageDb::class.java)
                        if (imagedb != null) {
                            adapter.add(ImageItem(imagedb, it.ref))
                        }


                    }
                }
        })
    }


    fun loadImages() {
        var recyclerview = root!!.findViewById(R.id.recyclerViewPicsMain) as RecyclerView
        recyclerview.layoutManager = GridLayoutManager(getActivity(), 3)


        var grindwidth = resources.displayMetrics.widthPixels
        var imagewidth = grindwidth / 2


        val firstreference = FirebaseDatabase.getInstance().getReference("users/${FirebaseAuth.getInstance().uid}/following")
        firstreference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    addImageToAdapter(it.getValue().toString())
                }
                recyclerview.adapter = adapter
            }

        })


    }
}
class asyncloading:AsyncTask<String,String,String>(){
    override fun doInBackground(vararg p0: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}