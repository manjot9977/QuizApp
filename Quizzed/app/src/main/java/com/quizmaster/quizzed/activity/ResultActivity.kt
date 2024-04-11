package com.quizmaster.quizzed.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.google.gson.Gson
import com.quizmaster.quizzed.databinding.ActivityResultBinding
import com.quizmaster.quizzed.models.quiz
import com.google.gson.JsonSyntaxException
import android.util.Log

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var quizData: quiz? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
    }

    private fun setUpViews() {
        val quizJson = intent.getStringExtra("QUIZ")
        Log.d("ResultActivity", "Quiz data received: $quizJson")
        if (!quizJson.isNullOrEmpty()) {
            try {
                quizData = Gson().fromJson(quizJson, quiz::class.java)
                Log.d("ResultActivity", "Quiz object successfully created: $quizData")
                if (quizData != null) {
                    initializeViews()
                } else {
                    showErrorAndFinish()
                }
            } catch (e: JsonSyntaxException) {
                Log.e("ResultActivity", "Error parsing quiz data: ${e.message}")
                showErrorAndFinish()
            }
        } else {
            Log.e("ResultActivity", "Quiz data is null or empty")
            showErrorAndFinish()
        }
    }

    private fun initializeViews() {
        calculateScore()
        setAnswerView()
    }

    private fun setAnswerView() {
        val builder = StringBuilder("")
        quizData?.let { quiz ->
            for (entry in quiz.questions.entries) {
                val question = entry.value
                builder.append("<font color='#18206F'><b>Question: ${question.description}</b></font><br/><br/>")
                builder.append("<font color='#009688'>Answer: ${question.answer}</font><br/><br/>")
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.txtAnswer.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
            } else {
                binding.txtAnswer.text = Html.fromHtml(builder.toString())
            }
        } ?: showErrorAndFinish()
    }

    private fun calculateScore() {
        var score = 0
        quizData?.let { quiz ->
            for (entry in quiz.questions.entries) {
                val question = entry.value
                if (question.answer == question.userAnswer) {
                    score += 10
                }
            }
            binding.txtScore.text = "Your Score : $score"
        } ?: showErrorAndFinish()
    }

    private fun showErrorAndFinish() {
        Toast.makeText(this, "Error processing quiz data", Toast.LENGTH_SHORT).show()
        finish()
    }
}
