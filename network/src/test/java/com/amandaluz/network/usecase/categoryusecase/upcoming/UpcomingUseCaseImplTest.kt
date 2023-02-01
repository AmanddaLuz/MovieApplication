package com.amandaluz.network.usecase.categoryusecase.upcoming

import com.amandaluz.core.util.url.language
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.repository.categoryrepository.upcoming.UpcomingRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.Response

@ExperimentalCoroutinesApi
class UpcomingUseCaseImplTest{
    private val repository = Mockito.mock(UpcomingRepository::class.java)
    private val useCase = UpcomingUseCaseImpl(repository)

    @Test
    fun `should returns movieResponse when getTopRate is called` () = runTest {
        val upcomingResponse = MovieResponse(1, listOf(), 1, 1)
        val response = Response.success(200, upcomingResponse)
        val results = response.body()
        //Arrange
        `when`(repository.getUpcoming("", language(), 1)).thenReturn(response)

        //Act
        val result = useCase.getUpcoming("",1)

        //Assert
        Assert.assertEquals(results, result)
    }

    @Test(expected = Exception::class)
    fun `should exception when response code 200 with null body`() = runTest {
        val responseNullBody = Response.success<MovieResponse>(200, null)
        val expected = Exception()

        //Arrange
        `when`(repository.getUpcoming("", language(), 1)).thenReturn(responseNullBody)

        //Act
        val result = useCase.getUpcoming("", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when getPopularMovie then throw HttpError `() = runTest {
        val mockResponse: Response<MovieResponse> = Response.error(400, "".toResponseBody())
        val expected = Exception("HttpError_upcomingMovie")

        //Arrange
        `when`(mockResponse.code()).thenReturn(400, 500)
        `when`(repository.getUpcoming("", language(), 1)).thenReturn(mockResponse)

        //Act
        val result = useCase.getUpcoming("", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when getPopularMovie then throw no content `() = runTest {
        val mockResponseBody = Mockito.mock(ResponseBody::class.java)
        val mockResponseNoContent: Response<MovieResponse> = Response.error(204, mockResponseBody)
        val expected = Exception("No content")

        //Arrange
        `when`(mockResponseNoContent.code()).thenReturn(204)
        `when`(repository.getUpcoming("", language(), 1)).thenReturn(mockResponseNoContent)

        //Act
        val result = useCase.getUpcoming("", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when getPopularMovie then throw 550`() = runTest {
        val mockResponseBody = Mockito.mock(ResponseBody::class.java)
        val mockResponseNoContent: Response<MovieResponse> = Response.error(550, mockResponseBody)
        val expected = IllegalArgumentException()

        //Arrange
        `when`(mockResponseNoContent.code()).thenReturn(550)
        `when`(repository.getUpcoming("", language(), 1)).thenReturn(mockResponseNoContent)

        //Act
        val result = useCase.getUpcoming("", 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }
}