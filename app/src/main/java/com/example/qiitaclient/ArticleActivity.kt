package com.example.qiitaclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.IntentCompat
import com.example.qiitaclient.model.Article
import com.example.qiitaclient.ui.theme.QiitaClientTheme
import com.example.qiitaclient.view.ArticleItem

class ArticleActivity : ComponentActivity() {

    companion object {
        private const val ARTICLE_EXTRA: String = "article"

        fun intent(context: Context, article: Article): Intent =
            Intent(context, ArticleActivity::class.java)
                .putExtra(ARTICLE_EXTRA, article)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val article: Article? = IntentCompat.getParcelableExtra(intent, ARTICLE_EXTRA, Article::class.java)

        setContent {
            QiitaClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        article?.let {
                            ArticleItem(article = it, onClick = {})
                            HorizontalDivider()
                            AndroidView(
                                factory = { context ->
                                    WebView(context).apply {
                                        loadUrl(it.url)
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
