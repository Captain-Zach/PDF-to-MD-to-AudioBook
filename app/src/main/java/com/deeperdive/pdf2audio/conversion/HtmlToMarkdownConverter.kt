package com.deeperdive.pdf2audio.conversion

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object HtmlToMarkdownConverter {
    fun convert(html: String): String {
        val document = Jsoup.parse(html)
        document.outputSettings().prettyPrint(false)
        document.select("script,style,nav,footer,header").remove()
        val body = document.body() ?: return ""

        val output = StringBuilder()
        for (child in body.children()) {
            appendElement(child, output, 0)
        }
        return output.toString().trim()
    }

    private fun appendElement(element: Element, output: StringBuilder, listDepth: Int) {
        when (element.tagName().lowercase()) {
            "h1" -> appendHeading(output, 1, element.text())
            "h2" -> appendHeading(output, 2, element.text())
            "h3" -> appendHeading(output, 3, element.text())
            "h4" -> appendHeading(output, 4, element.text())
            "h5" -> appendHeading(output, 5, element.text())
            "h6" -> appendHeading(output, 6, element.text())
            "p" -> appendParagraph(output, element.text())
            "blockquote" -> appendBlockquote(output, element.text())
            "pre" -> appendCodeBlock(output, element.wholeText())
            "ul" -> appendList(output, element, listDepth, ordered = false)
            "ol" -> appendList(output, element, listDepth, ordered = true)
            "br" -> output.append("\n")
            else -> {
                if (element.childrenSize() == 0) {
                    appendParagraph(output, element.text())
                } else {
                    for (child in element.children()) {
                        appendElement(child, output, listDepth)
                    }
                }
            }
        }
    }

    private fun appendHeading(output: StringBuilder, level: Int, text: String) {
        val cleaned = text.trim()
        if (cleaned.isNotEmpty()) {
            output.append("#".repeat(level)).append(' ').append(cleaned).append("\n\n")
        }
    }

    private fun appendParagraph(output: StringBuilder, text: String) {
        val cleaned = text.trim()
        if (cleaned.isNotEmpty()) {
            output.append(cleaned).append("\n\n")
        }
    }

    private fun appendBlockquote(output: StringBuilder, text: String) {
        val cleaned = text.trim()
        if (cleaned.isNotEmpty()) {
            output.append("> ").append(cleaned).append("\n\n")
        }
    }

    private fun appendCodeBlock(output: StringBuilder, text: String) {
        val cleaned = text.trimEnd()
        if (cleaned.isNotEmpty()) {
            output.append("```\n").append(cleaned).append("\n```\n\n")
        }
    }

    private fun appendList(
        output: StringBuilder,
        listElement: Element,
        listDepth: Int,
        ordered: Boolean
    ) {
        val items = listElement.children().filter { it.tagName().lowercase() == "li" }
        var index = 1
        for (item in items) {
            val indent = "  ".repeat(listDepth)
            val marker = if (ordered) "${index}." else "-"
            val text = item.ownText().trim()
            if (text.isNotEmpty()) {
                output.append(indent).append(marker).append(' ').append(text).append("\n")
            }
            for (child in item.children()) {
                val tag = child.tagName().lowercase()
                if (tag == "ul" || tag == "ol") {
                    appendList(output, child, listDepth + 1, ordered = tag == "ol")
                } else {
                    appendParagraph(output, child.text())
                }
            }
            index += 1
        }
        output.append("\n")
    }
}
