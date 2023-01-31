package com.amandaluz.network.repository.searchrepository

import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.service.MovieApi
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

@ExperimentalCoroutinesApi
internal class SearchRepositoryImplTest{
    private val api = mock(MovieApi::class.java)
    private var movieRepository = SearchRepositoryImpl(api)

    @Test
    fun `should success response when searchRepository is called`(): Unit = runBlocking {
        //Arrange
        val expected: Response<MovieResponse> = Response.success(200, MovieResponse(1, listOf(), 1, 1))
        `when`(api.searchMovies("", "", 1, "")).thenReturn(expected)

        //Act
        val result = movieRepository.getSearch("", "", 1, "")

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }


    @Test
    fun `should error response when searchRepository is called`(): Unit = runBlocking {
        //Arrange
        val expectedError = Response.error<MovieResponse>(400, "".toResponseBody())
        `when`(api.searchMovies("", "", 1, "")).thenReturn(expectedError)

        //Act
        val result = movieRepository.getSearch("", "", 1, "")

        //Assert
        Truth.assertThat(result).isEqualTo(expectedError)
    }

    @Test(expected = Exception::class)
    fun `should return exception when searchRepository is called`(): Unit = runBlocking {
        val exception = Exception()

        //Arrange
        `when`(api.searchMovies("", "", 1, "")).thenThrow(exception)

        //Act
        val result = movieRepository.getSearch("", "", 1, "")

        //Assert
        Truth.assertThat(result).isEqualTo(exception)
    }

}