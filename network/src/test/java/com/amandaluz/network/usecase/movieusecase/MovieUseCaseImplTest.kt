package com.amandaluz.network.usecase.movieusecase

import com.amandaluz.core.util.language
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.repository.movierepository.MovieRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieUseCaseImplTest{

    private val repository = mock(MovieRepository::class.java)
    private val useCase = MovieUseCaseImpl(repository)
    private val mockResponse: Response<MovieResponse> = Response.error(400, "".toResponseBody())
    private val mockResponseNoContent: Response<MovieResponse> = Response.error(404, "".toResponseBody())
    /*private val mockResponseNoContent: Response<MovieResponse> = Response.error(404, ResponseBody.create(
        "application/json".toMediaTypeOrNull(), "{}"))*/


    @Test
    fun `getPopularMovie returns the expected results`() = runTest {
        val movieResponse = MovieResponse(1, listOf(), 1, 1)
        val response = Response.success(200, movieResponse)

        //Arrange
        `when`(repository.getMovie("", language(), 1)).thenReturn(response)

        //Act
        val result = useCase.getPopularMovie("", language(), 1)

        //Assert
        Assert.assertEquals(response.body()?.results, result )

    }

    @Test(expected = Exception::class)
    fun `should exception when response code 200 with null body`() = runTest {
        //Arrange
        val response = Response.success<MovieResponse>(200, null)
        val expected = Exception()

        `when`(repository.getMovie("", language(), 1)).thenReturn(response)

        //Act
        val result = useCase.getPopularMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when getPopularMovie then throw HttpError `() = runTest {
        //Arrange
        val expected = Exception("HttpError")

        `when`(mockResponse.code()).thenReturn(400, 500)
        `when`(repository.getMovie("", language(), 1)).thenReturn(mockResponse)

        //Act
        val result = useCase.getPopularMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when getPopularMovie then throw no content `() = runTest {
        //Arrange
        val expected = Exception("No content")

        `when`(mockResponseNoContent.code()).thenReturn(404)
        `when`(repository.getMovie("", language(), 1)).thenReturn(mockResponseNoContent)

        //Act
        val result = useCase.getPopularMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

}