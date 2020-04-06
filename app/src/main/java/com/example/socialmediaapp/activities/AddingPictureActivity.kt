package com.example.socialmediaapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialmediaapp.R
import com.example.socialmediaapp.model.ImageDb
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_adding_picture.*
import java.util.*




class AddingPictureActivity : AppCompatActivity() {
    var selectedPhotoUri: Uri?=null
    val uid= FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_picture)
        choosePictureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        addPictureButton.setOnClickListener{
            uploadImageToFirebase()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null) {
            selectedPhotoUri = data.data
            Picasso.get().load(selectedPhotoUri).fit().into(choosePictureButton)



        }
    }
    fun uploadImageToFirebase(){
        if(selectedPhotoUri==null)return

        val filename= UUID.randomUUID().toString()
        val ref= FirebaseStorage.getInstance().getReference("images/$filename")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {



            ref.downloadUrl.addOnSuccessListener {

                val reference = FirebaseDatabase.getInstance().getReference("users/$uid/images").push()
                val description=pictureDescriptionTextField.text.toString()
                val image = ImageDb(it.toString(), System.currentTimeMillis(),description)
                reference.setValue(image)
                val intent = Intent(this, appActivity::class.java)
                startActivity(intent)
                finish();
                Toast.makeText(this, "Obrazek Dodany", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
