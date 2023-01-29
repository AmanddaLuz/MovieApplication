package com.amandaluz.network.repository.movierepository

import com.amandaluz.core.util.language
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.service.MovieApi
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieRepositoryImplTest{
    private val api = mock(MovieApi::class.java)
    private lateinit var movieRepository: MovieRepository

    @Before
    fun setup() {
        movieRepository = MovieRepositoryImpl(api)
    }

    @Test
    fun `should success response when called`(): Unit = runBlocking {
        //Arrange
        val expected: Response<MovieResponse> = Response.success(200, MovieResponse(1, listOf(), 1, 1))
        `when`(api.getPopularMovies("", "", 1)).thenReturn(expected)

        //Act
        val result = movieRepository.getMovie("", "", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
        verify(api).getPopularMovies("", "", 1)
    }


    @Test
    fun `should error response when called`(): Unit = runBlocking {
        //Arrange
        val expectedError = Response.error<MovieResponse>(400, "".toResponseBody())
        `when`(api.getPopularMovies("", language(), 1)).thenReturn(expectedError)

        //Act
        val result = movieRepository.getMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expectedError)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should return exception when called`(): Unit = runBlocking {
        //Arrange
        val exception = IllegalArgumentException()
        `when`(api.getPopularMovies("", language(), 1)).thenThrow(exception)

        //Act
        val result = movieRepository.getMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(exception)
    }

}