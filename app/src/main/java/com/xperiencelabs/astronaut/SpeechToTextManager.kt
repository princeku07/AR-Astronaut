package com.xperiencelabs.astronaut

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

object SpeechToTextManager {
    // Late-initialized variable to hold the SpeechRecognizer instance
    private lateinit var speechRecognizer: SpeechRecognizer

    // Function to initialize the SpeechRecognizer with a given context
    fun initialise(context: Context) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    }

    // Function to start listening for speech input
    fun startListening(
        onSpeechStarted: () -> Unit,
        onSpeechStopped: () -> Unit,
        onSpeechError: (Int) -> Unit,
        onSpeechResult: (List<String>) -> Unit
    ) {
        // RecognitionListener implementation to handle speech recognition events
        val recognitionListener: RecognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                // Callback indicating that speech recognition is ready for input
            }

            override fun onBeginningOfSpeech() {
                // Callback indicating the start of speech input
                onSpeechStarted()
            }

            override fun onRmsChanged(p0: Float) {
                // Callback indicating that the Root Mean Square (RMS) value of the audio has changed
            }

            override fun onBufferReceived(p0: ByteArray?) {
                // Callback indicating that an audio buffer has been received
            }

            override fun onEndOfSpeech() {
                // Callback indicating the end of speech input
                onSpeechStopped()
            }

            override fun onError(error: Int) {
                // Callback indicating a speech recognition error has occurred
                onSpeechError(error)
            }

            override fun onResults(results: Bundle?) {
                // Callback indicating that speech recognition results have been received
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.let {
                    // Pass the recognition results to the provided callback
                    onSpeechResult(it)
                }
            }

            override fun onPartialResults(p0: Bundle?) {
                // Callback indicating partial speech recognition results have been received
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
                // Callback indicating that a speech recognition event has occurred
            }
        }

        // Create an intent for speech recognition
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        // Start listening for speech using SpeechRecognizer
        speechRecognizer.startListening(intent)

        // Set the RecognitionListener for the SpeechRecognizer
        speechRecognizer.setRecognitionListener(recognitionListener)
    }

    // Function to stop speech recognition
    fun stopListening() {
        speechRecognizer.stopListening()
    }

    // Function to destroy the SpeechRecognizer and release associated resources
    fun destroy() {
        speechRecognizer.destroy()
    }
}

@Composable
fun SpeechToTextWrapper(
    listenEnable: Boolean,
    onSpeechStarted: () -> Unit,
    onSpeechStopped: () -> Unit,
    onSpeechError: (String) -> Unit,
    onSpeechResult: (List<String>) -> Unit
) {
    // LaunchedEffect block to trigger when the listenEnable value changes
    LaunchedEffect(key1 = listenEnable) {
        // Check if speech recognition should be started or stopped based on listenEnable
        if (listenEnable) {
            // Start speech recognition using SpeechToTextManager
            SpeechToTextManager.startListening(
                onSpeechStarted = onSpeechStarted,
                onSpeechStopped = onSpeechStopped,
                onSpeechError = { error ->
                    // Handle speech recognition errors and pass an error message to the callback
                    val errorMessage: String = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error."
                        SpeechRecognizer.ERROR_CLIENT -> "Other client-side errors."
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions."
                        SpeechRecognizer.ERROR_NETWORK -> "Other network-related errors."
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network operation timed out."
                        SpeechRecognizer.ERROR_NO_MATCH -> "No recognition result matched."
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy."
                        SpeechRecognizer.ERROR_SERVER -> "Server sends error status."
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input."
                        else -> "Unknown error occurred."
                    }
                    onSpeechError(errorMessage)
                },
                onSpeechResult = {
                    // Pass the speech recognition results to the provided callback
                    onSpeechResult(it)
                }
            )
        } else {
            // Stop speech recognition if listenEnable is false
            SpeechToTextManager.stopListening()
        }
    }
}
