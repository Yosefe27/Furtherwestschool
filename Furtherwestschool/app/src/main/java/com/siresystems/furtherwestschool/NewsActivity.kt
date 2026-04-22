package com.siresystems.furtherwestschool

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.MaterialToolbar
import com.siresystems.furtherwestschool.models.NewsItem
import org.json.JSONArray

class NewsActivity : AppCompatActivity() {

    private lateinit var newsListView: ListView
    private lateinit var newsList: MutableList<NewsItem>

    private val url = "https://yosefe27-001-site1.ktempurl.com/apis/get_news.php" // CHANGE THIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        newsListView = findViewById(R.id.newsListView)
        newsList = mutableListOf()

        fetchNews()
    }

    private fun fetchNews() {

        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(
            url,
            null,
            { response ->

                if (response.getString("status") == "success") {

                    val data: JSONArray = response.getJSONArray("data")

                    newsList.clear()

                    for (i in 0 until data.length()) {

                        val obj = data.getJSONObject(i)

                        newsList.add(
                            NewsItem(
                                obj.getString("news_date"),
                                obj.getString("news_title"),
                                obj.getString("news_details"),
                                obj.getString("news_image"),
                                false
                            )
                        )
                    }

                    newsListView.adapter = NewsAdapter()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to load news", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    inner class NewsAdapter : BaseAdapter() {

        override fun getCount(): Int = newsList.size
        override fun getItem(position: Int): Any = newsList[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val view = layoutInflater.inflate(R.layout.item_news, parent, false)

            val dateText = view.findViewById<TextView>(R.id.newsDate)
            val titleText = view.findViewById<TextView>(R.id.newsTitle)
            val contentText = view.findViewById<TextView>(R.id.newsContent)
            val toggleText = view.findViewById<TextView>(R.id.toggleText)
            val imageView = view.findViewById<ImageView>(R.id.newsImage)

            val item = newsList[position]

            dateText.text = item.date
            titleText.text = item.title
            contentText.text = item.content

            // Load image (if available)
            if (item.image.isNotEmpty()) {
                imageView.visibility = View.VISIBLE
                com.squareup.picasso.Picasso.get()
                    .load(item.image)
                    .into(imageView)
            } else {
                imageView.visibility = View.GONE
            }

            // Expand/collapse
            if (item.expanded) {
                contentText.maxLines = Int.MAX_VALUE
                toggleText.text = "Show Less"
            } else {
                contentText.maxLines = 2
                toggleText.text = "Show More"
            }

            toggleText.setOnClickListener {
                item.expanded = !item.expanded
                notifyDataSetChanged()
            }

            return view
        }
    }
}