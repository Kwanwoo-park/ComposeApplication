package com.example.composeapplication

import com.example.composeapplication.model.ContentDTO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

val databaseUser = FirebaseDatabase.getInstance().getReference("User")
val databaseImage = FirebaseDatabase.getInstance().getReference("Images")
var result = mutableMapOf<String, String>()
var number = ""

fun getNumberUser(): Int{
    var i = 0

    databaseUser.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (column: DataSnapshot in snapshot.children) {
                if (column.key != i.toString()) break
                else i++
            }

            number = i.toString()

            setDatabaseUser()
        }

        override fun onCancelled(error: DatabaseError) {
            //
        }
    })

    return i
}

fun getNumberImage(contentDTO: ContentDTO): Int {
    var i = 0

    databaseImage.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (column: DataSnapshot in snapshot.children) {
                if (column.key != i.toString()) break
                else i++
            }

            number = i.toString()

            setDatabaseImage(contentDTO)
        }

        override fun onCancelled(error: DatabaseError) {

        }
    })

    return i
}

fun setDatabaseUser() {
    databaseUser.child(number).setValue(result)
    databaseUser.push()

}

fun setDatabaseImage(contentDTO: ContentDTO) {
    databaseImage.child(number).setValue(contentDTO)
    databaseImage.push()
}
