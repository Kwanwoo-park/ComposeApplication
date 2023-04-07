package com.example.composeapplication.ui.screens


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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

    storage = FirebaseStorage.getInstance() //FirebaseStorage 연결
    auth = FirebaseAuth.getInstance() //Firebase Authentication 연결

    val context = LocalContext.current

    //uri, 가져온 uri값을 이미지로 변환한 값, 작성글 저장하는 변수
    var comment by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap: MutableState<Bitmap?> =  remember { mutableStateOf(null) }

    //갤러리 열어서 image uri와 uri를 bitmap으로 변환하는 과정
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
        //갤러리 열기
        val photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.type = "image/*"
        launcher.launch(photoIntent)
        Log.d("pkw", "Gallery: $photoUri")
    }
    
    Box(
        Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.baseBackground))
            .verticalScroll(rememberScrollState())
    ) {
           Column(horizontalAlignment = Alignment.CenterHorizontally){
               //갤러리에서 가져온 url을 Bitmap으로 변환하는 코드
               Glide.with(context)
                   .asBitmap()
                   .load(photoUri)
                   .into(object: CustomTarget<Bitmap>() {
                       override fun onResourceReady(
                           resource: Bitmap,
                           transition: Transition<in Bitmap>?
                       ) {
                           bitmap.value = resource
                       }

                       override fun onLoadCleared(placeholder: Drawable?) {
                       }
                   })

               //변환한 bitmap이 null이나 잘 되지 않았으면 기본 이미지 출력 아니면 서버에 있는 이미지 출력
               bitmap.value?.asImageBitmap()?.let {fetchedBitmap ->
                   Image(
                       bitmap = fetchedBitmap,
                       contentDescription = "gallery_image",
                       modifier = Modifier
                           .padding(8.dp)
                           .clickable {
                               val photoIntent = Intent(Intent.ACTION_PICK)
                               photoIntent.type = "image/*"
                               launcher.launch(photoIntent)
                           }
                   )
               } ?: Image(
                   painter = painterResource(id = R.drawable.ic_account),
                   contentDescription = null,
                   modifier = Modifier
                       .width(200.dp)
                       .height(200.dp)
                       .padding(8.dp)
                       .clickable {
                           val photoIntent = Intent(Intent.ACTION_PICK)
                           photoIntent.type = "image/*"
                           launcher.launch(photoIntent)
                       }
               )
               //이미지와 함께 올라는 작성글 작성하는 공간
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
               //이미지와 작성글, 아이디, 작성 시간, email을 버튼을 누르면 서버에 올리는 코드
               Button(
                   onClick = {
                       val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                       val imageFileName = "JPEG_" + timeStamp + "_.png"
                       val storageRef = storage.reference.child("images").child(imageFileName)
                       storageRef.putFile(photoUri!!).addOnSuccessListener {
                           Log.d("pkw", "Gallery log: ${R.string.upload_success}")

                           val contentDTO = ContentDTO()

                           contentDTO.imageUrl = "https://firebasestorage.googleapis.com/v0/b/composeapplication.appspot.com/o/images%2F" + imageFileName + "?alt=media"
                           contentDTO.uid = auth.currentUser?.uid
                           contentDTO.explain = comment
                           contentDTO.userId = auth.currentUser?.email
                           contentDTO.timestamp = System.currentTimeMillis()

                           getNumberImage(contentDTO)

                           Log.d("pkw", "Gallery navController: ${navController.currentBackStackEntry}")
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