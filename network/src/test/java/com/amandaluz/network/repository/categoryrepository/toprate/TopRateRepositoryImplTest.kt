package com.amandaluz.network.repository.categoryrepository.toprate

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
class TopRateRepositoryImplTest{
    private val api = mock(MovieApi::class.java)
    private var topRateRepository = TopRateRepositoryImpl(api)

    @Test
    fun `should success response when topRateRepository is called`(): Unit = runBlocking {
        val topRateResponse = MovieResponse(1, listOf(), 1, 1)
        val expected: Response<MovieResponse> = Response.success(200, topRateResponse)

        //Arrange
        `when`(api.getTopRatedMovies("", "", 1)).thenReturn(expected)

        //Act
        val result = topRateRepository.getTopRate("", "", 1)

        //
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should error response when topRateRepository is called`(): Unit = runBlocking {
        val expectedError = Response.error<MovieResponse>(400, "".toResponseBody())
        //Arrange
        `when`(api.getTopRatedMovies("","", 1)).thenReturn(expectedError)

        //Act
        val result = topRateRepository.getTopRate("", "", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expectedError)
    }

    @Test(expected = Exception::class)
    fun `should return exception when topRateRepository is called`(): Unit = runBlocking {
        val exception = Exception()

        //Arrange
        `when`(api.getTopRatedMovies("", "", 1)).thenThrow(exception)

        //Act
        val result = topRateRepository.getTopRate("", "", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(exception)
    }
}