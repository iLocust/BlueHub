package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.myapplication.api.MovieDetailService
import com.example.myapplication.model.MovieDetail
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Movie
import com.google.gson.Gson

@Composable
fun ItemDetailPage(navController: NavController, item: Int, viewModel: MainViewModel,isLoggedIn: MutableState<Boolean>) {
    val movieDetailService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MovieDetailService::class.java)

    var movieDetail by remember { mutableStateOf<MovieDetail?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var isInWishlist by remember { mutableStateOf(false) }

    LaunchedEffect(item) {
        try {
            movieDetail = movieDetailService.getMovieDetail(item)
            val movieDetailJson = Gson().toJson(movieDetail)
            Log.d("MovieDetail", movieDetailJson)
            isInWishlist = viewModel.wishlistMovies.value?.any { it.id == item } ?: false
        } catch (e: Exception) {
            error = e.localizedMessage
            Log.e("MovieDetail", "Error: $error", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            if (error != null) {
                item {
                    Text("Error: $error")
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (error != null) {
                    item {
                        Text("Error: $error")
                    }
                } else if (movieDetail != null) {
                    item {
                        Image(
                            painter = rememberImagePainter(
                                data = "https://image.tmdb.org/t/p/w500${movieDetail?.poster_path}",
                                builder = {
                                    crossfade(true)
                                }
                            ),
                            contentDescription = "Movie Poster",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.75F),
                            contentScale = ContentScale.Crop
                        )
                    }

                    item {
                        if (isLoggedIn.value) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .background(
                                        color = Color.Red,
                                        shape = MaterialTheme.shapes.medium
                                    )
                            ) {
                            if (isInWishlist) {
                                IconButton(
                                    onClick = {
                                        val movie = Movie(
                                            id = movieDetail?.id ?: 0,
                                            title = movieDetail?.title.orEmpty(),
                                            poster_path = movieDetail?.poster_path.orEmpty(),
                                            release_date = movieDetail?.release_date.orEmpty(),
                                            vote_average = movieDetail?.vote_average ?: 0f,
                                        )
                                        viewModel.removeMovieFromWishlist(movie)
                                        isInWishlist = false
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            color = Color.Red,
                                            shape = MaterialTheme.shapes.medium
                                        ),
                                ) {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Remove from Wishlist",
                                        tint = Color.White
                                    )
                                }
                                Text(
                                    text = "Remove from Wishlist",
                                    style = MaterialTheme.typography.subtitle1.copy(fontSize = 20.sp),
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                            } else {
                                IconButton(
                                    onClick = {
                                        val movie = Movie(
                                            id = movieDetail?.id ?: 0,
                                            title = movieDetail?.title.orEmpty(),
                                            poster_path = movieDetail?.poster_path.orEmpty(),
                                            release_date = movieDetail?.release_date.orEmpty(),
                                            vote_average = movieDetail?.vote_average ?: 0f,
                                        )
                                        viewModel.addMovieToWishlist(movie)
                                        isInWishlist = true
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            color = Color.Red,
                                            shape = MaterialTheme.shapes.medium
                                        ),
                                ) {
                                    Icon(
                                        Icons.Filled.FavoriteBorder,
                                        contentDescription = "Add to Wishlist",
                                        tint = Color.White
                                    )
                                }
                                Text(
                                    text = "Add to Wishlist",
                                    style = MaterialTheme.typography.subtitle1.copy(fontSize = 20.sp),
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }


                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item {
                        Text(
                            text = "Release Date : " + movieDetail?.release_date.orEmpty(),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item {
                        Text(
                            text = "Vote : " + movieDetail?.vote_average.toString().orEmpty(),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Text(
                            text = "Description : ",
                            style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                            )
                        Text(
                            text = movieDetail?.overview.orEmpty(),
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }

                }
            }
        }
        TopAppBar(
            title = { Text(movieDetail?.title.orEmpty()) }, // Menggunakan nama film sebagai judul
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
