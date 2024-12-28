package daniel.bertoldi.quiz.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.quizDestinations() {

    composable<QuizScreenRoutes.Home> {
        val viewModel = hiltViewModel<QuizViewModel>()
        val text = viewModel.text.collectAsStateWithLifecycle()
        val model = viewModel.currentModel.collectAsStateWithLifecycle().value

        LaunchedEffect(key1 = Unit) {
            viewModel.start()
        }

        Column {
            if (model != null) Text(model.question.text) else text.value

            Row {
                repeat(4) { num ->
                    TextButton(onClick = { viewModel.optionChosen(num) }) {
                        Text(text = "${num.inc()}")
                    }
                }
            }
        }
    }
}

fun NavController.navigateToQuizGame() {
    navigate(route = QuizScreenRoutes.Home)
}