package com.quizmaster.quizzed.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import com.quizmaster.quizzed.R
import com.quizmaster.quizzed.adapters.QuizAdapter
import com.quizmaster.quizzed.databinding.ActivityMainBinding
import com.quizmaster.quizzed.models.quiz
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val quizList = mutableListOf<quiz>()
    lateinit var adapters: QuizAdapter
    lateinit var firestore: FirebaseFirestore
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()

    }

    private fun populateDummyData() { quizList.add(quiz("12-11-23","12-11-23"))
      // quizList.add(quiz("12-10-23","12-10-23"))
      //  quizList.add(quiz("13-10-23","13-10-23"))
      //  quizList.add(quiz("14-10-23","14-10-23"))
     // quizList.add(quiz("15-10-23","15-10-23"))
    //quizList.add(quiz("16-10-23","16-10-23"))
   }

    fun setUpViews(){
        setUpFirestore()
        setUpDrawerLayout()
        setUpRecyclerView()
        setUpDatePicker()
    }
    private fun setUpDatePicker() {
        binding.btnDatePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)
                val dateFormatter = SimpleDateFormat("dd-mm-yyyy")
                val date = dateFormatter.format(Date(it))
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("DATE", date)
                startActivity(intent)
            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)

            }
            datePicker.addOnCancelListener {
                Log.d("DATEPICKER", "Date Picker Cancelled")
            }
        }

    }
    private fun setUpFirestore() {
        firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("quizzes")
        collectionReference.addSnapshotListener{ value, error ->
            if(value == null || error != null){
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("DATA", value.toObjects(quiz::class.java).toString())
            quizList.clear()
            quizList.addAll(value.toObjects(quiz::class.java))
            adapters.notifyDataSetChanged()

        }
    }

    private fun setUpRecyclerView() {
        adapters = QuizAdapter(this, quizList)
        binding.quizRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.quizRecyclerView.adapter = adapters
    }

    fun setUpDrawerLayout(){
        setSupportActionBar(binding.appBar)
        actionBarDrawerToggle= ActionBarDrawerToggle(this,binding.mainDrawer,
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
            binding.mainDrawer.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
            return super.onOptionsItemSelected(item)

    }
}