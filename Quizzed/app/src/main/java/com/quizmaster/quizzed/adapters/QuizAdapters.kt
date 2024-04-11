package com.quizmaster.quizzed.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.quizmaster.quizzed.R
import com.quizmaster.quizzed.Utils.ColorPicker
import com.quizmaster.quizzed.Utils.IconPicker
import com.quizmaster.quizzed.activity.QuestionActivity

import com.quizmaster.quizzed.models.quiz

class QuizAdapter(val context: Context, val quizzes: List<quiz>) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quiz_item, parent, false)
        return QuizViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.textViewTitle.text = quizzes[position].title
        holder.iconView.setOnClickListener{
            Toast.makeText(context, quizzes[position].title, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra("DATE", quizzes[position].title)
            context.startActivity(intent)
        }
        try {
            val colorString = ColorPicker.getColor()
            val color = Color.parseColor(colorString)
            holder.cardContainer.setCardBackgroundColor(color)
        } catch (e: IllegalArgumentException) {
            // Handle the error, for example, set a default color
            //holder.cardContainer.setCardBackgroundColor(Color.WHITE)
        }
        holder.iconView.setImageResource(IconPicker.getIcon())

    }

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.quizTitle)
        var iconView: ImageView = itemView.findViewById(R.id.quizIcon)
        var cardContainer: CardView = itemView.findViewById(R.id.cardContainer)
    }
}