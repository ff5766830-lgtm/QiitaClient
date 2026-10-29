package com.example.qiitaclient

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
    var hasSearchedState by mutableStateOf(false)

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
                    hasSearched = hasSearchedState,
                    onSearch = { searchArticles() },
                    onArticleClick = { article ->
                        startActivity(ArticleActivity.intent(this, article))
                    }
                )
            }
        }
    }

    private fun searchArticles() {
        hideKeyboard()
        if (queryState.isBlank()) return
        isLoadingState = true
        hasSearchedState = true

        disposables.add(
            articleClient.search(queryState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate {
                    isLoadingState = false
                }
                .subscribe(
                    { articles ->
                        articlesState = articles
                        queryState = ""
                    },
                    { error ->
                        toast("エラー: $error")
                    }
                )
        )
    }

    private fun hideKeyboard() {
        WindowCompat.getInsetsController(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        currentFocus?.let { view ->
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.decorView.windowToken?.let { token ->
            imm?.hideSoftInputFromWindow(token, 0)
        }
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
    hasSearched: Boolean,
    onSearch: () -> Unit,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val handleSearch = {
        focusManager.clearFocus(force = true)
        keyboardController?.hide()
        onSearch()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
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
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { handleSearch() })
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = handleSearch,
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
            } else if (hasSearched && articles.isEmpty()) {
                Text(
                    text = "該当する記事が見つかりませんでした",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("empty_state")
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
