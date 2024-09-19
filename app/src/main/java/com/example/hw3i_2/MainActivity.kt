package com.example.hw3i_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hw3i_2.ui.theme.Hw3i2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hw3i2Theme {
    FlashcardsQuizApp()

                }
            }
        }
    }
@Composable
fun FlashcardsQuizApp() {
    // Predefined list of questions and answers
    val questions = listOf(
        "What is the capital of France?" to "Paris",
        "What is 2 + 2?" to "4",
        "What is the largest planet in our solar system?" to "Jupiter"
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var quizComplete by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }

    val currentQuestion = questions[currentQuestionIndex].first
    val correctAnswer = questions[currentQuestionIndex].second
Scaffold(snackbarHost = {SnackbarHost(hostState = snackbarState)}) {
    padding->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!quizComplete) {
            // Question Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = currentQuestion,

                    modifier = Modifier.padding(16.dp)
                )
            }

            // Answer Input
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Your Answer") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    submitAnswer(
                        userInput,
                        correctAnswer,
                        snackbarState,
                        currentQuestionIndex,
                        questions.size,
                        { currentQuestionIndex++ },
                        { quizComplete = true }
                    )
                    userInput = "" // Clear input
                })
            )

            // Submit Button
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    submitAnswer(
                        userInput,
                        correctAnswer,
                        snackbarState,
                        currentQuestionIndex,
                        questions.size,
                        { currentQuestionIndex++ },
                        { quizComplete = true }
                    )
                    userInput = "" // Clear input
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Answer")
            }
        } else {
            // Quiz Complete
            Text("Quiz Complete!")
            Button(
                onClick = {
                    currentQuestionIndex = 0
                    quizComplete = false
                    userInput = ""
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Restart Quiz")
            }
        }

        // Snackbar for feedback
//        SnackbarHost(hostState = snackbarState)
    }
}

}

fun submitAnswer(
    userInput: String,
    correctAnswer: String,
    snackbarHostState: SnackbarHostState,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    onNextQuestion: () -> Unit,
    onQuizComplete: () -> Unit
) {
    if (userInput.equals(correctAnswer, ignoreCase = true)) {
        if (currentQuestionIndex < totalQuestions - 1) {
            onNextQuestion()
        } else {
            onQuizComplete()
        }
    }

    val msg = if (userInput.equals(correctAnswer, ignoreCase = true)) {
        "Correct!"
    } else {
        "Incorrect! The correct answer was $correctAnswer."
    }

    CoroutineScope(Dispatchers.Main).launch {
        snackbarHostState.showSnackbar(message=msg,
          duration =   SnackbarDuration.Long
       )
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardsQuizAppPreview() {
    FlashcardsQuizApp()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Hw3i2Theme {
        Greeting("Android")
    }
}