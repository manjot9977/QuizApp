package com.quizmaster.quizzed.models

data class quiz(
        var id : String = "",
        var title: String = "",
        var questions: MutableMap<String, Questions> = mutableMapOf()
)

