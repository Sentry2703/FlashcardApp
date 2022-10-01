package com.example.flashcardapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

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
        val addQuestion = findViewById<ImageView>(R.id.add_question_button)

        flashcardQuestion.visibility = View.VISIBLE
        flashcardAnswer.visibility = View.INVISIBLE

        flashcardQuestion.setOnClickListener {
            Snackbar.make(flashcardQuestion, "Question button was clicked", Snackbar.LENGTH_SHORT).show()
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

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) {
                val questionString = data.getStringExtra("QUESTION")
                val answerString = data.getStringExtra("ANSWER")

                flashcardQuestion.text = questionString
                flashcardAnswer.text = answerString

                Log.i("Tunji: MainActivity", "question: $questionString")
            } else {
                Log.i("Tunji:MainActivity", "returned null")
            }
        }

        addQuestion.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)

        }
    }
}