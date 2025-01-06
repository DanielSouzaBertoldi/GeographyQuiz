package daniel.bertoldi.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.quiz.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class GameStep {
    IN_PROGRESS, FINISHED
}

private val SCHEMA = Schema(
    name = "root",
    description = "The root of the JSON",
    type = FunctionType.OBJECT,
    properties = mapOf(
        "step" to Schema.enum(
            "step",
            "The current game step",
            values = GameStep.entries.map { it.name }),
        "round" to Schema.int("round", "The number of the current game round"),
        "question" to Schema.obj(
            name = "question",
            description = "Data related to the question in this round",
            contents = arrayOf(
                Schema.str("text", "The question per se"),
                Schema.str("difficulty", "The difficulty of the question"),
                Schema.int("points", "How many points this question is worth"),
                Schema.int(
                    "lifelineDeduction",
                    "How many points can be deducted from the `points` total if the user uses a lifeline."
                ),
            ),
        ),
        "options" to Schema.arr(
            name = "options",
            description = "A list of possible answers. There should always be four choices.",
            items = Schema.obj(
                name = "items",
                description = "Data related to a given answer",
                contents = arrayOf(
                    Schema.str("text", "The answer per se"),
                    Schema.bool(
                        "removable",
                        "If the answer can be removed by using the 50/50 lifeline"
                    ),
                    Schema.double(
                        "audienceGuess",
                        "defines how much a theoretical audience is confident that this answer is correct, this value will be used in case the user uses the 'Ask the Audience' lifeline. The sum of all percentages have to equal to 100. If the question is hard, this value should be almost equally shared between answers. There is a chance that the most of the audience might vote for the wrong option for MEDIUM and HARD questions."
                    ),
                ),
            ),
        ),
        "randomClue" to Schema.str("randomClue", "A clue about the correct answer. Should be short."),
        "answer" to Schema.obj(
            name = "answer",
            description = "Data related to the correct answer",
            contents = arrayOf(
                Schema.str(
                    "correctValue",
                    "The same exact text of the correct answer in one of the possible answers inside the `options` array"
                ),
                Schema.str(
                    "shortDescription",
                    "A really short description explaining why this answer is correct compared to others."
                ),
            ),
        ),
        "gameAnalysis" to Schema(
            name = "gameAnalysis",
            description = "An in-depth game analysis of how the user went based on the last `correctAnswers` value provided, and what they should study/learn about a bit more. Feel free to suggest books, videos, websites, whatever comes to mind! Make this final moment fun and be grateful for the user's participation.",
            type = FunctionType.STRING,
            nullable = true,
        ),
    ),
    required = listOf(
        "step",
        "round",
        "question",
        "options",
        "answer"
    )
)

data class GeminiResponse(
    val step: String,
    val round: Int,
    val question: QuestionResponse,
    val options: List<OptionsResponse>,
    val randomClue: String,
    val answer: AnswerResponse,
    val gameAnalysis: String?,
)

data class QuestionResponse(
    val text: String,
    val difficulty: String,
    val points: Int,
    val lifelineDeduction: Int,
)

data class OptionsResponse(
    val text: String,
    val removable: Boolean,
    val audienceGuess: Float,
)

data class AnswerResponse(
    val correctValue: String,
    val shortDescription: String,
)

