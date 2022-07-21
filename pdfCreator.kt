package com.pdfcreator

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.io.File

class PDFCreator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfcreator)

        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.pdf_layout, null)


        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        } else {
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        val pdfDocument = PdfDocument()
        val pageInfo =
            PdfDocument.PageInfo.Builder(displayMetrics.widthPixels, displayMetrics.heightPixels, 1)
                .create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        pdfDocument.finishPage(page)

        findViewById<Button>(R.id.button).setOnClickListener {
            try {
                /*Log.d("pdffile",file.absolutePath)
                tv1.text = "${file.absoluteFile}"*/

                val fileName = "doc${System.currentTimeMillis()}.pdf"

                val file = File(
                    Environment.getExternalStorageDirectory().getPath() + "/Download",
                    fileName
                )

                Log.d("filepath", file.absolutePath)
                pdfDocument.writeTo(file.outputStream())
                pdfDocument.close()
                Toast.makeText(applicationContext, "PDF file generated..", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: Exception) {
                e.printStackTrace()

                Toast.makeText(
                    applicationContext,
                    "Fail to generate PDF file..",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}