package com.example.login_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent;
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class Registration : AppCompatActivity() {
    private lateinit var password: String
    private lateinit var userName: String
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var buttonRegister: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etUsername= findViewById(R.id.etRUserName)
        etPassword= findViewById(R.id.etRPassword)
        buttonRegister= findViewById(R.id.btnRegister)

        buttonRegister.setOnClickListener {
            registerUser()
        }
    }
    //Code for registerUser() method
    private fun registerUser() {
         userName = etUsername.getText().toString().trim()
         password = etPassword.getText().toString().trim()
        if (userName.isEmpty()) {
            etUsername.setError("Username is required")
            etUsername.requestFocus()
            return         }
        else if (password.isEmpty()) {
            etPassword.setError("Password is required")
            etPassword.requestFocus()
            return
        }
        val call: Call<ResponseBody> = RetrofitClient
            .getInstance()
            .api
            .createUser(User(userName, password))
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                var s = ""
                try {
                    s = response.body()!!.string()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (s == "SUCCESS") {
                    Toast.makeText(
                        this@Registration,
                        "Successfully registered. Please login",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this@Registration, Login::class.java))
                } else {
                    Toast.makeText(this@Registration, "User already exists!", Toast.LENGTH_LONG)
                        .show()
                }
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@Registration, t.message, Toast.LENGTH_LONG).show()
            }
        })
        }
    }