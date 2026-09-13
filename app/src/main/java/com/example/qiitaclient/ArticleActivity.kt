package com.example.qiitaclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.qiitaclient.model.Article
import com.example.qiitaclient.view.ArticleView

class ArticleActivity : AppCompatActivity() {

    companion object {

        private const val ARTICLE_EXTRA: String = "article"

        fun intent(context: Context, article: Article): Intent =
            Intent(context, ArticleActivity::class.java)
                .putExtra(ARTICLE_EXTRA, article)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val articleView = findViewById(R.id.article_view) as ArticleView
        val webView = findViewById(R.id.web_view) as WebView

        val article: Article = androidx.core.content.IntentCompat.getParcelableExtra(intent, ARTICLE_EXTRA, Article::class.java)!!
        articleView.setArticle(article)
        webView.loadUrl(article.url)

    }
}