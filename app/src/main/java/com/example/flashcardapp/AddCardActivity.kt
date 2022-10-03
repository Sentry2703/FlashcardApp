package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import java.time.Duration

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        val s1 = intent.getStringExtra("question")
        val s2 = intent.getStringExtra("answer")

        val cancelIcon = findViewById<ImageView>(R.id.cancel_button)
        val saveIcon = findViewById<ImageView>(R.id.save_button)
        val questionEditText = findViewById<EditText>(R.id.New_Question)
        val answerEditText = findViewById<EditText>(R.id.New_Answer)

        questionEditText.setText(s1)
        answerEditText.setText(s2)

        cancelIcon.setOnClickListener {
            finish()
        }

        saveIcon.setOnClickListener {

            val questionString = questionEditText.text.toString()
            val answerString = answerEditText.text.toString()
            val data = Intent()
            data.putExtra("QUESTION", questionString)
            data.putExtra("ANSWER", answerString)

            setResult(RESULT_OK, data)
            finish()

        }
    }
}