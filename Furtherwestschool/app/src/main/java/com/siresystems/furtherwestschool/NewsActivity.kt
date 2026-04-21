package com.siresystems.furtherwestschool

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.siresystems.furtherwestschool.models.NewsItem

class NewsActivity : AppCompatActivity() {

    private lateinit var newsListView: ListView
    private lateinit var newsList: MutableList<NewsItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        newsListView = findViewById(R.id.newsListView)

        // Sample data (replace with database later)
        newsList = mutableListOf(
            NewsItem(
                "2026-04-10",
                "School Fees Reminder",
                "Parents are reminded to complete school fee payments before the end of the month to avoid inconveniences."
            ),
            NewsItem(
                "2026-04-15",
                "Upcoming Sports Day",
                "Our annual sports day will be held on the 15th. All students are expected to participate. Parents are invited to attend and support their children."
            ),
            NewsItem(
                "2026-04-20",
                "Mid-Term Exams",
                "Mid-term examinations will begin on April 20th. Students are advised to revise all subjects thoroughly."
            )
        )

        val adapter = NewsAdapter()
        newsListView.adapter = adapter
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

            val item = newsList[position]

            // Set data
            dateText.text = item.date
            titleText.text = item.title
            contentText.text = item.content

            // Expand / Collapse logic
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