package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.myapplication.api.NowPlayingService
import com.example.myapplication.api.PopularService
import com.example.myapplication.api.GenreService

import com.example.myapplication.model.Movie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.ui.text.font.FontWeight
import com.example.myapplication.api.TopRatedService
import androidx.compose.foundation.border
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import com.example.myapplication.model.Genre
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ListPage(navController: NavController, isLoggedIn: MutableState<Boolean>) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val movieService = retrofit.create(NowPlayingService::class.java)
    val popularService = retrofit.create(PopularService::class.java)
    val topRatedService = retrofit.create(TopRatedService::class.java)
    val genreService = retrofit.create(GenreService::class.java)

    var genres by remember { mutableStateOf(listOf<Genre>()) }
    var movies by remember { mutableStateOf(listOf<Movie>()) }
    var popularMovies by remember { mutableStateOf(listOf<Movie>()) }
    var topRatedMovies by remember { mutableStateOf(listOf<Movie>()) }
    var error by remember { mutableStateOf<String?>(null) }



    LaunchedEffect(Unit) {
        try {
            genres = genreService.getMovieGenres().genres
            movies = movieService.getNowPlayingMovies().results
            popularMovies = popularService.getPopularMovies().results
            topRatedMovies = topRatedService.getTopRatedMovies().results
        } catch (e: Exception) {
            error = e.localizedMessage
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("BlueTube") },
            actions = {

                if (isLoggedIn.value) {
                    IconButton(onClick = { navController.navigate("wishlist") }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Wishlist")
                    }
                }

                if (!isLoggedIn.value) {
                    Button(onClick = {
                        navController.navigate("login")
                    }) {
                        Text("Login")
                    }
                }
            }
        )

        LazyColumn {
            if (error != null) {
                item {
                    Text("Error: $error")
                }
            } else {
                item {
                    Text(
                        text = "Now Playing",
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(movies) { movie ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(1.dp)
                                    .background(Color(0xFF6A0EAC))
                                    .clickable { navController.navigate("detail/${movie.id}") }
                            ) {
                                Column {
                                    Image(
                                        painter = rememberImagePainter(
                                            data = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                                            builder = {
                                                crossfade(true)
                                            }
                                        ),
                                        contentDescription = "Movie Poster",
                                        modifier = Modifier
                                            .size(200.dp, 300.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Popular",
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(popularMovies) { movie ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(1.dp)
                                    .background(Color(0xFF6A0EAC))
                                    .clickable { navController.navigate("detail/${movie.id}") }
                            ) {
                                Column {
                                    Image(
                                        painter = rememberImagePainter(
                                            data = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                                            builder = {
                                                crossfade(true)
                                            }
                                        ),
                                        contentDescription = "Movie Poster",
                                        modifier = Modifier
                                            .size(100.dp, 150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Top Rated",
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(topRatedMovies) { movie ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(4.dp)
                            .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { navController.navigate("detail/${movie.id}") }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Image(
                                painter = rememberImagePainter(
                                    data = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                                    builder = {
                                        crossfade(true)
                                    }
                                ),
                                contentDescription = "Movie Poster",
                                modifier = Modifier
                                    .size(100.dp, 140.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    text = movie.title,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)  // Add vertical padding here

                                )
//                                Text(
//                                    text = "Genres: ${movie.genre_ids.split(",").map { id -> genres.firstOrNull { it.id == id.toInt() }?.name }.joinToString(", ") { it ?: "" }}",
//                                    color = Color.Black,
//                                    fontSize = 14.sp,
//                                    modifier = Modifier.padding(vertical = 4.dp)  // Add vertical padding here
//                                )
                                Text(
                                    text = "Release Date: ${movie.release_date}",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)  // Add vertical padding here
                                )
                                Text(
                                    text = "Vote Average: ${movie.vote_average}",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)  // Add vertical padding here
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}