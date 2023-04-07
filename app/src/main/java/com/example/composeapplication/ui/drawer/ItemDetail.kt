package com.example.composeapplication.ui.drawer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.composeapplication.R
import com.example.composeapplication.model.ContentDTO
import com.example.composeapplication.ui.screens.*
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ItemDetail(contentDTO: ContentDTO) {

    var uid = FirebaseAuth.getInstance().currentUser?.uid
    val bitmap: MutableState<Bitmap?> = remember{ mutableStateOf(null) }
    val context = LocalContext.current

    Log.d("pkw", "ItemDetail: $contentDTO")

    Card {
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = "user_profile",
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                        .padding(7.5.dp)
                )
                Text(
                    text = contentDTO.userId.toString()
                )
            }


            Glide.with(context)
                .asBitmap()
                .load(contentDTO.imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bitmap.value = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

            bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->
                Image(
                    bitmap = fetchedBitmap,
                    contentDescription = "main_image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .fillMaxSize(0.4f)
                )
            } ?: Image(
                painter = painterResource(R.drawable.ic_account),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .fillMaxSize(0.4f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "heart",
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                        .clickable {
                            contentDTO.favoriteCount = contentDTO.favoriteCount + 1
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_chat_black),
                    contentDescription = "comment",
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp)
                )
            }

            Text(
                text = "좋아요 ${contentDTO.favoriteCount}개",
                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
            )

            Text(
                text = contentDTO.explain.toString(),
                modifier = Modifier
                    .padding(8.dp, 0.dp, 0.dp, 35.dp)
                    .fillMaxWidth()

            )
        }
    }
}