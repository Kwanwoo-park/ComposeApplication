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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapplication.R
import com.example.composeapplication.databaseUser
import com.example.composeapplication.getNumberUser
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

class LoginActivity: ComponentActivity() {
    private var auth: FirebaseAuth? = null
    private val GOOGLE_LOGIN_CODE = -1
    private lateinit var googleSignInClient: GoogleSignInClient

    //Google email 연동
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultss: ActivityResult ->
        if (resultss.resultCode == GOOGLE_LOGIN_CODE) {
            val results = Auth.GoogleSignInApi.getSignInResultFromIntent(resultss.data!!)

            //성공적으로 연동되면 회원가입 및 로그인
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
        //Google login option
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Google login class 생성
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val keyboardController = LocalSoftwareKeyboardController.current

        //아이디와 비밀번호 저장하는 변수
        var id by remember { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.baseBackground))
        ) {
            Column(
                modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //아이디 입력하는 칸
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
                //비밀번호 입력하는 칸
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = getString(R.string.password))},
                    modifier = Modifier
                        .padding(20.dp, 0.dp, 20.dp, 10.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle(
                        color = colorResource(id = R.color.baseTextColor),
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
                //회원가입 및 로그인 버튼
                Button(
                    onClick = { emailLogin(id, password) },
                    modifier = Modifier
                        .padding(20.dp, 15.dp, 20.dp, 35.dp)
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = getString(R.string.signin_email))
                }
                //Google login button
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

    //Firebase Authentication에 이메일과 비밀번호 올리는 코드(회원가입)와 이미 회원가입했으면 로그인 메소드 호출
    private fun createAndLoginEmail(id: String, password: String) {
        auth?.createUserWithEmailAndPassword(id, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.signup_complete), Toast.LENGTH_SHORT).show()
                    result["email"] = id
                    result["password"] = password
                    getNumberUser()
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

    //메인화면으로 이동하는 메소드
    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            Log.d("pkw", "moveMainPage log: ${getString(R.string.signin_complete)}")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            Log.d("pkw", "moveMainPage: $user")
        }
    }

    //구글 로그인 메소드
    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        val activityLauncher = resultLauncher
        activityLauncher.launch(signInIntent)
    }

    //빈 칸 체크와 회원가입 메소드로 호출
    private fun emailLogin(id: String, password: String) {
        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.signout_fail_null), Toast.LENGTH_SHORT).show()
        }
        else {
            createAndLoginEmail(id, password)
        }
    }

    //Firebase Authentication에 구글 이메일과 비밀번호 올리는 코드
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    databaseUser.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (column in snapshot.children) {
                                var email = column.child("email").value.toString()

                                if (email.contains("gmail")) break

                                if (email != account.email.toString()) {
                                    result["email"] = account.email.toString()
                                    result["password"] = account.idToken.toString()
                                    getNumberUser()
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

    //로그인 메소드
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

    //액티비티가 호출되면 바로 메인 화면 이동 메소드를 호출해서 자동로그인 구현
    override fun onStart() {
        super.onStart()

        moveMainPage(auth?.currentUser)
    }
}