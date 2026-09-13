package com.example.qiitaclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import com.example.qiitaclient.model.Article
import com.example.qiitaclient.ui.theme.QiitaClientTheme
import com.example.qiitaclient.view.ArticleView
import com.example.qiitaclient.model.User


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val listAdapter = ArticleListAdapter(applicationContext)
        listAdapter.articles = listOf(dummyArticle("Kotlin入門", "たろう"),
                dummyArticle("Java入門", "じろう"))

        val listView: ListView = findViewById(R.id.list_view) as ListView
        listView.adapter = listAdapter
        listView.setOnItemClickListener { _, _, position, _ ->
            val article = listAdapter.articles[position]
            startActivity(ArticleActivity.intent(this, article))
        }
    }
    //ダミーを生成するメゾット
    private fun dummyArticle(title: String, userName: String): Article =
        Article(id = "",
                title = title,
                url = "https://kotlinlang.org/",
                user = User(id = "", name = userName, profileImageUrl = ""))
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QiitaClientTheme {
        Greeting("Android")
    }
}