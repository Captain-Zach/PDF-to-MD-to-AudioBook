package com.deeperdive.pdf2audio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private var ttsReady = false
    private var isPlaying = false
    private var selectedUri: Uri? = null

    private lateinit var statusView: TextView
    private lateinit var fileView: TextView
    private lateinit var primaryButton: Button
    private lateinit var pickButton: Button

    private val openDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) {
            statusView.text = "Status: No document selected"
            return@registerForActivityResult
        }
        try {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (error: SecurityException) {
            // Persisted permission is best-effort for user-selected files.
        }
        selectedUri = uri
        fileView.text = queryDisplayName(uri) ?: uri.toString()
        statusView.text = "Status: Ready"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusView = findViewById(R.id.statusView)
        fileView = findViewById(R.id.fileView)
        primaryButton = findViewById(R.id.primaryButton)
        pickButton = findViewById(R.id.pickButton)

        tts = TextToSpeech(this, this)
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {
                runOnUiThread {
                    statusView.text = "Status: Speaking"
                }
            }

            override fun onDone(utteranceId: String) {
                runOnUiThread {
                    isPlaying = false
                    primaryButton.text = getString(R.string.play)
                    statusView.text = "Status: Finished"
                }
            }

            override fun onError(utteranceId: String) {
                runOnUiThread {
                    isPlaying = false
                    primaryButton.text = getString(R.string.play)
                    statusView.text = "Status: Playback error"
                }
            }
        })

        primaryButton.setOnClickListener { togglePlayback() }
        pickButton.setOnClickListener {
            openDocumentLauncher.launch(
                arrayOf("application/pdf", "application/epub+zip")
            )
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
            statusView.text = if (ttsReady) {
                "Status: TTS ready"
            } else {
                "Status: TTS not available"
            }
        } else {
            ttsReady = false
            statusView.text = "Status: TTS init failed"
        }
    }

    private fun togglePlayback() {
        val uri = selectedUri
        if (uri == null) {
            statusView.text = "Status: Select a PDF or EPUB first"
            return
        }
        if (!ttsReady) {
            statusView.text = "Status: TTS not ready"
            return
        }
        if (isPlaying) {
            tts.stop()
            isPlaying = false
            primaryButton.text = getString(R.string.play)
            statusView.text = "Status: Paused"
            return
        }

        val displayName = queryDisplayName(uri) ?: "document"
        val text = "Playback placeholder. Document selected: $displayName."
        isPlaying = true
        primaryButton.text = getString(R.string.pause)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "playback")
    }

    private fun queryDisplayName(uri: Uri): String? {
        contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1 && cursor.moveToFirst()) {
                return cursor.getString(index)
            }
        }
        return null
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
