package com.siresystems.furtherwestschool

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.MaterialToolbar
import com.siresystems.furtherwestschool.models.PaymentHistory
import org.json.JSONArray

class PaymentHistoryActivity : AppCompatActivity() {

    private lateinit var newsListView: ListView
    private lateinit var newsList: MutableList<PaymentHistory>

    private val url = "https://yosefe27-001-site1.ktempurl.com/apis/get_payment_history.php" // CHANGE THIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_history)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        newsListView = findViewById(R.id.newsListView)
        newsList = mutableListOf()

        fetchNews()
    }

    private fun fetchNews() {

        val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
        val parentID = prefs.getString("email", "") ?: ""

        val url = "https://furtherwest-001-site1.ktempurl.com/apis/get_payment_history.php?parentID=$parentID"

        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(
            url,
            null,
            { response ->

                if (response.getString("status") == "success") {

                    val data = response.getJSONArray("data")
                    newsList.clear()

                    for (i in 0 until data.length()) {

                        val obj = data.getJSONObject(i)

                        newsList.add(
                            PaymentHistory(
                                obj.getString("receipt_number"),
                                obj.getString("amount"),
                                obj.getString("payment_datetime"),
                                obj.getString("channel_transaction_id"),
                                obj.getString("transaction_status"),
                                obj.getString("student_name"),
                                obj.getString("studentID"),
                                obj.getString("image")
                            )
                        )
                    }

                    newsListView.adapter = NewsAdapter()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to load payments", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    inner class NewsAdapter : BaseAdapter() {

        override fun getCount(): Int = newsList.size
        override fun getItem(position: Int): Any = newsList[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val view = layoutInflater.inflate(R.layout.item_payment_history, parent, false)

            val dateText = view.findViewById<TextView>(R.id.rcptDate)
            val rcptNoText = view.findViewById<TextView>(R.id.rcptNo)
            val studentIDText = view.findViewById<TextView>(R.id.rcptStudentID)
            val amount = view.findViewById<TextView>(R.id.rcptAmount)
            val status = view.findViewById<TextView>(R.id.rcptStatus)
            val name = view.findViewById<TextView>(R.id.rcptName)
            val imageView = view.findViewById<ImageView>(R.id.rcptImage)
            val downloadBtn = view.findViewById<Button>(R.id.btnDownload)

            val item = newsList[position]

            dateText.text = "Receipt Date: ${item.pay_date}"
            rcptNoText.text = "Receipt No: ${item.receipt_no}"
            studentIDText.text = "Student ID: ${item.student_id}"
            name.text = "Name: ${item.student_name}"
            status.text = "Status: ${item.status}"
            amount.text = "Amount: ${item.amount}"

            // ✅ IMAGE + DOWNLOAD LOGIC (PUT YOUR CODE HERE)
            if (item.image.isNotEmpty()) {
                imageView.visibility = View.VISIBLE
                downloadBtn.visibility = View.VISIBLE

                com.squareup.picasso.Picasso.get()
                    .load(item.image)
                    .into(imageView)

                downloadBtn.setOnClickListener {
                    downloadImage(item.image)
                }

            } else {
                imageView.visibility = View.GONE
                downloadBtn.visibility = View.GONE
            }


            return view
        }
    }

    private fun downloadImage(imageUrl: String) {

        val request = android.app.DownloadManager.Request(android.net.Uri.parse(imageUrl))
            .setTitle("Receipt Download")
            .setDescription("Downloading receipt...")
            .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        // Save to Downloads folder
        request.setDestinationInExternalPublicDir(
            android.os.Environment.DIRECTORY_DOWNLOADS,
            "receipt_${System.currentTimeMillis()}.jpg"
        )

        val manager = getSystemService(DOWNLOAD_SERVICE) as android.app.DownloadManager
        manager.enqueue(request)

        Toast.makeText(this, "Download started...", Toast.LENGTH_SHORT).show()
    }
}