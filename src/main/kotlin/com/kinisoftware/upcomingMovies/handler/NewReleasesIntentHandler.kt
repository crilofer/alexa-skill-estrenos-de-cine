package com.kinisoftware.upcomingMovies.handler

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.handler.RequestHandler
import com.amazon.ask.model.IntentRequest
import com.amazon.ask.model.Response
import com.amazon.ask.request.Predicates
import com.kinisoftware.upcomingMovies.DirectiveServiceHandler
import com.kinisoftware.upcomingMovies.MoviesGetter
import com.kinisoftware.upcomingMovies.Translations
import com.kinisoftware.upcomingMovies.getLanguage
import java.util.Optional


class NewReleasesIntentHandler(private val moviesGetter: MoviesGetter) : RequestHandler {

    override fun canHandle(input: HandlerInput): Boolean {
        return input.matches(Predicates.intentName("NewReleasesIntent"))
    }

    override fun handle(input: HandlerInput): Optional<Response> {
        val request = input.requestEnvelope.request
        val intentRequest = request as IntentRequest
        val intent = intentRequest.intent
        val slots = intent.slots
        val releasesDate = slots["releasesDate"]!!
        val dateValue = releasesDate.value

        DirectiveServiceHandler(input).onRequestingUpcomings(input.getLanguage())
        val movies = moviesGetter.getUpcomings(intentRequest.locale, dateValue)

        return if (movies.isBlank()) {
            val text = Translations.getMessage(input.getLanguage(), Translations.TranslationKey.UPCOMINGS_NOT_FOUND)
            val reprompt = Translations.getMessage(input.getLanguage(), Translations.TranslationKey.ASKING_FOR_NOW_PLAYING)
            input.responseBuilder
                    .withSpeech(text + reprompt)
                    .withReprompt(reprompt)
                    .build()
        } else {
            val text = "${Translations.getMessage(input.getLanguage(), Translations.TranslationKey.UPCOMINGS_RESPONSE)}: $movies"
            input.responseBuilder
                    .withSpeech(text)
                    .withShouldEndSession(true)
                    .build()
        }
    }
}
