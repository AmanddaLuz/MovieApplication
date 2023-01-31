package com.amandaluz.network.usecase.searchusecase

import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.repository.searchrepository.SearchRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
internal class SearchMoviesUseCaseImplTest {
    private val repository = mock(SearchRepository::class.java)
    private val useCase = SearchMoviesUseCaseImpl(repository)

    @Test
    fun `should returns results when searchMovies is called`() = runTest {
        val movieResponse = MovieResponse(1, listOf(), 1, 1)
        val response = Response.success(200, movieResponse)
        val results = response.body()?.results
        //Arrange
        `when`(repository.getSearch("", "", 1, "")).thenReturn(response)

        //Act
        val result = useCase.getSearch("", "", 1, "")

        //Assert
        Assert.assertEquals(results, result)

    }

    @Test(expected = Exception::class)
    fun `should exception when response code 200 with null body`() = runTest {
        val responseNullBody = Response.success<MovieResponse>(200, null)
        val expected = Exception()

        //Arrange
        `when`(repository.getSearch("", "", 1, "")).thenReturn(responseNullBody)

        //Act
        val result = useCase.getSearch("", "", 1, "")

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = HttpException::class)
    fun `should exception when searchMovies then throw HttpException `() = runTest {
        val mockResponse: Response<MovieResponse> = Response.error(400, "".toResponseBody())
        val expected = HttpException(mockResponse)

        //Arrange
        `when`(mockResponse.code()).thenReturn(400, 500)
        `when`(repository.getSearch("", "", 1, "")).thenReturn(mockResponse)

        //Act
        val result = useCase.getSearch("", "", 1, "")

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should exception when searchMovies then throw IllegalArgumentException`() = runTest {
        val mockResponseBody = mock(ResponseBody::class.java)
        val mockResponseNoContent: Response<MovieResponse> = Response.error(204, mockResponseBody)
        val expected = IllegalArgumentException()

        //Arrange
        `when`(mockResponseNoContent.code()).thenReturn(204)
        `when`(repository.getSearch("", "", 1, "")).thenReturn(mockResponseNoContent)

        //Act
        val result = useCase.getSearch("", "", 1, "")

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when searchMovies then throw 550`() = runTest {
        val mockResponseBody = mock(ResponseBody::class.java)
        val mockResponseNoContent: Response<MovieResponse> = Response.error(550, mockResponseBody)
        val expected = IllegalArgumentException()

        //Arrange
        `when`(mockResponseNoContent.code()).thenReturn(550)
        `when`(repository.getSearch("", "", 1, "")).thenReturn(mockResponseNoContent)

        //Act
        val result = useCase.getSearch("", "", 1, "")

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

}