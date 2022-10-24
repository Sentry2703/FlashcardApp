package com.example.flashcardapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.security.AccessController.getContext
import kotlin.math.hypot

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
            //Snackbar.make(flashcardQuestion, "Question button was clicked", Snackbar.LENGTH_SHORT).show()
            val centerX = flashcardAnswer.width / 2
            val centerY = flashcardAnswer.height / 2
            val finalRad = hypot(centerX.toDouble(), centerY.toDouble()).toFloat()
            val circleAnim = ViewAnimationUtils.createCircularReveal(flashcardAnswer, centerX, centerY, 0f, finalRad)
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
            circleAnim.duration = 3000
            circleAnim.start()
        }

        flashcardAnswer.setOnClickListener(){
            val centerX = flashcardQuestion.width / 2
            val centerY = flashcardQuestion.height / 2
            val finalRad = hypot(centerX.toDouble(), centerY.toDouble()).toFloat()
            val circleAnim = ViewAnimationUtils.createCircularReveal(flashcardQuestion, centerX, centerY, 0f, finalRad)
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
            circleAnim.duration = 3000
            circleAnim.start()
        }

        val correctAnswer = findViewById<TextView>(R.id.answer)
        val wrongAnswer1 = findViewById<TextView>(R.id.wrong_answer_1)
        val wrongAnswer2 = findViewById<TextView>(R.id.wrong_answer_2)
        val backgroundInteract = findViewById<RelativeLayout>(R.id.background)

        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[1].question
            flashcardAnswer.text = allFlashcards[1].answer
            correctAnswer.text = allFlashcards[1].answer
            wrongAnswer1.text = allFlashcards[1].wrongAnswer1
            wrongAnswer2.text = allFlashcards[1].wrongAnswer2
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
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

        val editQuestion = findViewById<ImageView>(R.id.edit_question_button)
        editQuestion.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("question", flashcardQuestion.text)
            intent.putExtra("answer", flashcardAnswer.text)
            intent.putExtra("wrong1", wrongAnswer1.text)
            intent.putExtra("wrong2", wrongAnswer2.text)
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.top_in, R.anim.bottom_out)
            flashcardDatabase.deleteCard(flashcardQuestion.text.toString())
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        }

        val nextQuestion = findViewById<ImageView>(R.id.next_question_button)

        val leftOutAnim = AnimationUtils.loadAnimation(this.applicationContext, R.anim.left_out)
        val rightInAnim = AnimationUtils.loadAnimation(this.applicationContext, R.anim.right_in)
        val leftInAnim = AnimationUtils.loadAnimation(this.applicationContext, R.anim.left_in)
        val rightOutAnim = AnimationUtils.loadAnimation(this.applicationContext, R.anim.right_out)

        nextQuestion.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                return@setOnClickListener
            }


            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.startAnimation(leftOutAnim)
            correctAnswer.startAnimation(leftOutAnim)
            wrongAnswer1.startAnimation(leftOutAnim)
            wrongAnswer2.startAnimation(leftOutAnim)

            leftOutAnim.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (currCardDisplayedIndex >= allFlashcards.size) {
                        currCardDisplayedIndex = 0
                    }


                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()

                    val question = allFlashcards[currCardDisplayedIndex].question
                    val answer = allFlashcards[currCardDisplayedIndex].answer
                    val wrong1 = allFlashcards[currCardDisplayedIndex].wrongAnswer1
                    val wrong2 = allFlashcards[currCardDisplayedIndex].wrongAnswer2

                    currCardDisplayedIndex++

                    flashcardQuestion.text = question
                    flashcardAnswer.text = answer
                    correctAnswer.text = answer
                    wrongAnswer1.text = wrong1
                    wrongAnswer2.text = wrong2


                    flashcardQuestion.startAnimation(rightInAnim)
                    correctAnswer.startAnimation(rightInAnim)
                    wrongAnswer1.startAnimation(rightInAnim)
                    wrongAnswer2.startAnimation(rightInAnim)

                    flashcardQuestion.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })


        }

        val previousQuestion = findViewById<ImageView>(R.id.previous_question_button)
        previousQuestion.setOnClickListener {

            if (allFlashcards.isEmpty()) {
                return@setOnClickListener
            }

            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.startAnimation(rightOutAnim)
            correctAnswer.startAnimation(rightOutAnim)
            wrongAnswer1.startAnimation(rightOutAnim)
            wrongAnswer2.startAnimation(rightOutAnim)

            rightOutAnim.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (currCardDisplayedIndex < 0) {
                        currCardDisplayedIndex = (allFlashcards.size - 1)
                    }

                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()

                    val question = allFlashcards[currCardDisplayedIndex].question
                    val answer = allFlashcards[currCardDisplayedIndex].answer
                    val wrong1 = allFlashcards[currCardDisplayedIndex].wrongAnswer1
                    val wrong2 = allFlashcards[currCardDisplayedIndex].wrongAnswer2

                    currCardDisplayedIndex--

                    flashcardQuestion.text = question
                    flashcardAnswer.text = answer
                    correctAnswer.text = answer
                    wrongAnswer1.text = wrong1
                    wrongAnswer2.text = wrong2

                    flashcardQuestion.visibility = View.VISIBLE
                    flashcardQuestion.startAnimation(leftInAnim)
                    correctAnswer.startAnimation(leftInAnim)
                    wrongAnswer1.startAnimation(leftInAnim)
                    wrongAnswer2.startAnimation(leftInAnim)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    TODO("Not yet implemented")
                }

            })




        }

        val deleteAllCards = findViewById<ImageView>(R.id.delete_All)

        deleteAllCards.setOnClickListener {
            flashcardDatabase.deleteAll()
            flashcardDatabase.initFirstCard()
            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        }

        val clearQuestion = findViewById<ImageView>(R.id.clear_button)
        clearQuestion.setOnClickListener {
            if (allFlashcards.size > 1) {
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                flashcardDatabase.deleteCard(allFlashcards[currCardDisplayedIndex].question)
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            }
        }
    }
}