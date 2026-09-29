package com.example.data.repository

import com.example.BuildConfig
import com.example.data.model.QuizQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiQuizService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generate5Pyqs(topic: String, exam: String): List<QuizQuestion> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // If no real API key or placeholder, return curated real PYQs
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext PyqQuestionBank.getQuestionsForTopic(topic, exam)
        }

        try {
            val prompt = """
                Generate exactly 5 authentic, high-quality Previous Year Questions (PYQ) or PYQ-standard multiple-choice questions for competitive exam '$exam' on topic '$topic'.
                Format strictly as a JSON array of 5 objects, each with:
                - "questionText": string with clear conceptual/numerical question
                - "options": array of 4 distinct strings (e.g. ["A) ...", "B) ...", "C) ...", "D) ..."])
                - "correctIndex": integer (0, 1, 2, or 3)
                - "examYearTag": string (e.g. "$exam 2023", "$exam 2021")
                - "explanation": concise step-by-step mathematical or conceptual explanation
                Return ONLY valid JSON array without any markdown wrappers or preamble.
            """.trimIndent()

            val requestBodyJson = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val partObj = JSONObject().apply { put("text", prompt) }
                    val partsArray = JSONArray().apply { put(partObj) }
                    put(JSONObject().apply { put("parts", partsArray) })
                }
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.3)
                    put("responseMimeType", "application/json")
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext PyqQuestionBank.getQuestionsForTopic(topic, exam)
            }

            val responseString = response.body?.string() ?: ""
            val jsonRoot = JSONObject(responseString)
            val candidates = jsonRoot.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val rawText = parts?.optJSONObject(0)?.optString("text") ?: ""

            if (rawText.isBlank()) {
                return@withContext PyqQuestionBank.getQuestionsForTopic(topic, exam)
            }

            val cleanJson = rawText.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val parsedArray = JSONArray(cleanJson)
            val questions = mutableListOf<QuizQuestion>()

            for (i in 0 until minOf(5, parsedArray.length())) {
                val obj = parsedArray.getJSONObject(i)
                val optArray = obj.getJSONArray("options")
                val optionsList = mutableListOf<String>()
                for (j in 0 until optArray.length()) {
                    optionsList.add(optArray.getString(j))
                }
                questions.add(
                    QuizQuestion(
                        id = i + 1,
                        questionText = obj.getString("questionText"),
                        options = optionsList,
                        correctIndex = obj.getInt("correctIndex").coerceIn(0, 3),
                        examYearTag = obj.optString("examYearTag", "$exam PYQ"),
                        explanation = obj.optString("explanation", "Standard solution based on fundamental concepts."),
                        topic = topic
                    )
                )
            }

            if (questions.size == 5) questions else PyqQuestionBank.getQuestionsForTopic(topic, exam)
        } catch (e: Exception) {
            PyqQuestionBank.getQuestionsForTopic(topic, exam)
        }
    }
}
