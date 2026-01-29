package com.deeperdive.pdf2audio

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MarkdownPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markdown_preview)

        val titleView = findViewById<TextView>(R.id.previewTitle)
        val markdownView = findViewById<TextView>(R.id.markdownView)

        val path = intent.getStringExtra(EXTRA_PATH)
        val title = intent.getStringExtra(EXTRA_TITLE)
        if (!title.isNullOrBlank()) {
            titleView.text = title
        }

        if (path.isNullOrBlank()) {
            markdownView.text = getString(R.string.preview_empty)
            return
        }

        markdownView.text = try {
            File(path).readText()
        } catch (error: Exception) {
            getString(R.string.preview_empty)
        }
    }

    companion object {
        const val EXTRA_PATH = "extra_markdown_path"
        const val EXTRA_TITLE = "extra_markdown_title"
    }
}
