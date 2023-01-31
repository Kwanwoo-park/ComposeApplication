package com.example.composeapplication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

val database = FirebaseDatabase.getInstance().getReference("User")
var result = mutableMapOf<String, String>()
var number = ""

fun getNumber(): Int{
    var i = 0

    database.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (column: DataSnapshot in snapshot.children) {
                if (column.key != i.toString()) break
                else i++
            }

            number = i.toString()

            setDatabase()
        }

        override fun onCancelled(error: DatabaseError) {
            //
        }
    })

    return i
}

fun setDatabase() {
    database.child(number).setValue(result)
    database.push()

}
