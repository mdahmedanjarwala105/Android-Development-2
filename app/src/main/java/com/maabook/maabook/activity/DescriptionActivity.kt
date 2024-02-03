package com.maabook.maabook.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.maabook.maabook.R
import com.maabook.maabook.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    private lateinit var txtBookName: TextView
    private lateinit var txtBookAuthor: TextView
    private lateinit var txtBookPrice: TextView
    private lateinit var txtBookRating: TextView
    private lateinit var imgBookImage: ImageView
    private lateinit var txtBookDesc: TextView
    private lateinit var btnAddToFav: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: RelativeLayout
    private lateinit var toolbar: Toolbar

    private var bookId : String? = "Book ID"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some Unexpected Error Occurred", Toast.LENGTH_SHORT).show()
        }

        if(bookId == "100"){
            finish()
            Toast.makeText(this@DescriptionActivity, "Some Unexpected Error Occurred", Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                try {
                    val success = it.getBoolean("success")
                    if (success) {
                        val bookJSONObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE

                        Picasso.get().load(bookJSONObject.getString("image"))
                            .error(R.drawable.logo).into(imgBookImage)
                        txtBookName.text = bookJSONObject.getString("name")
                        txtBookAuthor.text = bookJSONObject.getString("author")
                        txtBookPrice.text = bookJSONObject.getString("price")
                        txtBookRating.text = bookJSONObject.getString("rating")
                        txtBookDesc.text = bookJSONObject.getString("description")
                    } else {
                        Toast.makeText(this@DescriptionActivity, "Some Error Occurred!!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@DescriptionActivity, "Some Error Occurred!!", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Some Error Occurred!!", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String> ()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "1becfbd50ebb54"
                    return headers
                }
            }
            queue.add(jsonRequest)
        } else {
//          Internet is not available
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") {
                    _, _ -> val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") {
                    _, _ -> ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
}