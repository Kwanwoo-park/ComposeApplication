package com.example.composeapplication.ui.screens


import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.example.composeapplication.R
import com.example.composeapplication.getNumberImage
import com.example.composeapplication.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

lateinit var storage: FirebaseStorage
lateinit var auth: FirebaseAuth

@Composable
fun Gallery(num: String, navController: NavController) {
    Log.d("pkw", "Gallery: ${num}")

    storage = FirebaseStorage.getInstance()
    auth = FirebaseAuth.getInstance()

    val context = LocalContext.current
    
    var comment by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri>("https://firebasestorage.googleapis.com/v0/b/composeapplication.appspot.com/o/images%2Fic_account.png?alt=media&token=bb6400d7-f24e-4cd8-8a1c-68a43c327fc8".toUri()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { results ->
            if (results.resultCode == -1) {
                photoUri = results.data?.data!!
                Log.d("pkw", "Gallery: $photoUri")
            }
        }
    )

    LaunchedEffect(Unit) {
        val photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.type = "image/*"
        launcher.launch(photoIntent)
    }
    
    Box(
        Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.baseBackground))) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_account),
                contentDescription = "image",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(8.dp)
                    .clickable {
                        val photoIntent = Intent(Intent.ACTION_PICK)
                        photoIntent.type = "image/*"
                        launcher.launch(photoIntent)
                    }
            )
           Column(horizontalAlignment = Alignment.End){
               TextField(
                   value = comment,
                   onValueChange = { comment = it },
                   textStyle = TextStyle(
                       color = colorResource(id = R.color.baseTextColor)
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
                       storageRef.putFile(photoUri).addOnSuccessListener {
                           Toast.makeText(context, R.string.upload_success, Toast.LENGTH_SHORT).show()

                           val contentDTO = ContentDTO()

                           contentDTO.imageUrl = "https://firebasestorage.googleapis.com/v0/b/composeapplication.appspot.com/o/images%2F" + imageFileName + "?alt=media"
                           contentDTO.uid = auth.currentUser?.uid
                           contentDTO.explain = comment
                           contentDTO.userId = auth.currentUser?.email
                           contentDTO.timestamp = System.currentTimeMillis()

                           getNumberImage(contentDTO)

                           navController.popBackStack()
                       }
                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp)
                       .background(Color(R.color.white))
               ) {
                   Text(stringResource(id = R.string.upload_image))
               }
           }
        }
    }
}