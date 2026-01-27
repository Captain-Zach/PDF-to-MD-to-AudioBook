package com.deeperdive.pdf2audio.conversion

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.io.File

class ConversionWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val uriString = inputData.getString(KEY_URI) ?: return Result.failure()
        val mimeType = inputData.getString(KEY_MIME)
        val displayName = inputData.getString(KEY_DISPLAY_NAME)
        val uri = Uri.parse(uriString)

        return try {
            val result = DocumentConverter.convert(
                applicationContext,
                uri,
                mimeType,
                displayName
            )
            val outputDir = File(applicationContext.filesDir, "converted")
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            val outputFile = File(outputDir, "${id}.md")
            outputFile.writeText(result.markdown)
            Result.success(
                workDataOf(
                    KEY_OUTPUT_PATH to outputFile.absolutePath,
                    KEY_TITLE to result.title
                )
            )
        } catch (error: Exception) {
            Result.failure(
                workDataOf(KEY_ERROR to (error.message ?: "Conversion failed"))
            )
        }
    }

    companion object {
        const val KEY_URI = "input_uri"
        const val KEY_MIME = "input_mime"
        const val KEY_DISPLAY_NAME = "input_display_name"
        const val KEY_OUTPUT_PATH = "output_path"
        const val KEY_TITLE = "output_title"
        const val KEY_ERROR = "output_error"
    }
}
