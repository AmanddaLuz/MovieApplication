package com.amandaluz.network.repository.trailerrepository

import com.amandaluz.core.util.url.language
import com.amandaluz.network.model.trailer.TrailerResponse
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
class TrailerRepositoryImplTest{
    private val api = mock(MovieApi::class.java)
    private var trailerRepository: TrailerRepository = TrailerRepositoryImpl(api)

    @Test
    fun `should success response when trailerRepository is called`(): Unit = runBlocking {
        val expected: Response<TrailerResponse> = Response.success(200, TrailerResponse(1, listOf()))

        //Arrange
        `when`(api.getTrailerMovies(1, "", language())).thenReturn(expected)

        //Act
        val result = trailerRepository.getTrailer("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should error response when trailerRepository is called`(): Unit = runBlocking {
        val expectedError = Response.error<TrailerResponse>(400, "".toResponseBody())
        //Arrange
        `when`(api.getTrailerMovies(1, "", "")).thenReturn(expectedError)

        //Act
        val result = trailerRepository.getTrailer("", "", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expectedError)
    }

    @Test(expected = Exception::class)
    fun `should return exception when trailerRepository is called`(): Unit = runBlocking {
        val exception = Exception()

        //Arrange
        `when`(api.getPopularMovies("", "", 1)).thenThrow(exception)

        //Act
        val result = trailerRepository.getTrailer("", "", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(exception)
    }
}