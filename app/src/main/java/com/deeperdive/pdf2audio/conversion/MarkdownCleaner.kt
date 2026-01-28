package com.deeperdive.pdf2audio.conversion

object MarkdownCleaner {
    fun cleanPdfText(rawText: String): String {
        val lines = rawText.lines()
        val output = StringBuilder()
        var paragraph = StringBuilder()

        fun flushParagraph() {
            val text = paragraph.toString().trim()
            if (text.isNotEmpty()) {
                output.append(text).append("\n\n")
            }
            paragraph = StringBuilder()
        }

        for (i in lines.indices) {
            val line = lines[i].trim()
            if (line.isEmpty()) {
                flushParagraph()
                continue
            }

            val isBullet = line.startsWith("•") ||
                line.startsWith("-") ||
                line.startsWith("*") ||
                line.matches(Regex("^\\d+\\..*"))
            if (isBullet) {
                flushParagraph()
                val normalized = line
                    .replaceFirst(Regex("^[•*-]"), "-")
                    .trim()
                output.append(normalized).append("\n")
                continue
            }

            if (paragraph.isNotEmpty()) {
                val previous = paragraph.last()
                if (previous == '-' && line.firstOrNull()?.isLowerCase() == true) {
                    paragraph.deleteCharAt(paragraph.length - 1)
                    paragraph.append(line)
                } else {
                    paragraph.append(' ').append(line)
                }
            } else {
                paragraph.append(line)
            }
        }

        flushParagraph()
        return output.toString().trim()
    }

    fun cleanMarkdown(markdown: String): String {
        return markdown
            .replace(Regex("[ \\t]+\\n"), "\n")
            .replace(Regex("\\n{3,}"), "\n\n")
            .trim()
    }
}
