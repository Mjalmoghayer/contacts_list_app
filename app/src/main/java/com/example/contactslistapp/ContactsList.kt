package com.example.contactslistapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactslistapp.adapter.ContactAdapter
import com.example.contactslistapp.model.Contact
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContactsList : AppCompatActivity() {
    private val getData = Firebase.firestore
    private val data = ArrayList<Contact>()
    lateinit var btnAdd: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)


        getData.collection("Contacts").get().addOnSuccessListener { result ->
            val listOfContacts = findViewById<ListView>(R.id.listOfContacts)

            for (document in result) {
                val name = document.data["name"]
                val number = document.data["number"]
                val address = document.data["address"]

                data.add(Contact(name.toString(), number.toString(), address.toString()))


                val contactAdapter = ContactAdapter(this, data)
                listOfContacts.adapter = contactAdapter

                listOfContacts.setOnItemClickListener { _, _, position, _ ->
                    Toast.makeText(this, data[position].name, Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this, "$name $number $address", Toast.LENGTH_LONG).show()


            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
        }

        btnAdd = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}