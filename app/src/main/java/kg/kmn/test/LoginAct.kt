package kg.kmn.test

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class LoginAct : AppCompatActivity() {

    private lateinit var edLogin: EditText
    private lateinit var edPassword: EditText
    private lateinit var btnSignIn : Button
    private lateinit var btnSignUp : Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStartAuthActBtn : Button
    private lateinit var mUserEmail : TextView
    private lateinit var btnSignOut : Button

    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        init()
        mAuth = Firebase.auth
        Log.d(TAG, "onCreate()")
    }

    private fun init() {
        edLogin = findViewById(R.id.ed_login)
        edPassword = findViewById(R.id.ed_password)
        btnSignIn = findViewById(R.id.btn_signIn)
        btnSignUp = findViewById(R.id.btn_signUp)
        mStartAuthActBtn = findViewById(R.id.btn_openAuthAct)
        mUserEmail = findViewById(R.id.tv_userEmail)
        btnSignOut = findViewById(R.id.btn_SignOut)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d(TAG, "onStart()")
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "currentUser != null")

            showSigned()
            val userName = "Вы вошли как : ${currentUser.email}"
            mUserEmail.text = userName
            Toast.makeText(this, "User is existed", Toast.LENGTH_SHORT).show()

        } else {
            Log.d(TAG, "current == null")

            notSigned()
            Toast.makeText(this, "User is not existed", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickSignUp(view: View) {
        if (!TextUtils.isEmpty(edLogin.text.toString()) && !TextUtils.isEmpty(edPassword.text.toString())) {
            Log.d(TAG, "onClickSignUp")

            mAuth.createUserWithEmailAndPassword(
                edLogin.text.toString(),
                edPassword.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        showSigned()
                        sendEmailVerification()

                        Toast.makeText(
                            applicationContext,
                            "User has been Signed Up Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "createUserWithEmail:success")
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "User hasn't signed up, might you already signed up with this email, please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "createUserWithEmail:failure", task.exception)
                    }
                }
        } else {
            Toast.makeText(this, "Enter all fields.", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickSignIn(view: View) {
        if (!TextUtils.isEmpty(edLogin.text.toString()) && !TextUtils.isEmpty(edPassword.text.toString())) {
            mAuth.signInWithEmailAndPassword(edLogin.text.toString(), edPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        showSigned()

                        Toast.makeText(
                            applicationContext,
                            "User has been Signed In Successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d(TAG, "signInWithEmail:success")

                    } else {
                        notSigned()

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    fun onClickSignOut(view: View){
        FirebaseAuth.getInstance().signOut()
        notSigned()
    }

    private fun showSigned(){
        val user = mAuth.currentUser
        assert(user!=null)
        if(user.isEmailVerified){
            val userName = "Вы вошли как : ${user.email}"
            mUserEmail.text = userName
            mStartAuthActBtn.visibility = View.VISIBLE
            mUserEmail.visibility = View.VISIBLE
            btnSignOut.visibility = View.VISIBLE
            edLogin.visibility = View.GONE
            edPassword.visibility = View.GONE
            btnSignIn.visibility = View.GONE
            btnSignUp.visibility = View.GONE
        }else{
            Toast.makeText(this, "Проверьте вашу почту для подтверждения Email адреса", Toast.LENGTH_SHORT).show()
        }
    }

    private fun notSigned(){
        mStartAuthActBtn.visibility = View.GONE
        mUserEmail.visibility = View.GONE
        btnSignOut.visibility = View.GONE
        edLogin.visibility = View.VISIBLE
        edPassword.visibility = View.VISIBLE
        btnSignIn.visibility = View.VISIBLE
        btnSignUp.visibility = View.VISIBLE
    }

    fun onClickStartAuthAct(view: View){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }



    private fun sendEmailVerification(){

        val user = mAuth.currentUser
        assert(user != null)
        mAuth.currentUser.sendEmailVerification()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Проверьте вашу почту для подтверждения Email адреса", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, "Send Email Failed", Toast.LENGTH_SHORT).show()

                }
            }

    }


}

