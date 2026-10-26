package com.example.qiitaclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.qiitaclient.client.ArticleClient
import com.example.qiitaclient.model.Article
import com.example.qiitaclient.ui.theme.QiitaClientTheme
import com.example.qiitaclient.view.ArticleItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var articleClient: ArticleClient

    private val disposables = CompositeDisposable()

    var queryState by mutableStateOf("")
    var articlesState by mutableStateOf<List<Article>>(emptyList())
    var isLoadingState by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as QiitaClientApp).component.inject(this)
        enableEdgeToEdge()

        setContent {
            QiitaClientTheme {
                MainScreen(
                    query = queryState,
                    onQueryChange = { queryState = it },
                    articles = articlesState,
                    isLoading = isLoadingState,
                    onSearch = { searchArticles() },
                    onArticleClick = { article ->
                        startActivity(ArticleActivity.intent(this, article))
                    }
                )
            }
        }
    }

    private fun searchArticles() {
        if (queryState.isBlank()) return
        isLoadingState = true

        disposables.add(
            articleClient.search(queryState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate {
                    isLoadingState = false
                }
                .subscribe({ articles ->
                    articlesState = articles
                    queryState = ""
                }, { error ->
                    toast("エラー: $error")
                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}

@Composable
fun MainScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    articles: List<Article>,
    isLoading: Boolean,
    onSearch: () -> Unit,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("query_edit_text"),
                    placeholder = { Text("キーワードを入力") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSearch,
                    modifier = Modifier.testTag("search_button")
                ) {
                    Text("検索")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("progress_bar")
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("list_view")
                ) {
                    items(articles) { article ->
                        ArticleItem(
                            article = article,
                            onClick = { onArticleClick(article) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
