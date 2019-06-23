package com.kinisoftware.upcomingMovies.interceptor

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor
import com.amazon.ask.model.Response
import com.google.gson.Gson
import java.util.Optional

class LogResponseInterceptor : ResponseInterceptor {
    override fun process(input: HandlerInput, response: Optional<Response>) {
        response.ifPresent { println("Response: ${Gson().toJson(it)}") }
    }
}