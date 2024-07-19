package com.example.playall

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playall.databinding.ActivityEditChordBinding

class EditChordActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditChordBinding
    private lateinit var buttonChords: Array<String>
    private lateinit var textViews: Array<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditChordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Edit Chord"

//        buttonChords = intent.getStringArrayExtra("chordArray") ?: arrayOf()
//        buttonChords = arrayOf(
//            "C#sus4", "Db7", "Ebdim", "Fsus4", "Gb9", "AM7", "Bdim", "-"  // 각 버튼에 해당하는 코드
//        )

        // 하단 TextView 배열 초기화
        textViews = arrayOf(
            binding.chordText1,
            binding.chordText2,
            binding.chordText3,
            binding.chordText4,
            binding.chordText5,
            binding.chordText6,
            binding.chordText7,
            binding.chordText8
        )

        // 상단 버튼 배열 초기화 및 클릭 리스너 설정
        val buttonIds = arrayOf(
            binding.C, binding.Cm, binding.C7, binding.Cm7, binding.CM7, binding.Cdim, binding.Csus4, binding.C6, binding.C9, binding.C11,
            binding.Csharp, binding.Csharpm, binding.Csharp7, binding.Csharpm7, binding.CsharpM7, binding.Csharpdim, binding.Csharpsus4, binding.Csharp6, binding.Csharp9, binding.Csharp11,
            binding.Db, binding.Dbm, binding.Db7, binding.Dbm7, binding.DbM7, binding.Dbdim, binding.Dbsus4, binding.Db6, binding.Db9, binding.Db11,
            binding.D, binding.Dm, binding.D7, binding.Dm7, binding.DM7, binding.Ddim, binding.Dsus4, binding.D6, binding.D9, binding.D11,
            binding.Dsharp, binding.Dsharpm, binding.Dsharp7, binding.Dsharpm7, binding.DsharpM7, binding.Dsharpdim, binding.Dsharpsus4, binding.Dsharp6, binding.Dsharp9, binding.Dsharp11,
            binding.Eb, binding.Ebm, binding.Eb7, binding.Ebm7, binding.EbM7, binding.Ebdim, binding.Ebsus4, binding.Eb6, binding.Eb9, binding.Eb11,
            binding.E, binding.Em, binding.E7, binding.Em7, binding.EM7, binding.Edim, binding.Esus4, binding.E6, binding.E9, binding.E11,
            binding.F, binding.Fm, binding.F7, binding.Fm7, binding.FM7, binding.Fdim, binding.Fsus4, binding.F6, binding.F9, binding.F11,
            binding.Fsharp, binding.Fsharpm, binding.Fsharp7, binding.Fsharpm7, binding.FsharpM7, binding.Fsharpdim, binding.Fsharpsus4, binding.Fsharp6, binding.Fsharp9, binding.Fsharp11,
            binding.Gb, binding.Gbm, binding.Gb7, binding.Gbm7, binding.GbM7, binding.Gbdim, binding.Gbsus4, binding.Gb6, binding.Gb9, binding.Gb11,
            binding.G, binding.Gm, binding.G7, binding.Gm7, binding.GM7, binding.Gdim, binding.Gsus4, binding.G6, binding.G9, binding.G11,
            binding.Gsharp, binding.Gsharpm, binding.Gsharp7, binding.Gsharpm7, binding.GsharpM7, binding.Gsharpdim, binding.Gsharpsus4, binding.Gsharp6, binding.Gsharp9, binding.Gsharp11,
            binding.Ab, binding.Abm, binding.Ab7, binding.Abm7, binding.AbM7, binding.Abdim, binding.Absus4, binding.Ab6, binding.Ab9, binding.Ab11,
            binding.A, binding.Am, binding.A7, binding.Am7, binding.AM7, binding.Adim, binding.Asus4, binding.A6, binding.A9, binding.A11,
            binding.Asharp, binding.Asharpm, binding.Asharp7, binding.Asharpm7, binding.AsharpM7, binding.Asharpdim, binding.Asharpsus4, binding.Asharp6, binding.Asharp9, binding.Asharp11,
            binding.Bb, binding.Bbm, binding.Bb7, binding.Bbm7, binding.BbM7, binding.Bbdim, binding.Bbsus4, binding.Bb6, binding.Bb9, binding.Bb11,
            binding.B, binding.Bm, binding.B7, binding.Bm7, binding.BM7, binding.Bdim, binding.Bsus4, binding.B6, binding.B9, binding.B11
        )

        for (button in buttonIds) {
            button.setOnClickListener { onChordButtonClick(button) }
        }

        binding.editButton2.setOnClickListener {
            // buttonChords 배열을 크기 8로 초기화
            buttonChords = arrayOf(
                if (binding.chordText1.text.toString().isEmpty()) "-" else binding.chordText1.text.toString(),
                if (binding.chordText2.text.toString().isEmpty()) "-" else binding.chordText2.text.toString(),
                if (binding.chordText3.text.toString().isEmpty()) "-" else binding.chordText3.text.toString(),
                if (binding.chordText4.text.toString().isEmpty()) "-" else binding.chordText4.text.toString(),
                if (binding.chordText5.text.toString().isEmpty()) "-" else binding.chordText5.text.toString(),
                if (binding.chordText6.text.toString().isEmpty()) "-" else binding.chordText6.text.toString(),
                if (binding.chordText7.text.toString().isEmpty()) "-" else binding.chordText7.text.toString(),
                if (binding.chordText8.text.toString().isEmpty()) "-" else binding.chordText8.text.toString()
            )
            // 수정된 buttonChords 값을 인텐트에 담아 반환
            val resultIntent = Intent()
            resultIntent.putExtra("chordArray", buttonChords)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }

    private fun onChordButtonClick(button: Button) {
        val buttonText = button.text.toString()
        var found = false

        for (textView in textViews) {
            if (textView.text == buttonText && textView.visibility == View.VISIBLE) {
                textView.visibility = View.GONE
                textView.text = "-"
                found = true
                break
            }
        }

        if (!found) {
            for (textView in textViews) {
                if (textView.visibility == View.GONE) {
                    textView.visibility = View.VISIBLE
                    textView.text = buttonText
                    break
                }
            }
        }

        rearrangeTextViews()
    }

    private fun rearrangeTextViews() {
        val visibleTexts = textViews.filter { it.visibility == View.VISIBLE }.map { it.text.toString() }
        for (i in textViews.indices) {
            if (i < visibleTexts.size) {
                textViews[i].visibility = View.VISIBLE
                textViews[i].text = visibleTexts[i]
            } else {
                textViews[i].visibility = View.GONE
                textViews[i].text = ""
            }
        }
    }
}
