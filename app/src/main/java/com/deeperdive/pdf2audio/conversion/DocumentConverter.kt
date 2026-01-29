package com.deeperdive.pdf2audio.conversion

import android.content.Context
import android.net.Uri

object DocumentConverter {
    fun convert(
        context: Context,
        uri: Uri,
        mimeType: String?,
        displayName: String?
    ): ConversionResult {
        val resolvedMime = mimeType
            ?: context.contentResolver.getType(uri)
            ?: ""
        val converter = when {
            resolvedMime == "application/pdf" || displayName?.endsWith(".pdf", true) == true ->
                PdfToMarkdownConverter()
            resolvedMime == "application/epub+zip" || displayName?.endsWith(".epub", true) == true ->
                EpubToMarkdownConverter()
            else -> throw IllegalArgumentException("Unsupported document type: $resolvedMime")
        }
        context.contentResolver.openInputStream(uri).use { inputStream ->
            requireNotNull(inputStream) { "Unable to open input stream." }
            return converter.convert(inputStream, displayName)
        }
    }
}
