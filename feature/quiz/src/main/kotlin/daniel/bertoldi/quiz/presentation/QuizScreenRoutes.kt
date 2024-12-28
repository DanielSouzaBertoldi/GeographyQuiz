package daniel.bertoldi.quiz.presentation

import kotlinx.serialization.Serializable

interface QuizScreenRoutes {

    @Serializable
    data object Home : QuizScreenRoutes
}