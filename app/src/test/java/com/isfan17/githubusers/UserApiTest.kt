package com.isfan17.githubusers

import com.google.gson.Gson
import com.isfan17.githubusers.data.model.User
import com.isfan17.githubusers.data.remote.api.UserApi
import com.isfan17.githubusers.data.remote.response.SearchUserResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(MockitoJUnitRunner::class)
class UserApiTest {

    @Mock
    private lateinit var mockUserApi: UserApi

    private lateinit var mockWebServer: MockWebServer

    private val dummyUser = User(
        login = "isfan17",
        htmlUrl = "https://github.com/isfan17",
        avatarUrl = "https://avatars.githubusercontent.com/u/94845753?v=4",
        favorite = false
    )

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mockUserApi = retrofit.create(UserApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetSearchUsers() {
        val response = SearchUserResponse(1, listOf(dummyUser))
        val responseBody = Gson().toJson(response)
        mockWebServer.enqueue(MockResponse().setBody(responseBody))

        val query = "isfan17"
        runBlocking {
            val actualResponse = mockUserApi.getSearchUsers(query)
            assertEquals(response, actualResponse)
        }

        val request = mockWebServer.takeRequest()
        assertEquals("/search/users?q=$query", request.path)
        assertEquals("token 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", request.getHeader("Authorization"))
    }
}
