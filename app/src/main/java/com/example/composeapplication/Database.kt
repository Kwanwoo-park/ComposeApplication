package com.example.composeapplication

import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

fun getNumber() {
    database.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            var i = 0
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
}

fun setDatabase() {
    database.child(number).setValue(result)
    database.push()

}

fun createAccount(email: String, password: String) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        auth?.createUserWithEmailAndPassword(email, password)?.
                addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        getNumber()
                    }
                }
    }
}

fun logout() {
    auth?.signOut()
}