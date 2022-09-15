package com.example.flashcardapp

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flashcardQuestion = findViewById<TextView>(R.id.question1)
        val flashcardAnswer = findViewById<TextView>(R.id.toggle_answer)
        val correctAnswer = findViewById<TextView>(R.id.answer)
        val wrongAnswer1 = findViewById<TextView>(R.id.wrong_answer_1)
        val wrongAnswer2 = findViewById<TextView>(R.id.wrong_answer_2)
        val backgroundInteract = findViewById<RelativeLayout>(R.id.background)

        flashcardQuestion.visibility = View.VISIBLE
        flashcardAnswer.visibility = View.INVISIBLE

        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }

        flashcardAnswer.setOnClickListener(){
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }

        backgroundInteract.setOnClickListener {
            correctAnswer.setBackgroundResource(R.drawable.options_background)
            wrongAnswer1.setBackgroundResource(R.drawable.options_background)
            wrongAnswer2.setBackgroundResource(R.drawable.options_background)
        }


        correctAnswer.setOnClickListener {
            correctAnswer.setBackgroundResource(R.drawable.options_background)
            wrongAnswer1.setBackgroundResource(R.drawable.options_background)
            wrongAnswer2.setBackgroundResource(R.drawable.options_background)
            correctAnswer.setBackgroundColor(resources.getColor(R.color.Correct_Green, null))
        }

        wrongAnswer1.setOnClickListener {
            correctAnswer.setBackgroundResource(R.drawable.options_background)
            wrongAnswer1.setBackgroundResource(R.drawable.options_background)
            wrongAnswer2.setBackgroundResource(R.drawable.options_background)
            correctAnswer.setBackgroundColor(resources.getColor(R.color.Correct_Green, null))
            wrongAnswer1.setBackgroundColor(resources.getColor(R.color.Red, null))
        }

        wrongAnswer2.setOnClickListener {
            correctAnswer.setBackgroundResource(R.drawable.options_background)
            wrongAnswer1.setBackgroundResource(R.drawable.options_background)
            wrongAnswer2.setBackgroundResource(R.drawable.options_background)
            correctAnswer.setBackgroundColor(resources.getColor(R.color.Correct_Green, null))
            wrongAnswer2.setBackgroundColor(resources.getColor(R.color.Red, null))
        }


    }
}