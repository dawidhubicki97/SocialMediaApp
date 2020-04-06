package com.example.socialmediaapp.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.R
import com.example.socialmediaapp.model.SearchItem
import com.example.socialmediaapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter

import com.xwray.groupie.ViewHolder




class SearchFragment: Fragment() {
    internal var root: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root=inflater.inflate(R.layout.fragment_search, container, false)
        turnSearchingEngineOn()

        return root
    }
    fun turnSearchingEngineOn(){
        var searchbutton=root!!.findViewById(R.id.searchButton) as Button
        var searchusertext=root!!.findViewById(R.id.searchUserText) as EditText
        searchbutton.setOnClickListener {

        }
        searchusertext.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchtext=searchusertext.text.toString()
                loadFirebaseData(searchtext)
            }

        })
    }
    fun loadFirebaseData(searchtext:String){

        val recyclerview=root!!.findViewById(R.id.searchRecyclerView) as RecyclerView
        recyclerview.layoutManager= LinearLayoutManager(getActivity())
        val adapter= GroupAdapter<ViewHolder>()
        if(searchtext.isEmpty()){
            adapter.clear()
            recyclerview.adapter=adapter
        }
        else{
            val ref = FirebaseDatabase.getInstance().getReference("users")
            ref.orderByChild("username").startAt(searchtext).endAt(searchtext+"\uf8ff").addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach{
                        val searchresult = it.getValue(User::class.java)
                        adapter.add(SearchItem(searchresult))
                        Log.d("ocotochodzi",searchresult.toString())
                    }
                    recyclerview.adapter=adapter
                }


            })
            adapter.setOnItemClickListener { item, view ->
                val searchitem =item as SearchItem
                val frag=ProfileFragment()
                val bundle=Bundle()
                bundle.putString("str",item.user!!.uid)
                frag.arguments=bundle
                getActivity()!!.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .addToBackStack(null)
                    .commit()


            }



        }
    }
}

