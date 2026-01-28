package com.deeperdive.pdf2audio.conversion

import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream

class PdfToMarkdownConverter : Converter {
    override fun convert(inputStream: InputStream, displayName: String?): ConversionResult {
        PDDocument.load(inputStream).use { document ->
            val stripper = PDFTextStripper()
            stripper.sortByPosition = true
            stripper.lineSeparator = "\n"
            val rawText = stripper.getText(document)
            val cleaned = MarkdownCleaner.cleanPdfText(rawText)
            val title = document.documentInformation?.title?.takeIf { it.isNotBlank() }
                ?: displayName
            return ConversionResult(title, cleaned)
        }
    }
}
