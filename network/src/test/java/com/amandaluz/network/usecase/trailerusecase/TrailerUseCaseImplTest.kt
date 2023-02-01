package com.amandaluz.network.usecase.trailerusecase

import com.amandaluz.core.util.url.language
import com.amandaluz.network.model.trailer.TrailerResponse
import com.amandaluz.network.repository.trailerrepository.TrailerRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

@ExperimentalCoroutinesApi
class TrailerUseCaseImplTest{

    private val repository = mock(TrailerRepository::class.java)
    private val useCase = TrailerUseCaseImpl(repository)


    @Test
    fun `should returns trailerResponse when getTrailerMovies is called`() = runTest{
        val trailerResponse = TrailerResponse(1, listOf())
        val response = Response.success(200, trailerResponse)
        val results = response.body()?.results

        //Arrange
        `when`(repository.getTrailer("", "", 1)).thenReturn(response)

        //Act
        val result = useCase.getTrailerMovie("", "", 1)

        //Assert
        Assert.assertEquals(results, result)
    }

    @Test(expected = Exception::class)
    fun `should exception when response code 200 with null body`() = runTest{
        val responseNullBody = Response.success<TrailerResponse>(200, null)
        val expected = Exception()

        //Arrange
        `when`(repository.getTrailer("", language(), 1)).thenReturn(responseNullBody)

        //Act
        val result = useCase.getTrailerMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when getTrailerMovie then throw HttpError `() = runTest {
        val mockResponse: Response<TrailerResponse> = Response.error(400, "".toResponseBody())
        val expected = Exception("HttpError_trailerMovie")

        //Arrange
        `when`(mockResponse.code()).thenReturn(400, 500)
        `when`(repository.getTrailer("", language(), 1)).thenReturn(mockResponse)

        //Act
        val result = useCase.getTrailerMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when getTrailerMovie then throw no content(204) `() = runTest {
        val mockResponseBody = mock(ResponseBody::class.java)
        val mockResponseNoContent: Response<TrailerResponse> = Response.error(204, mockResponseBody)
        val expected = Exception("No content")

        //Arrange
        `when`(mockResponseNoContent.code()).thenReturn(204)
        `when`(repository.getTrailer("", language(), 1)).thenReturn(mockResponseNoContent)

        //Act
        val result = useCase.getTrailerMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `should exception when getPopularMovie then throw 550`() = runTest {
        val mockResponseBody = mock(ResponseBody::class.java)
        val mockResponseNoContent: Response<TrailerResponse> = Response.error(550, mockResponseBody)
        val expected = IllegalArgumentException()

        //Arrange
        `when`(mockResponseNoContent.code()).thenReturn(550)
        `when`(repository.getTrailer("", language(), 1)).thenReturn(mockResponseNoContent)

        //Act
        val result = useCase.getTrailerMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

}