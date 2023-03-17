package ipm.stgit.qrapp

import android.R.attr.bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    val str =
        "{\"companyCode\":\"STG\",\"secretKey\":\"AdCtRyJyJx9QpSP63b1DjTCFAXS8XrZLaBKMiPnIF3Q=\"}"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateQr();
    }

    private fun generateQr() {

        val qrgEncoder = QRGEncoder(str, null, QRGContents.Type.TEXT, 100)
        qrgEncoder.setColorBlack(Color.WHITE)
        qrgEncoder.setColorWhite(Color.BLACK)
        try {
            // Getting QR-Code as Bitmap
            val bitmap = qrgEncoder.getBitmap()
            var img = findViewById<ImageView>(R.id.imgQr)
            // Setting Bitmap to ImageView
            img.setImageBitmap(bitmap)
        } catch (e: java.lang.Exception) {
            Log.d("dsdsds", e.localizedMessage)
        }
    }
}