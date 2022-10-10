package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.time.Duration

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val s1 = intent.getStringExtra("question")
        val s2 = intent.getStringExtra("answer")
        val s3 = intent.getStringExtra("wrong1")
        val s4 = intent.getStringExtra("wrong2")

        val cancelIcon = findViewById<ImageView>(R.id.cancel_button)

        val saveIcon = findViewById<ImageView>(R.id.save_button)
        cancelIcon.setOnClickListener {
            finish()
        }

        val questionEditText = findViewById<EditText>(R.id.New_Question)
        val answerEditText = findViewById<EditText>(R.id.New_Answer)
        val wrongAnswerEditText = findViewById<EditText>(R.id.New_Wrong_Answer_1)
        val wrongAnswerEditText2 = findViewById<EditText>(R.id.New_Wrong_Answer_2)

        questionEditText.setText(s1)
        answerEditText.setText(s2)
        wrongAnswerEditText.setText(s3)
        wrongAnswerEditText2.setText(s4)

        saveIcon.setOnClickListener {


            val questionString = questionEditText.text.toString()
            val answerString = answerEditText.text.toString()
            val wrongAnswer1 = wrongAnswerEditText.text.toString()
            val wrongAnswer2 = wrongAnswerEditText2.text.toString()
            val data = Intent()

            data.putExtra("question", questionString)
            data.putExtra("answer", answerString)
            data.putExtra("wrong1", wrongAnswer1)
            data.putExtra("wrong2", wrongAnswer2)

            if ((questionString != "") && (answerString != "")) {
                setResult(RESULT_OK, data)
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Require a question and a correct answer",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

        }
    }
}