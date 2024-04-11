package com.quizmaster.quizzed.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.CollectionReference
import com.quizmaster.quizzed.adapters.OptionAdapter
import com.quizmaster.quizzed.databinding.ActivityQuestionBinding
import com.quizmaster.quizzed.models.Questions
import com.quizmaster.quizzed.models.quiz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
//import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionBinding
    private var quizzes: MutableList<quiz>? = null
    private var questions: MutableMap<String, Questions>? = null
    private var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpFirestore()
        setUpEventListener()
    }

    private fun setUpEventListener() {
        binding.btnPrevious.setOnClickListener {
            index--
            bindViews()
        }

        binding.btnNext.setOnClickListener {
            index++
            bindViews()
        }

        binding.btnSubmit.setOnClickListener {
            Log.d("QuestionActivity", "Final quiz data: $questions")

            val intent = Intent(this, ResultActivity::class.java)
            val json = Gson().toJson(quizzes!![0])
            intent.putExtra("QUIZ", json)
            startActivity(intent)
        }
    }

    private fun setUpFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        var date = intent.getStringExtra("DATE")
        if (date != null) {
            firestore.collection("quizzes").whereEqualTo("title", date)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null && !documents.isEmpty) {
                        quizzes = documents.toObjects(quiz::class.java)
                        questions = quizzes!![0].questions
                        bindViews()

                        // Log the fetched questions
                        Log.d("QuestionActivity", "Fetched questions: $questions")
                    } else {
                        // Log if no documents are found
                        Log.d("QuestionActivity", "No documents found.")
                    }
                }
                .addOnFailureListener { e ->
                    // Log any errors that occur during the fetch
                    Log.e("QuestionActivity", "Error fetching documents: ", e)
                }
        }
    }

    private fun bindViews() {
        Log.d("QuestionActivity", "bindViews() called")
        binding.btnPrevious.visibility = View.GONE
        binding.btnSubmit.visibility = View.GONE
        binding.btnNext.visibility = View.GONE

        if (index == 1) { //first question
            binding.btnNext.visibility = View.VISIBLE
        } else if (index == questions!!.size) { // last question
            binding.btnSubmit.visibility = View.VISIBLE
            binding.btnPrevious.visibility = View.VISIBLE
        } else { // Middle
            binding.btnPrevious.visibility = View.VISIBLE
            binding.btnNext.visibility = View.VISIBLE
        }

        val question = questions!!["question$index"]
        question?.let {
            binding.description.text = it.description
            val optionAdapter = OptionAdapter(this, it)
            binding.optionList.layoutManager = LinearLayoutManager(this)
            binding.optionList.adapter = optionAdapter
            binding.optionList.setHasFixedSize(true)
        }
    }
}