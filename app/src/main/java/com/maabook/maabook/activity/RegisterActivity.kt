package com.maabook.maabook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.maabook.maabook.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var et_name: EditText
    private lateinit var et_email: EditText
    private lateinit var et_mobile_number: EditText
    private lateinit var et_password1: EditText
    private lateinit var et_password2: EditText
    private lateinit var btn_register: Button
    private lateinit var toolbar: Toolbar

    var valName: String? = "name"
    var valEmail: String? = "email@gmail.com"
    var valMobileNum: String? = "0987654321"
    var valPassword: String? = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        et_name = findViewById(R.id.et_name)
        et_email = findViewById(R.id.et_email)
        et_mobile_number = findViewById(R.id.et_mobile_number)
        et_password1 = findViewById(R.id.et_password1)
        et_password2 = findViewById(R.id.et_password2)
        btn_register = findViewById(R.id.btn_register)

        if (intent != null) {
            valName = intent.getStringExtra("name")
            valEmail = intent.getStringExtra("email")
            valMobileNum = intent.getStringExtra("mobileNum")
            valPassword = intent.getStringExtra("password")
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"

        btn_register.setOnClickListener() {
            val password1 = et_password1.text.toString()
            val password2 = et_password2.text.toString()

            if (password1 == password2) {
                Toast.makeText(this@RegisterActivity, "Added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.putExtra("name", et_name.text.toString())
                intent.putExtra("email", et_email.text.toString())
                intent.putExtra("mobileNum", et_mobile_number.text.toString())
                intent.putExtra("password", password1)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this@RegisterActivity, "Passwords doesn't match ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.putExtra("name", valName)
        intent.putExtra("email", valEmail)
        intent.putExtra("mobileNum", valMobileNum)
        intent.putExtra("password", valPassword)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

}