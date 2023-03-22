package com.example.composeapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapplication.R
import com.example.composeapplication.database
import com.example.composeapplication.getNumber
import com.example.composeapplication.result
import com.example.composeapplication.ui.main.MainActivity
import com.example.composeapplication.ui.theme.ComposeApplicationTheme
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class LoginActivity: ComponentActivity() {
    private var auth: FirebaseAuth? = null
    private val GOOGLE_LOGIN_CODE = -1
    private lateinit var googleSignInClient: GoogleSignInClient

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultss: ActivityResult ->
        if (resultss.resultCode == GOOGLE_LOGIN_CODE) {
            val results = Auth.GoogleSignInApi.getSignInResultFromIntent(resultss.data!!)
            
            if (results!!.isSuccess) {
                val account = results.signInAccount
                firebaseAuthWithGoogle(account!!)
            }
        }
        else {
            Log.d("pkw", "openActivityResultLauncher: sign in unsuccess")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            ComposeApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun LoginScreen() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val keyboardController = LocalSoftwareKeyboardController.current

        var id by remember { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(R.color.baseBackground))
        ) {
            Column(
                modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                TextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text(text = getString(R.string.email)) },
                    modifier = Modifier
                        .padding(20.dp, 0.dp, 20.dp, 10.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle(
                        color = Color(R.color.baseTextColor),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        defaultKeyboardAction(imeAction = ImeAction.Next)
                    })
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = getString(R.string.password))},
                    modifier = Modifier
                        .padding(20.dp, 0.dp, 20.dp, 10.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle(
                        color = Color(R.color.baseTextColor),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password).copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    })
                )
                
                Button(
                    onClick = { emailLogin(id, password) },
                    modifier = Modifier
                        .padding(20.dp, 15.dp, 20.dp, 35.dp)
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = getString(R.string.signin_email))
                }

                Button(
                    onClick = { googleLogin() },
                    modifier = Modifier
                        .padding(20.dp, 0.dp, 20.dp, 5.dp)
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = getString(R.string.signin_google))
                }
            }
        }
    }

    private fun createAndLoginEmail(id: String, password: String) {
        auth?.createUserWithEmailAndPassword(id, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.signup_complete), Toast.LENGTH_SHORT).show()
                    result["email"] = id
                    result["password"] = password
                    getNumber()
                    moveMainPage(auth?.currentUser)
                }
                else if (task.exception?.message.isNullOrEmpty()) {
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
                else {
                    signInEmail(id, password)
                }
            }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, getString(R.string.signin_complete), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            Log.d("pkw", "moveMainPage: $user")
        }
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        val activityLauncher = resultLauncher
        activityLauncher.launch(signInIntent)
    }

    private fun emailLogin(id: String, password: String) {
        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.signout_fail_null), Toast.LENGTH_SHORT).show()
        }
        else {
            createAndLoginEmail(id, password)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (column in snapshot.children) {
                                var email = column.child("email").value.toString()

                                if (email.contains("gmail")) break

                                if (email != account.email.toString()) {
                                    result["email"] = account.email.toString()
                                    result["password"] = account.idToken.toString()
                                    getNumber()
                                    break
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            //
                        }
                    })
                    moveMainPage(auth?.currentUser)
                }
            }
    }

    private fun signInEmail(id: String, password: String) {
        auth?.signInWithEmailAndPassword(id, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(auth?.currentUser)
                }
                else {
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        moveMainPage(auth?.currentUser)
    }
}