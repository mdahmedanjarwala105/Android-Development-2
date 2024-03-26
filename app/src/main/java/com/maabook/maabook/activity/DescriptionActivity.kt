package com.maabook.maabook.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.maabook.maabook.R
import com.maabook.maabook.database.BookDatabase
import com.maabook.maabook.database.BookEntity
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

    var bookId: String? = "100"

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
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
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
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE
                        val bookImageUrl = bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.logo).into(imgBookImage)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDesc.text = bookJsonObject.getString("description")

                        val bookEntity = BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUrl
                        )

                        val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                        val isFav = checkFav.get()

                        if (isFav) {
                            btnAddToFav.text = getString(R.string.remove_from_favourites)
                            val favColor = ContextCompat.getColor(applicationContext, R.color.orange)
                            btnAddToFav.setBackgroundColor(favColor)
                            val textFavColor = ContextCompat.getColor(applicationContext, R.color.brown)
                            btnAddToFav.setTextColor(textFavColor)
                        } else {
                            btnAddToFav.text = getString(R.string.add_to_favourite)
                            val noFavColor = ContextCompat.getColor(applicationContext, R.color.brown)
                            btnAddToFav.setBackgroundColor(noFavColor)
                            val textFavColor = ContextCompat.getColor(applicationContext, R.color.orange)
                            btnAddToFav.setTextColor(textFavColor)
                        }

                        btnAddToFav.setOnClickListener {

                            if (!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()) {
                                val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                val result = async.get()
                                if (result) {
                                    Toast.makeText(this@DescriptionActivity, getString(R.string.book_added_to_favourites), Toast.LENGTH_SHORT).show()

                                    btnAddToFav.text = getString(R.string.remove_from_favourites)
                                    val favColor = ContextCompat.getColor(applicationContext, R.color.orange)
                                    btnAddToFav.setBackgroundColor(favColor)
                                    val textFavColor = ContextCompat.getColor(applicationContext, R.color.brown)
                                    btnAddToFav.setTextColor(textFavColor)
                                } else {
                                    Toast.makeText(this@DescriptionActivity, "Some error occurred!", Toast.LENGTH_SHORT).show()
                                }
                            } else {

                                val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                val result = async.get()

                                if (result){
                                    Toast.makeText(this@DescriptionActivity,
                                        getString(R.string.book_removed_from_favourites), Toast.LENGTH_SHORT).show()

                                    btnAddToFav.text = getString(R.string.add_to_favourite)
                                    val noFavColor = ContextCompat.getColor(applicationContext, R.color.brown)
                                    btnAddToFav.setBackgroundColor(noFavColor)
                                    val textFavColor = ContextCompat.getColor(applicationContext, R.color.orange)
                                    btnAddToFav.setTextColor(textFavColor)
                                } else {
                                    Toast.makeText(this@DescriptionActivity, "Some error occurred!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@DescriptionActivity, "Some Error Occurred!", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@DescriptionActivity, "Some error occurred!", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Volley Error $it", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "1becfbd50ebb54"
                    return headers
                }
            }

            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }

            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
    class DBAsyncTask(val context: Context, private val bookEntity: BookEntity, private val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
        * */

        private val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {

//                  Check DB if the book is favourite or not
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null

                }

                2 -> {

//                  Save the book into DB as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }

                3 -> {

//                  Remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}