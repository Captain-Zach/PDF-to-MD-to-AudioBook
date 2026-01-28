package com.deeperdive.pdf2audio

import android.app.Application
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader

class Pdf2AudioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PDFBoxResourceLoader.init(applicationContext)
    }
}
