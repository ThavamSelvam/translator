package com.stgit.voicetranslator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.*

class VoiceActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var edtText: EditText
    lateinit var edtText1: EditText
    lateinit var textView: TextView
    lateinit var textView1: TextView
    lateinit var error: TextView
    lateinit var error1: TextView
    var englishTamilTranslator: Translator? = null
    var tamilEnglishTranslator: Translator? = null
    private val languageIdentification = LanguageIdentification.getClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpTamilToEnglishLang()
        setUpEngToTamilLang()
        findViewById<AppCompatImageView>(R.id.mic).setOnClickListener(this)
        findViewById<AppCompatImageView>(R.id.mic1).setOnClickListener(this)
        error1 = findViewById(R.id.error1)
        error = findViewById(R.id.error)
        edtText = findViewById(R.id.edtText)
        edtText1 = findViewById(R.id.edtText1)
        textView1 = findViewById(R.id.textView1)
        textView = findViewById(R.id.textView)
        error1.setText("Please wait translation is downloading from server")
        error.setText("Please wait translation is downloading from server")
        edtText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    callTranslator(s.toString())
                }
            }
        })
        edtText1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    callTranslatorEngToTamil(s.toString())
                }
            }
        })

    }

    private fun setUpEngToTamilLang() {
        val options: TranslatorOptions =
            TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.TAMIL).build()
        tamilEnglishTranslator = options.let { Translation.getClient(it) }
        tamilEnglishTranslator!!.downloadModelIfNeeded().addOnSuccessListener {
            error1.setText("Start translating... ")
            callTranslatorEngToTamil("Hi Good morning")
            failedFromApi1 = false
        }.addOnFailureListener { e ->
            error.setText(e.message)
            failureTxt1 = e.message.toString()
            failedFromApi1 = true
        }

    }

    private fun callTranslatorEngToTamil(s: String) {


        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(s).addOnSuccessListener { languageCode ->
            if (languageCode == "und") {
                error1.setText("Can't identify language")
            } else {
                Log.i("", "Language: $languageCode")
                // error.setText("Language - $languageCode")
            }
        }.addOnFailureListener {}
        error1.setText("Language - Tamil to english")

        if (failedFromApi) {
            //error1.setText(failureTxt)
        }
        tamilEnglishTranslator!!.translate(s)
            .addOnSuccessListener { translatedText -> // Translation successful.
                textView1.setText(translatedText)
            }.addOnFailureListener { e -> // Error.
                textView1.setText(e.message)
            }
    }


    private fun setUpTamilToEnglishLang() {
        val options: TranslatorOptions =
            TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.TAMIL)
                .setTargetLanguage(TranslateLanguage.ENGLISH).build()
        englishTamilTranslator = options.let { Translation.getClient(it) }
        englishTamilTranslator!!.downloadModelIfNeeded().addOnSuccessListener {
            error.setText("Start translating... ")
            callTranslator("காலை வணக்கம்")
            failedFromApi = false
        }.addOnFailureListener { e ->
            error.setText(e.message)
            failureTxt = e.message.toString()
            failedFromApi = true
        }
    }

    var failedFromApi: Boolean = false
    var failedFromApi1: Boolean = false
    var failureTxt = ""
    var failureTxt1 = ""

    @SuppressLint("SetTextI18n")
    private fun callTranslator(s: String) {

        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(s).addOnSuccessListener { languageCode ->
            if (languageCode == "und") {
                error.setText("Can't identify language")
            } else {
                Log.i("", "Language: $languageCode")
                // error.setText("Language - $languageCode")
            }
        }.addOnFailureListener {}
        error.setText("Language - Tamil to english")

        if (failedFromApi) {
            error.setText(failureTxt)
        }
        englishTamilTranslator!!.translate(s)
            .addOnSuccessListener { translatedText -> // Translation successful.
                textView.setText(translatedText)
            }.addOnFailureListener { e -> // Error.
                textView.setText(e.message)
            }
    }

    var micSelected = false
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.mic -> micSelected = true
                R.id.mic1 -> micSelected = false
            }
        }
        voice();
    }

    fun voice() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        if (micSelected)
            startForResult.launch(
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE, "ta"
                )
            )
        else {
            startForResult.launch(
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH
                )
            )
        }

    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val result: ArrayList<String> =
                    result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!
                if (micSelected) {
                    edtText.setText(result[0])
                } else {
                    edtText1.setText(result[0])
                }
            }

        }

}