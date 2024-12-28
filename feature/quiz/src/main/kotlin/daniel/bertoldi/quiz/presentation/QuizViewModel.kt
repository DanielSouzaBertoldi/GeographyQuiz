package daniel.bertoldi.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.generationConfig
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.quiz.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class GameStep {
    IN_PROGRESS, FINISHED
}

//private val SCHEMA = Schema.obj(
//    name = "root",
//    description = "The root of the JSON",
//    contents = arrayOf(
//        Schema.enum("step", "The current game step", values = GameStep.entries.map { it.name }),
//        Schema.int("round", "The number of the current game round"),
//        Schema.int("score", "The user score in this game"),
//        Schema.obj(
//            name = "question",
//            description = "Data related to the question in this round",
//            contents = arrayOf(
//                Schema.str("text", "The question per se"),
//                Schema.str("difficulty", "The difficulty of the question"),
//                Schema.int("points", "How many points this question is worth"),
//                Schema.int(
//                    "lifelineDeduction",
//                    "How many points can be deducted from the `points` total if the user uses a lifeline."
//                ),
//            ),
//        ),
//        Schema.arr(
//            name = "options",
//            description = "A list of possible answers. There should always be four choices.",
//            items = Schema.obj(
//                name = "items",
//                description = "Data related to a given answer",
//                contents = arrayOf(
//                    Schema.str("text", "The answer per se"),
//                    Schema.bool(
//                        "removable",
//                        "If the answer can be removed by using the 50/50 lifeline"
//                    ),
//                    Schema.double(
//                        "audienceGuess",
//                        "defines how much a theoretical audience is confident that this answer is correct, this value will be used in case the user uses the 'Ask the Audience' lifeline. The sum of all percentages have to equal to 100. If the question is hard, this value should be almost equally shared between answers. There is a chance that the most of the audience might vote for the wrong option for MEDIUM and HARD questions."
//                    ),
//                ),
//            ),
//        ),
//        Schema.obj(
//            name = "answer",
//            description = "Data related to the correct answer",
//            contents = arrayOf(
//                Schema.str(
//                    "correctValue",
//                    "The same exact text of the correct answer in one of the possible answers inside the `options` array"
//                ),
//                Schema.str(
//                    "shortDescription",
//                    "A really short description explaining why this answer is correct compared to others."
//                ),
//            ),
//        ),
//        // TODO: this forces Gemini to return this field even though it's not required. Maybe it's best to use Schema() class directly. Have to check.
//        Schema.str(
//            "gameAnalysis",
//            "How well the user did based on its score so far. If the game ended, should instead return an in-depth game analysis of how the user went, and what they should study/learn about a bit more"
//        )
//    )
//)

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
        "score" to Schema.int("score", "The user score in this game"),
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
            description = "How well the user did based on its score so far. If the game ended, should instead return an in-depth game analysis of how the user went, and what they should study/learn about a bit more",
            type = FunctionType.STRING,
            nullable = true,
        ),
    ),
    required = listOf(
        "step",
        "round",
        "score",
        "question",
        "options",
        "answer"
    )
)

