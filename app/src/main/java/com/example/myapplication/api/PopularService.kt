package com.example.myapplication.api

import com.example.myapplication.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface PopularService {
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0YTQyYWZhNTI1MmIxNzA0YmJhZDQ1OTc3OWVkOTQyMCIsInN1YiI6IjY0ZTVmNGVlMDZmOTg0MDEyZDcxNjQ5MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yMmEjCr3FTm5Y38W8y2FrdzCF9iSnADzryuCuw_w-rg")
    @GET("3/movie/popular?language=en-US&page=1")
    suspend fun getPopularMovies(): MovieResponse
}