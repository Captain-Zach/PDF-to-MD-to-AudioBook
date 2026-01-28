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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.deeperdive.pdf2audio.conversion.ConversionWorker
import java.io.File
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private var ttsReady = false
    private var isPlaying = false
    private var isConverting = false
    private var selectedUri: Uri? = null
    private var selectedMimeType: String? = null
    private var conversionWorkId: UUID? = null
    private var markdownPath: String? = null
    private var chunks: List<String> = emptyList()
    private var currentChunkIndex = 0

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
        cancelActiveConversion()
        selectedUri = uri
        selectedMimeType = contentResolver.getType(uri)
        markdownPath = null
        chunks = emptyList()
        currentChunkIndex = 0
        isPlaying = false
        primaryButton.text = getString(R.string.play)
        primaryButton.isEnabled = true
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
                    statusView.text = "Status: Speaking ${currentChunkIndex + 1}/${chunks.size}"
                }
            }

            override fun onDone(utteranceId: String) {
                runOnUiThread {
                    if (!isPlaying) {
                        return@runOnUiThread
                    }
                    if (currentChunkIndex < chunks.size - 1) {
                        currentChunkIndex += 1
                        speakCurrentChunk()
                    } else {
                        isPlaying = false
                        primaryButton.text = getString(R.string.play)
                        statusView.text = "Status: Finished"
                    }
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
        if (isConverting) {
            statusView.text = "Status: Conversion in progress"
            return
        }
        if (markdownPath == null) {
            startConversion(uri)
            return
        }
        if (chunks.isEmpty()) {
            statusView.text = "Status: No text to read"
            return
        }
        if (isPlaying) {
            pausePlayback()
            return
        }
        startPlayback()
    }

    private fun startConversion(uri: Uri) {
        val displayName = queryDisplayName(uri)
        val data = workDataOf(
            ConversionWorker.KEY_URI to uri.toString(),
            ConversionWorker.KEY_MIME to (selectedMimeType ?: ""),
            ConversionWorker.KEY_DISPLAY_NAME to (displayName ?: "")
        )
        val request = OneTimeWorkRequestBuilder<ConversionWorker>()
            .setInputData(data)
            .build()
        conversionWorkId = request.id
        isConverting = true
        primaryButton.isEnabled = false
        primaryButton.text = getString(R.string.converting)
        statusView.text = "Status: Converting"
        WorkManager.getInstance(this).enqueue(request)
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(request.id)
            .observe(this) { info ->
                handleWorkInfo(info)
            }
    }

    private fun handleWorkInfo(info: WorkInfo?) {
        if (info == null) return
        when (info.state) {
            WorkInfo.State.SUCCEEDED -> {
                isConverting = false
                primaryButton.isEnabled = true
                primaryButton.text = getString(R.string.play)
                markdownPath = info.outputData.getString(ConversionWorker.KEY_OUTPUT_PATH)
                loadMarkdownAndPrepare()
            }
            WorkInfo.State.FAILED -> {
                isConverting = false
                primaryButton.isEnabled = true
                primaryButton.text = getString(R.string.play)
                statusView.text = "Status: Conversion failed"
            }
            WorkInfo.State.CANCELLED -> {
                isConverting = false
                primaryButton.isEnabled = true
                primaryButton.text = getString(R.string.play)
                statusView.text = "Status: Conversion cancelled"
            }
            WorkInfo.State.RUNNING -> {
                statusView.text = "Status: Converting"
            }
            WorkInfo.State.ENQUEUED -> {
                statusView.text = "Status: Queued"
            }
            WorkInfo.State.BLOCKED -> {
                statusView.text = "Status: Blocked"
            }
        }
    }

    private fun loadMarkdownAndPrepare() {
        val path = markdownPath ?: return
        try {
            val markdown = File(path).readText()
            chunks = buildChunks(markdown)
            currentChunkIndex = 0
            statusView.text = if (chunks.isNotEmpty()) {
                "Status: Ready (${chunks.size} chunks)"
            } else {
                "Status: No text to read"
            }
        } catch (error: Exception) {
            statusView.text = "Status: Failed to load markdown"
        }
    }

    private fun startPlayback() {
        isPlaying = true
        primaryButton.text = getString(R.string.pause)
        speakCurrentChunk()
    }

    private fun pausePlayback() {
        tts.stop()
        isPlaying = false
        primaryButton.text = getString(R.string.play)
        statusView.text = "Status: Paused"
    }

    private fun speakCurrentChunk() {
        val chunk = chunks.getOrNull(currentChunkIndex) ?: return
        statusView.text = "Status: Speaking ${currentChunkIndex + 1}/${chunks.size}"
        tts.speak(chunk, TextToSpeech.QUEUE_FLUSH, null, "chunk_$currentChunkIndex")
    }

    private fun buildChunks(markdown: String): List<String> {
        val paragraphs = markdown.split(Regex("\\n\\s*\\n"))
        val chunks = mutableListOf<String>()
        val maxLength = 1200
        val buffer = StringBuilder()

        fun flushBuffer() {
            val text = buffer.toString().trim()
            if (text.isNotEmpty()) {
                chunks.add(text)
            }
            buffer.clear()
        }

        for (paragraph in paragraphs) {
            val cleaned = paragraph.trim()
            if (cleaned.isEmpty()) continue
            if (buffer.isNotEmpty() && buffer.length + cleaned.length + 2 > maxLength) {
                flushBuffer()
            }
            if (buffer.isNotEmpty()) {
                buffer.append("\n\n")
            }
            buffer.append(cleaned)
        }

        flushBuffer()
        return chunks
    }

    private fun cancelActiveConversion() {
        conversionWorkId?.let { WorkManager.getInstance(this).cancelWorkById(it) }
        conversionWorkId = null
        isConverting = false
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
