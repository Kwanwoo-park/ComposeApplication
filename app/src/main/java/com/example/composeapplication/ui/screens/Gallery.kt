package com.example.composeapplication.ui.screens


import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.composeapplication.R
import com.example.composeapplication.database
import com.example.composeapplication.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

lateinit var photoUri: Uri
lateinit var storage: FirebaseStorage
lateinit var auth: FirebaseAuth

@Composable
fun Gallery() {
    val context = LocalContext.current
    var comment by remember { mutableStateOf("") }

    val takePhotoFromAlbumLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                photoUri = result.data?.data!!
            }
        }
    }

    storage = FirebaseStorage.getInstance()
    auth = FirebaseAuth.getInstance()

    val photoPickerIntent = Intent(Intent.ACTION_PICK)
    photoPickerIntent.type = "image/*"
    val actionLauncher = takePhotoFromAlbumLauncher
    actionLauncher.launch(photoPickerIntent)

    
    Box(Modifier.fillMaxSize()) {
        Row {
           Image(
               bitmap = photoUri.parseBitmap(context).asImageBitmap(),
               contentDescription = "image",
               modifier = Modifier
                   .width(100.dp)
                   .height(100.dp)
                   .padding(8.dp)
                   .clickable {
                       val photoPickerIntent = Intent(Intent.ACTION_PICK)
                       photoPickerIntent.type = "image/*"
                       val actionLauncher = takePhotoFromAlbumLauncher
                       actionLauncher.launch(photoPickerIntent)
                   }
           )
           Column(horizontalAlignment = Alignment.End){
               TextField(
                   value = comment,
                   onValueChange = { comment = it },
                   textStyle = TextStyle(
                       color = Color(R.color.baseTextColor)
                   ),
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(100.dp)
                       .padding(8.dp)
               )
               
               Button(
                   onClick = {
                       val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                       val imageFileName = "JPEG_" + timeStamp + "_.png"
                       val storageRef = storage.reference.child("images").child(imageFileName)
                       storageRef.putFile(photoUri).addOnSuccessListener { taskSnapshot ->
                           Toast.makeText(context, R.string.upload_success, Toast.LENGTH_SHORT).show()

                           val uri = taskSnapshot.uploadSessionUri

                           val contentDTO = ContentDTO()

                           contentDTO.imageUrl = uri.toString()
                           contentDTO.uid = auth.currentUser?.uid
                           contentDTO.explain = comment
                           contentDTO.userId = auth.currentUser?.email
                           contentDTO.timestamp = System.currentTimeMillis()
                       }
                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp)
                       .background(Color(R.color.white))
               ) {
                   Text(text = R.string.upload_image.toString())
               }
           }
        }
    }
}

@Suppress("DEPRECATION", "NewApi")
private fun Uri.parseBitmap(context: Context): Bitmap {
    return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        true -> {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        }
        else -> {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }
    }
}