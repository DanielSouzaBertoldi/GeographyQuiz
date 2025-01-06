package daniel.bertoldi.quiz.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.quizDestinations() {

    composable<QuizScreenRoutes.Home> {
        val viewModel = hiltViewModel<QuizViewModel>()
        val screenState = viewModel.screenState.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            viewModel.start()
        }

        QuizGameScreen(screenState.value, onChoiceClick = { viewModel.optionChosen(it) })
    }
}

fun NavController.navigateToQuizGame() {
    navigate(route = QuizScreenRoutes.Home)
}