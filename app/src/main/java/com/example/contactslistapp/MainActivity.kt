package com.example.contactslistapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    //create a database
    private val db = Firebase.firestore
    lateinit var btnSave: Button
    lateinit var btnBackToList: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave = findViewById(R.id.btnSave)
        btnBackToList = findViewById(R.id.btnBackToList)

    }

    override fun onResume() {
        super.onResume()


        //save to firebase
        btnSave.setOnClickListener {

            val name = this.findViewById<EditText>(R.id.setName).text.toString()
            val number = this.findViewById<EditText>(R.id.setNumber).text.toString()
            val address = this.findViewById<EditText>(R.id.setAddress).text.toString()

            //if name, number and address are not empty
            if (name.isNotEmpty() && number.isNotEmpty() && address.isNotEmpty()) {
                val contact =
                    hashMapOf(
                        "name" to name,
                        "number" to number,
                        "address" to address
                    )
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Saving data...")
                progressDialog.setCancelable(false)
                progressDialog.show()


                db.collection("Contacts").add(contact).addOnSuccessListener { result ->

                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Save failed", Toast.LENGTH_LONG).show()
                    }


            } else {
                Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
            }
        }


        //show contacts
        btnBackToList.setOnClickListener {
            startActivity(Intent(this, ContactsList::class.java))
        }
    }

}