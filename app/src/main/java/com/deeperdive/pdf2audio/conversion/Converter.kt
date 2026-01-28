package com.deeperdive.pdf2audio.conversion

import java.io.InputStream

interface Converter {
    fun convert(inputStream: InputStream, displayName: String?): ConversionResult
}
