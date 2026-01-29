package com.deeperdive.pdf2audio.conversion

import nl.siegmann.epublib.epub.EpubReader
import java.io.InputStream
import java.nio.charset.Charset

class EpubToMarkdownConverter : Converter {
    override fun convert(inputStream: InputStream, displayName: String?): ConversionResult {
        val book = EpubReader().readEpub(inputStream)
        val title = book.title?.takeIf { it.isNotBlank() } ?: displayName
        val builder = StringBuilder()

        val spine = book.spine?.spineReferences ?: emptyList()
        for (spineRef in spine) {
            val resource = spineRef.resource ?: continue
            val encoding = resource.inputEncoding?.takeIf { it.isNotBlank() } ?: "UTF-8"
            val bytes = resource.inputStream.use { it.readBytes() }
            val html = bytes.toString(Charset.forName(encoding))
            val markdown = HtmlToMarkdownConverter.convert(html)
            if (markdown.isNotBlank()) {
                builder.append(markdown).append("\n\n")
            }
        }

        val cleaned = MarkdownCleaner.cleanMarkdown(builder.toString())
        return ConversionResult(title, cleaned)
    }
}
