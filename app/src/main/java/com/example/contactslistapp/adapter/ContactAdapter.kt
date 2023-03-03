package com.example.contactslistapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.contactslistapp.R
import com.example.contactslistapp.model.Contact
import com.google.firebase.firestore.FirebaseFirestore

// ContactAdapter class is used to display the data in the listview
class ContactAdapter(var activity: Activity, var data: ArrayList<Contact>) : BaseAdapter() {
    val db = FirebaseFirestore.getInstance()

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var root = convertView

        if (root == null)
            root = LayoutInflater.from(activity).inflate(R.layout.contact_card_item, null, false)

        root!!.findViewById<TextView>(R.id.getName).text = data[position].name
        root.findViewById<TextView>(R.id.getNumber).text = data[position].number
        root.findViewById<TextView>(R.id.getAddress).text = data[position].address



        root.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Delete Contact")
            builder.setMessage("Are you sure you want to delete this contact?")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { dialog, which ->

                db.collection("Contacts").get().addOnFailureListener {
                    Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                }.addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.data["name"] == data[position].name) {

                            db.collection("Contacts").document(document.id).delete()
                                .addOnSuccessListener {
                                    data.removeAt(position)
                                    notifyDataSetChanged()
                                    Toast.makeText(
                                        activity,
                                        "Contact deleted successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG)
                                        .show()
                                }
                            break
                        }
                    }
                }

            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
        return root
    }

}