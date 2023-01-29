package com.amandaluz.network.usecase.movieusecase

import com.amandaluz.core.util.language
import com.amandaluz.network.model.movie.MovieResponse
import com.amandaluz.network.repository.movierepository.MovieRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieUseCaseImplTest{

    private val repository = mock(MovieRepository::class.java)
    private val useCase = MovieUseCaseImpl(repository)

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
    fun `should exception when response code different 200 or null`() = runTest {
        //Arrange
        val response = Response.success<MovieResponse>(200, null)
        val expected = Exception()

        `when`(repository.getMovie("", language(), 1)).thenReturn(response)

        //Act
        val result = useCase.getPopularMovie("", language(), 1)

        //Assert
        Truth.assertThat(result).isEqualTo(expected)
    }

    //Todo Fazer o tratamento do throw

}