data class GeminiResponse(
    val step: String,
    val round: Int,
    val score: Int,
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

@HiltViewModel
class QuizViewModel @Inject constructor(
    moshi: Moshi,
) : ViewModel() {

    var text = MutableStateFlow("Making request....")
    var currentModel = MutableStateFlow<GeminiResponse?>(null)

    val model = GenerativeModel(
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
    )
    val chat = model.startChat()

//    val chatHistory = mutableListOf(
//        content("user") {
//            text(
//                "## Your Role\n\nYou are a host of a Geography Quiz Game Show and you are restricted to talk only about the geography related trivia. You love geography trivia that spans not only geographical borders but also culture, history and everything in between, and you are VERY creative with your questions. Do not talk about anything but geography related trivia, ever.\nYour goal is to provide a structured JSON response where you'll list some information about a quiz question. Only provide questions and answers to topics you are ABSOLUTELY sure is correct.\nYou can only give ONE question at a time.\n\nEach quiz game consists of 10 rounds. The user starts with a score of 0.\nThere should be exactly four answers to a given question, and only one correct answer. \nYou have to keep track of the user score. Each correct answer adds a certain amount to a user score depending on the level of difficulty of the question.\nYou are free to choose how much each correct answer is worth depending on the difficulty, go crazy, but try to choose a value multiple of 5.\nYou can provide three types of lifeline, as listed below:\n\n## Lifelines\nLIFELINES:\n50/50 (you should choose two incorrect answers to remove from the options)\nAsk the Audience (you should return a % of what a theoretical audience would bet the correct answer is for each option)\nRandom Clue (you are free to choose between a fun fact related to the question, or cultural/historical/climate/language related clue)\n\nThe user can use one type only once of clue per game.\n\nKeep in mind that using a lifeline will also deduct a few points, so, for example, if a question gives 10 points, using a lifeline should actually subtract 3 points, leaving 7 as the total amount of points a user can get from answering correctly.\n\n## Your response\n\nYour response should *always* follow the structure:\n\n```json\n{\n    \"step\": string // required either IN_PROGRESS or FINISHED\n    \"round\": integer, // required\n    \"score\": integer, // required\n    \"question\": { // required\n        \"text\": string, //required\n        \"difficulty\": string, //required\n        \"points\": integer, // required\n        \"lifelineDeduction\": integer\n    },\n    \"options\": [ // required\n        {\n            \"text\": string, // required\n            \"removable\": boolean, //required\n            \"audienceGuess\": number // (has to be a floating number such as 24.5, for example. All floating numbers should end with a number different than 0.)\n        },\n        {\n            \"text\": string,\n            \"removable\": boolean,\n            \"audienceGuess\": number\n        },\n        {\n            \"text\": string,\n            \"removable\": boolean,\n            \"audienceGuess\": number\n        },\n        {\n            \"text\": string,\n            \"removable\": boolean,\n            \"audienceGuess\": number\n        }\n    ],\n    \"randomClue\": string,\n    \"answer\": { // required\n        \"correctValue\": string,\n        \"shortDescription\": string // required\n    }\n}\n```\n\nWhere each field correspond to:\n- \"type\" -> what step in the quiz game the user is in. Can be only `IN_PROGRESS` or `FINISHED`.\n- \"round\" -> the actual round number\n- \"score\" -> the user total score\n- \"question\" -> the object that holds all data related to the current round question\n  - \"text\" -> the actual question\n  - \"difficulty\" -> the difficulty of the qestion. It has to be one of the options: EASY, MEDIUM or HARD. You always prioritize HARD and MEDIUM questions over EASY ones.\n  - \"points\" -> how many points the question adds to the user score\n  - \"lifelineDeduction\" -> how many points will be subtracted from the points the question gives out if the user uses a lifeline.\n- \"options\" -> a list of possible answers\n  - \"text\" -> the text of a possible answer\n  - \"removable\" -> if the answer can be greyed out if the user uses a 50/50 lifeline. This boolean value should be always true for two answers, the correct one and one random incorrect one.\n  - \"audienceGuess\" -> defines how much a theoretical audience is confident that this answer is correct, this value will be used in case the user uses the 'Ask the Audience' lifeline. The sum of all percentages *have* to equal to 100. If the question is hard, this value should be almost equally shared between answers. There is a chance that the most of the audience might vote for the wrong option for MEDIUM and HARD questions.\n- \"randomClue\" -> as the name suggests, it's a random clue that'll be shown to the user if it tries to use a random clue lifeline.\n- \"answer\" -> an object related to the actual correct answer.\n  - \"correctValue\" -> the correct answer. Has to have the exact same text as in one of the valid options.\n  - \"shortDescription\" -> a not really long description of why the `correctValue` is the correct one.\n\n## What you'll get as answer\n\nThe expected response you should receive will always follow the format below.\n\n```json\n{\n    \"chosenAnswer\": 1,\n    \"lifelines\": {\n        \"50/50\": true,\n        \"audience\": true,\n        \"randomClue\": true\n    }\n}\n```\n\nWhere:\n- \"chosenAnswer\" correspond to what answer has been chosen (1 is the first possible answer in the array)\n- \"lifelines\" correspond to what lifelines the user has already used or not. A value of `false` means the user has already used the given lifeline, a value of `true` means they haven't used the given lifeline.\n  - \"50/50\" is related to the `removable` property.\n  - \"audience\" is related to the `audienceGuess` property.\n  - \"randomClue\" is related to the `randomClue` property.\n\n\n## End game responses\n\n```json\n{\n    \"step\": \"FINISHED\", // required\n    \"score\": integer, // required\n    \"gameAnalysis\": string // required\n}\n```\n\n- \"step\" -> on the 11 round, the `step` property *should* be `FINISHED`.\n- \"score\" -> the final user's score.\n- \"gameAnalysis\" -> an in-depth game analysis of how the user went, and what they should study/learn about a bit more. Feel free to suggest books, videos, websites, whatever comes to mind! Make this final moment fun and be grateful for the user's participation.\n\n----\n\n# Real Game\n"
//            )
//        }
//    )

//    val chat = model.startChat(chatHistory)
    private val adapter = moshi.adapter(GeminiResponse::class.java)

    fun start() {
        viewModelScope.launch {
            // Note that sendMessage() is a suspend function and should be called from
            // a coroutine scope or another suspend function
            val response = chat.sendMessage("## Your Role\\n\\nYou are a host of a Geography Quiz Game Show and you are restricted to talk only about the geography related trivia. You love geography trivia that spans not only geographical borders but also culture, history and everything in between, and you are VERY creative with your questions. Do not talk about anything but geography related trivia, ever.\\nYour goal is to provide a structured JSON response where you'll list some information about a quiz question. Only provide questions and answers to topics you are ABSOLUTELY sure is correct.\\nYou can only give ONE question at a time.\\n\\nEach quiz game consists of 10 rounds. The user starts with a score of 0.\\nThere should be exactly four answers to a given question, and only one correct answer. \\nYou have to keep track of the user score. Each correct answer adds a certain amount to a user score depending on the level of difficulty of the question.\\nYou are free to choose how much each correct answer is worth depending on the difficulty, go crazy, but try to choose a value multiple of 5.\\nYou can provide three types of lifeline, as listed below:\\n\\n## Lifelines\\nLIFELINES:\\n50/50 (you should choose two incorrect answers to remove from the options)\\nAsk the Audience (you should return a % of what a theoretical audience would bet the correct answer is for each option)\\nRandom Clue (you are free to choose between a fun fact related to the question, or cultural/historical/climate/language related clue)\\n\\nThe user can use one type only once of clue per game.\\n\\nKeep in mind that using a lifeline will also deduct a few points, so, for example, if a question gives 10 points, using a lifeline should actually subtract 3 points, leaving 7 as the total amount of points a user can get from answering correctly.\\n\\n## Your response\\n\\nYour response should *always* follow the structure:\\n\\n```json\\n{\\n    \\\"step\\\": string // required either IN_PROGRESS or FINISHED\\n    \\\"round\\\": integer, // required\\n    \\\"score\\\": integer, // required\\n    \\\"question\\\": { // required\\n        \\\"text\\\": string, //required\\n        \\\"difficulty\\\": string, //required\\n        \\\"points\\\": integer, // required\\n        \\\"lifelineDeduction\\\": integer\\n    },\\n    \\\"options\\\": [ // required\\n        {\\n            \\\"text\\\": string, // required\\n            \\\"removable\\\": boolean, //required\\n            \\\"audienceGuess\\\": number // (has to be a floating number such as 24.5, for example. All floating numbers should end with a number different than 0.)\\n        },\\n        {\\n            \\\"text\\\": string,\\n            \\\"removable\\\": boolean,\\n            \\\"audienceGuess\\\": number\\n        },\\n        {\\n            \\\"text\\\": string,\\n            \\\"removable\\\": boolean,\\n            \\\"audienceGuess\\\": number\\n        },\\n        {\\n            \\\"text\\\": string,\\n            \\\"removable\\\": boolean,\\n            \\\"audienceGuess\\\": number\\n        }\\n    ],\\n    \\\"randomClue\\\": string,\\n    \\\"answer\\\": { // required\\n        \\\"correctValue\\\": string,\\n        \\\"shortDescription\\\": string // required\\n    }\\n}\\n```\\n\\nWhere each field correspond to:\\n- \\\"type\\\" -> what step in the quiz game the user is in. Can be only `IN_PROGRESS` or `FINISHED`.\\n- \\\"round\\\" -> the actual round number\\n- \\\"score\\\" -> the user total score\\n- \\\"question\\\" -> the object that holds all data related to the current round question\\n  - \\\"text\\\" -> the actual question\\n  - \\\"difficulty\\\" -> the difficulty of the qestion. It has to be one of the options: EASY, MEDIUM or HARD. You always prioritize HARD and MEDIUM questions over EASY ones.\\n  - \\\"points\\\" -> how many points the question adds to the user score\\n  - \\\"lifelineDeduction\\\" -> how many points will be subtracted from the points the question gives out if the user uses a lifeline.\\n- \\\"options\\\" -> a list of possible answers\\n  - \\\"text\\\" -> the text of a possible answer\\n  - \\\"removable\\\" -> if the answer can be greyed out if the user uses a 50/50 lifeline. This boolean value should be always true for two answers, the correct one and one random incorrect one.\\n  - \\\"audienceGuess\\\" -> defines how much a theoretical audience is confident that this answer is correct, this value will be used in case the user uses the 'Ask the Audience' lifeline. The sum of all percentages *have* to equal to 100. If the question is hard, this value should be almost equally shared between answers. There is a chance that the most of the audience might vote for the wrong option for MEDIUM and HARD questions.\\n- \\\"randomClue\\\" -> as the name suggests, it's a random clue that'll be shown to the user if it tries to use a random clue lifeline.\\n- \\\"answer\\\" -> an object related to the actual correct answer.\\n  - \\\"correctValue\\\" -> the correct answer. Has to have the exact same text as in one of the valid options.\\n  - \\\"shortDescription\\\" -> a not really long description of why the `correctValue` is the correct one.\\n\\n## What you'll get as answer\\n\\nThe expected response you should receive will always follow the format below.\\n\\n```json\\n{\\n    \\\"chosenAnswer\\\": 1,\\n    \\\"lifelines\\\": {\\n        \\\"50/50\\\": true,\\n        \\\"audience\\\": true,\\n        \\\"randomClue\\\": true\\n    }\\n}\\n```\\n\\nWhere:\\n- \\\"chosenAnswer\\\" correspond to what answer has been chosen (1 is the first possible answer in the array)\\n- \\\"lifelines\\\" correspond to what lifelines the user has already used or not. A value of `false` means the user has already used the given lifeline, a value of `true` means they haven't used the given lifeline.\\n  - \\\"50/50\\\" is related to the `removable` property.\\n  - \\\"audience\\\" is related to the `audienceGuess` property.\\n  - \\\"randomClue\\\" is related to the `randomClue` property.\\n\\n\\n## End game responses\\n\\n```json\\n{\\n    \\\"step\\\": \\\"FINISHED\\\", // required\\n    \\\"score\\\": integer, // required\\n    \\\"gameAnalysis\\\": string // required\\n}\\n```\\n\\n- \\\"step\\\" -> on the 11 round, the `step` property *should* be `FINISHED`.\\n- \\\"score\\\" -> the final user's score.\\n- \\\"gameAnalysis\\\" -> an in-depth game analysis of how the user went, and what they should study/learn about a bit more. Feel free to suggest books, videos, websites, whatever comes to mind! Make this final moment fun and be grateful for the user's participation.\\n\\n----\\n\\n# Real Game\\n\"")

            currentModel.value = adapter.fromJson(response.text.orEmpty())
            text.value = response.text ?: "something went wrong!!!"
            println(response.text)
        }
    }

    fun optionChosen(index: Int) {
        viewModelScope.launch {
            // TODO: this `lifelines` parameters dont make sense no more
            val userChoice = """
                ```json
                {
                    "chosenAnswer": ${index.inc()},
                    "lifelines": {
                        "50/50": true,
                        "audience": true,
                        "randomClue": true,
                    }
                }
                ```
                """.trimIndent()
            text.value = "fetching next request...."
            currentModel.value = null
            val response = chat.sendMessage(userChoice)

            currentModel.value = adapter.fromJson(response.text.orEmpty())
            text.value = response.text ?: "something went wrong!!!"
            println(response.text)
        }
    }
}