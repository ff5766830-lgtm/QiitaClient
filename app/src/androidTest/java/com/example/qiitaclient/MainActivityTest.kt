package com.example.qiitaclient

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.qiitaclient.client.ArticleClient
import io.reactivex.rxjava3.core.Observable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun 各ビューが表示されていること_ただしプログレスバーは非表示() {
        composeTestRule.onNodeWithTag("list_view").assertIsDisplayed()
        composeTestRule.onNodeWithTag("query_edit_text").assertIsDisplayed()
        composeTestRule.onNodeWithTag("search_button").assertIsDisplayed()

        composeTestRule.onNodeWithTag("progress_bar").assertDoesNotExist()
    }

    @Test
    fun 検索ボタンがタップされたら_入力されたクエリ文字列で記事検索APIを叩くこと() {
        val articleClient = mock(ArticleClient::class.java).apply {
            `when`(search("user:ngsw_taro")).thenReturn(Observable.just(listOf()))
        }
        composeTestRule.activity.articleClient = articleClient

        composeTestRule.onNodeWithTag("query_edit_text").performTextInput("user:ngsw_taro")
        composeTestRule.onNodeWithTag("search_button").performClick()

        verify(articleClient).search("user:ngsw_taro")
    }
}
