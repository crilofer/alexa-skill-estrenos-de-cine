package com.kinisoftware.upcomingMovies

import com.google.gson.Gson
import com.kinisoftware.upcomingMovies.api.TheMovieDBService
import com.kinisoftware.upcomingMovies.model.Movie
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

class MoviesGetter(gson: Gson) {

    private val theMovieDBService = TheMovieDBService(gson)

    fun getUpcomings(locale: String, releaseDate: String) = theMovieDBService.getUpcomings(locale)
            .filter { it.isReleasedOnDate(releaseDate) }
            .sortedBy { it.releaseDate }

    fun getNowPlayingMovies(locale: String) = theMovieDBService.getNowPlayingMovies(locale)

    private fun Movie.isReleasedOnDate(dateValue: String) =
            getWeekFormatStyle(releaseDate) == dateValue || getMonthFormatStyle(releaseDate) == dateValue

    private fun getWeekFormatStyle(releaseDate: String): String {
        val localDate = LocalDate.parse(releaseDate)
        val weekFields = WeekFields.of(Locale.getDefault())
        return localDate.year.toString() + "-W" + localDate.get(weekFields.weekOfWeekBasedYear())
    }

    private fun getMonthFormatStyle(releaseDate: String): String {
        val localDate = LocalDate.parse(releaseDate)
        return if (localDate.monthValue < 10)
            localDate.year.toString() + "-0" + localDate.monthValue
        else
            localDate.year.toString() + "-" + localDate.monthValue
    }
}