sealed class QuizGameScreenState {
    data object Loading : QuizGameScreenState()
    data object Error : QuizGameScreenState()
    data class Success(val roundData: GeminiResponse) : QuizGameScreenState()
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    moshi: Moshi,
) : ViewModel() {

    private var correctAnswers = 0
    private val _screenState = MutableStateFlow<QuizGameScreenState>(QuizGameScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        // Retrieve API key as an environmental variable defined in a Build Configuration
        // see https://github.com/google/secrets-gradle-plugin for further instructions
        apiKey = BuildConfig.geminiapi,
        generationConfig = generationConfig {
            temperature = 2f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "application/json"
            responseSchema = SCHEMA
        },
        systemInstruction = content { text("## Your Role\n\nYou are a host of a Geography Quiz Game Show and you are restricted to talk only about the geography related trivia. You love geography trivia that spans not only geographical borders but also culture, history and everything in between, and you are VERY creative with your questions. Do not talk about anything but geography related trivia, ever.\nYour goal is to provide a structured JSON response where you'll list some information about a quiz question. Only provide questions and answers to topics you are ABSOLUTELY sure is correct.\nYou can only give ONE question at a time.\n\nEach quiz game consists of 10 rounds. The user starts with a score of 0.\nThere should be exactly four answers to a given question, and only one correct answer. \nYou are free to choose how much each correct answer is worth depending on the difficulty, go crazy, but try to choose a value multiple of 5.\n\n## Your response\n\nYour response should *always* follow the structure:\n\n```json\n{\n    \"step\": string // required either IN_PROGRESS or FINISHED\n    \"round\": integer, // required\n    \"question\": { // required\n        \"text\": string, //required\n        \"difficulty\": string, //required\n        \"points\": integer, // required\n        \"lifelineDeduction\": integer\n    },\n    \"options\": [ // required\n        {\n            \"text\": string, // required\n            \"removable\": boolean, //required\n            \"audienceGuess\": number // (has to be a floating number such as 24.5, for example. All floating numbers should end with a number different than 0.)\n        },\n        {\n            \"text\": string,\n            \"removable\": boolean,\n            \"audienceGuess\": number\n        },\n        {\n            \"text\": string,\n            \"removable\": boolean,\n            \"audienceGuess\": number\n        },\n        {\n            \"text\": string,\n            \"removable\": boolean,\n            \"audienceGuess\": number\n        }\n    ],\n    \"randomClue\": string,\n    \"answer\": { // required\n        \"correctValue\": string,\n        \"shortDescription\": string // required\n    }\n}\n```\n\nWhere each field correspond to:\n- \"type\" -> what step in the quiz game the user is in. Can be only `IN_PROGRESS` or `FINISHED`.\n- \"round\" -> the actual round number\n- \"question\" -> the object that holds all data related to the current round question\n  - \"text\" -> the actual question\n  - \"difficulty\" -> the difficulty of the qestion. It has to be one of the options: EASY, MEDIUM or HARD. You always prioritize HARD and MEDIUM questions over EASY ones.\n  - \"points\" -> how many points the question adds to the user score\n  - \"lifelineDeduction\" -> how many points will be subtracted from the points the question gives out if the user uses a lifeline.\n- \"options\" -> a list of possible answers\n  - \"text\" -> the text of a possible answer\n  - \"removable\" -> if the answer can be greyed out if the user uses a 50/50 lifeline. This value should always be false for *two answers•, the correct one and one random incorrect one.\n  - \"audienceGuess\" -> defines how much a theoretical audience is confident that this answer is correct, this value will be used in case the user uses the 'Ask the Audience' lifeline. The sum of all percentages *have* to equal to 100. If the question is hard, this value should be almost equally shared between answers. There is a chance that the most of the audience might vote for the wrong option for MEDIUM and HARD questions.\n- \"randomClue\" -> as the name suggests, it's a random clue that'll be shown to the user if it tries to use a random clue lifeline.\n- \"answer\" -> an object related to the actual correct answer.\n  - \"correctValue\" -> the correct answer. Has to have the exact same text as in one of the valid options.\n  - \"shortDescription\" -> a not really long description of why the `correctValue` is the correct one.\n\n## What you'll get as answer\n\nThe expected response you should receive will always follow the format below.\n\n```json\n{\n    \"correctAnswers\": 0,\n}\n```\n\nWhere:\n- \"correctAnswers\" corresponds to how many of the 10 questions the user got correct. If by the end of the game this value reaches 10, then the user aced the quiz.\n\n\n## End game responses\n\n```json\n{\n    \"step\": \"FINISHED\", // required\n    \"gameAnalysis\": string // required\n}\n```\n\n- \"step\" -> on the 11 round, the `step` property *should* be `FINISHED`.\n- \"gameAnalysis\" -> an in-depth game analysis of how the user went based on the `correctAnswers` value provided previously, and what they should study/learn about a bit more. Feel free to suggest books, videos, websites, whatever comes to mind! Make this final moment fun and be grateful for the user's participation.") }
    )
    private val chat = model.startChat()

    private val adapter = moshi.adapter(GeminiResponse::class.java)

    fun start() {
        viewModelScope.launch {
            try {
                val response = chat.sendMessage("(game begins)")

                val responseModel = adapter.fromJson(response.text.orEmpty())
                if (responseModel != null)
                    _screenState.value = QuizGameScreenState.Success(responseModel)
                else
                    _screenState.value = QuizGameScreenState.Error
            } catch (e: Exception) {
                println(e)
                _screenState.value = QuizGameScreenState.Error
            }
        }
    }

    fun optionChosen(optionClicked: OptionsResponse) {
        if (optionClicked.text == (screenState.value as QuizGameScreenState.Success).roundData.answer.correctValue)
            correctAnswers.inc()

        viewModelScope.launch {
            val userResponse = """
                ```json
                {
                    "correctAnswers": $correctAnswers,
                }
                ```
                """.trimIndent()
            _screenState.value = QuizGameScreenState.Loading

            try {
                val response = chat.sendMessage(userResponse)

                val responseModel = adapter.fromJson(response.text.orEmpty())
                if (responseModel != null)
                    _screenState.value = QuizGameScreenState.Success(responseModel)
                else
                    _screenState.value = QuizGameScreenState.Error
            } catch (e: Exception) {
                println(e)
                _screenState.value = QuizGameScreenState.Error
            }
        }
    }
}