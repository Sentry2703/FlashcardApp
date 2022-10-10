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

lateinit var flashcardDatabase: FlashcardDatabase
var allFlashcards = mutableListOf<Flashcard>()

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase= FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        if (allFlashcards.size == 0) {
            flashcardDatabase.initFirstCard()
        }

        val flashcardQuestion = findViewById<TextView>(R.id.question1)
        val flashcardAnswer = findViewById<TextView>(R.id.toggle_answer)

        var currCardDisplayedIndex = 0

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

        val correctAnswer = findViewById<TextView>(R.id.answer)
        val wrongAnswer1 = findViewById<TextView>(R.id.wrong_answer_1)
        val wrongAnswer2 = findViewById<TextView>(R.id.wrong_answer_2)
        val backgroundInteract = findViewById<RelativeLayout>(R.id.background)

        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
            wrongAnswer1.text = allFlashcards[0].wrongAnswer1
            wrongAnswer2.text = allFlashcards[0].wrongAnswer2
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
                val question = data.getStringExtra("question")
                val answer = data.getStringExtra("answer")
                val wrong1 = data.getStringExtra("wrong1")
                val wrong2 = data.getStringExtra("wrong2")

                flashcardQuestion.text = question
                flashcardAnswer.text = answer
                correctAnswer.text = answer
                wrongAnswer1.text = wrong1
                wrongAnswer2.text = wrong2


                if (question != null && answer != null) {
                    flashcardDatabase.insertCard(Flashcard(question, answer,wrong1,wrong2))
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                }
                Log.i("Tunji: MainActivity", "question: $question")
            } else {
                Log.i("Tunji:MainActivity", "returned null")
            }
        }

        val addQuestion = findViewById<ImageView>(R.id.add_question_button)
        addQuestion.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

        val editQuestion = findViewById<ImageView>(R.id.edit_question_button)
        editQuestion.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("question", flashcardQuestion.text)
            intent.putExtra("answer", flashcardAnswer.text)
            intent.putExtra("wrong1", wrongAnswer1.text)
            intent.putExtra("wrong2", wrongAnswer2.text)
            resultLauncher.launch(intent)
            flashcardDatabase.deleteCard(flashcardQuestion.text.toString())
        }

        val nextQuestion = findViewById<ImageView>(R.id.next_question_button)
        nextQuestion.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                return@setOnClickListener
            }

            currCardDisplayedIndex++

            if (currCardDisplayedIndex >= allFlashcards.size) {
                currCardDisplayedIndex = 0
            }

            allFlashcards = flashcardDatabase.getAllCards().toMutableList()

            val question = allFlashcards[currCardDisplayedIndex].question
            val answer = allFlashcards[currCardDisplayedIndex].answer
            val wrong1 = allFlashcards[currCardDisplayedIndex].wrongAnswer1
            val wrong2 = allFlashcards[currCardDisplayedIndex].wrongAnswer2

            flashcardQuestion.text = question
            flashcardAnswer.text = answer
            correctAnswer.text = answer
            wrongAnswer1.text = wrong1
            wrongAnswer2.text = wrong2
        }

        val previousQuestion = findViewById<ImageView>(R.id.previous_question_button)
        previousQuestion.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                return@setOnClickListener
            }

            currCardDisplayedIndex--

            if (currCardDisplayedIndex < 0) {
                currCardDisplayedIndex = (allFlashcards.size - 1)
            }

            allFlashcards = flashcardDatabase.getAllCards().toMutableList()

            val question = allFlashcards[currCardDisplayedIndex].question
            val answer = allFlashcards[currCardDisplayedIndex].answer
            val wrong1 = allFlashcards[currCardDisplayedIndex].wrongAnswer1
            val wrong2 = allFlashcards[currCardDisplayedIndex].wrongAnswer2

            flashcardQuestion.text = question
            flashcardAnswer.text = answer
            correctAnswer.text = answer
            wrongAnswer1.text = wrong1
            wrongAnswer2.text = wrong2
        }

        val deleteAllCards = findViewById<ImageView>(R.id.delete_All)

        deleteAllCards.setOnClickListener {
            flashcardDatabase.deleteAll()
            flashcardDatabase.initFirstCard()
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        }
    }
}