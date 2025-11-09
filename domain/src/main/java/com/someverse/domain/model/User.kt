package com.someverse.domain.model

/**
 * User Model
 */
data class User(
    val id: String,
    val provider: SocialProvider,
    val nickname: String?,
    val birthDate: String?,
    val gender: Gender?,
    val activityLocations: List<Location>?,
    val profileImages: List<String>?,
    val primaryImageUrl: String?,
    val bio: String?,
    val job: String?,
    val favoriteMovies: List<Movie>?,
    val preferredGenres: List<Genre>?
)

/**
 * Location Model
 */
data class Location(
    val city: String,
    val district: String
)

/**
 * Movie Model (Basic Info)
 */
data class Movie(
    val movieId: Long,
    val title: String,
    val posterPath: String?
)

/**
 * Genre Model
 */
data class Genre(
    val genreId: Long,
    val name: String
)