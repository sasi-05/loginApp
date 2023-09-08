package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
        private lateinit var binding: ActivityLoginBinding
        private lateinit var fireBaseAuth: FirebaseAuth


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.loginBtn.setOnClickListener {
                val userName = binding.etUserName.text.toString()
                val passWord = binding.etPassword.text.toString()

                if (userName.isNotEmpty() && passWord.isNotEmpty()) {
                    fireBaseAuth = FirebaseAuth.getInstance()

                    fireBaseAuth.signInWithEmailAndPassword(userName, passWord)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Login SuccessFull", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Ensure to Fill UserName and password", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            binding.tvToSignUp.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }