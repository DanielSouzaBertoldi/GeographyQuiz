package daniel.bertoldi.quiz.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

@Composable
internal fun QuizGameScreen(
    screenState: QuizGameScreenState,
    onChoiceClick: (OptionsResponse) -> Unit,
) {
    var showDebugInfo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (screenState) {
            is QuizGameScreenState.Loading -> {
                CircularProgressIndicator()
            }

            is QuizGameScreenState.Error -> {
                Text("oops, all berries!")
            }

            is QuizGameScreenState.Success -> {
                Row {
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = { showDebugInfo = !showDebugInfo },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            contentColor = if (showDebugInfo) Color.Green else Color.White,
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("${screenState.roundData.round}")
                    Text("+${screenState.roundData.question.points}")
                    Text("Score: 0")
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                ) {
                    Text(screenState.roundData.question.text)
                    if (showDebugInfo) {
                        Text("Difficulty: ${screenState.roundData.question.difficulty}")
                        Text("Lifeline deduction: ${screenState.roundData.question.lifelineDeduction}")
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        screenState.roundData.options.forEach {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                TextButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = ButtonDefaults.shape,
                                    colors = ButtonDefaults.buttonColors(),
                                    onClick = { onChoiceClick(it) },
                                ) {
                                    Text(it.text)
                                }

                                if (showDebugInfo) {
                                    Text(
                                        modifier = Modifier.align(Alignment.CenterStart),
                                        text = "Removable: ${it.removable}",
                                        color = Color.DarkGray,
                                    )
                                }

                                if (showDebugInfo) {
                                    Text(
                                        modifier = Modifier.align(Alignment.CenterEnd),
                                        text = "Aud. guess %: ${it.audienceGuess}",
                                        color = Color.DarkGray,
                                    )
                                }
                            }
                        }
                    }
                }

                if (showDebugInfo) {
                    Text(
                        modifier = Modifier.padding(top = 15.dp),
                        text = "Random clue: ${screenState.roundData.randomClue}",
                    )

                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "Answer: ${screenState.roundData.answer.correctValue}"
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Short description: ${screenState.roundData.answer.shortDescription}"
                    )
                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizGameScreenPreview(
    @PreviewParameter(QuizGameScreenPreviewParameterProvider::class)
    quizGameScreenState: QuizGameScreenState,
) {
    QuizGameScreen(quizGameScreenState, {})
}

class QuizGameScreenPreviewParameterProvider : PreviewParameterProvider<QuizGameScreenState> {
    override val values = sequenceOf(
        QuizGameScreenState.Loading,
        QuizGameScreenState.Error,
        QuizGameScreenState.Success(
            roundData = GeminiResponse(
                step = "IN_PROGRESS",
                round = 1,
                question = QuestionResponse(
                    text = "This is the question part",
                    difficulty = "HARD",
                    points = 20,
                    lifelineDeduction = 10,
                ),
                options = listOf(
                    OptionsResponse(text = "Option 1", removable = true, audienceGuess = 25.0f),
                    OptionsResponse(text = "Option 2", removable = false, audienceGuess = 25.0f),
                    OptionsResponse(text = "Option 3", removable = true, audienceGuess = 25.0f),
                    OptionsResponse(text = "Option 4", removable = false, audienceGuess = 25.0f),
                ),
                randomClue = "This is a random clue",
                answer = AnswerResponse(
                    correctValue = "Option 2",
                    shortDescription = "Description for option 2",
                ),
                gameAnalysis = null,
            )
        )
    )
}
