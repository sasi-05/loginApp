package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.loginapp.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Thread.sleep(2000)
        setTheme(R.style.Theme_login)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fireBaseAuth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener {
            val userName = binding.etUserName.text.toString()
            val passWord = binding.etPassword.text.toString()

            if (userName.isNotEmpty() && passWord.isNotEmpty()) {

                fireBaseAuth.createUserWithEmailAndPassword(userName, passWord)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "SingUp SuccessFull", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "SingUp Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Ensure to Fill UserName and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val googleSignInButton = binding.googleSignIn

        googleSignInButton.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val signInClient = GoogleSignIn.getClient(this, options)

            signInClient.signInIntent.also {
//                Toast.makeText(this, "In SignIn Client Module", Toast.LENGTH_SHORT).show()

                launcher.launch(it)
                // startActivityForResult(it,101)
            }
        }
    }

    private var launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if(result.resultCode == Activity.RESULT_OK){
//            Toast.makeText(this, "In Launcher Module", Toast.LENGTH_SHORT).show()
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).result
            account?.let {
                binding.progressBar.visibility = View.VISIBLE
                handleSignInWithFireBase(account)
            }
//        }
        }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)

         if(requestCode == 101){
             val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
             Toast.makeText(this,"In Launcher Module",Toast.LENGTH_SHORT).show()
             account?.let {
                 handleSignInWithFireBase(account)
             }
         }
     }*/
    private fun handleSignInWithFireBase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)

//        Toast.makeText(this, "In FireBase Module", Toast.LENGTH_SHORT).show()

        fireBaseAuth.signInWithCredential(credentials).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                binding.progressBar.visibility = View.GONE

                Toast.makeText(this, "SuccessFully Signed In With Google", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else
                Toast.makeText(this, "Failed to sign In With Google", Toast.LENGTH_SHORT).show()
        }
    }
}