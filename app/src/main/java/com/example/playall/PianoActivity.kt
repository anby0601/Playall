package com.example.playall

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playall.databinding.ActivityPianoBinding

class PianoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPianoBinding
    private val keyMap = mutableMapOf<View, Int>()
    private val keyMapFirst = mutableMapOf<View, Int>()
    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<Int, Int>()
    private val soundMapFirst = mutableMapOf<Int, Int>()

    private var metronomeHandler: Handler? = null
    private var metronomeRunnable: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPianoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Piano"

        binding.scrollviewPiano.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val targetView = binding.C4

                    val scrollToX = targetView.left

                    binding.scrollviewPiano.scrollTo(scrollToX, 0)

                    binding.scrollviewPiano.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })


        soundPool = SoundPool.Builder().setMaxStreams(10).build()

        val metronomeSoundId = soundPool.load(this, R.raw.metronome, 1)
        soundMap[R.raw.metronome] = metronomeSoundId

        initKeyMapFirst()
        loadSoundsFirst()
        setPlaySoundsFirst()
        initKeyMap()
        loadSounds()
        setPlaySounds()



        binding.scrollviewPiano.setOnTouchListener { _, _ ->
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.size_menu1, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.small -> {
                startActivity(Intent(this, PianoActivity4::class.java))
                finish()
            }
            R.id.medium -> {
                startActivity(Intent(this, PianoActivity3::class.java))
                finish()
            }
            R.id.extraLarge -> {
                startActivity(Intent(this, PianoActivity2::class.java))
                finish()
            }
            R.id.metronome -> showMetronomeDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMetronomeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Metronome BPM (10 ~ 250)")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("Start") { _, _ ->
            val bpm = input.text.toString().toIntOrNull()
            if (bpm != null && bpm in 10..250) {
                startMetronome(bpm)
            } else {
                Toast.makeText(this, "Please enter a BPM between 10 and 250", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.setNeutralButton("Stop") { _, _ ->
            stopMetronome()
        }

        builder.show()
    }

    private fun startMetronome(bpm: Int) {
        stopMetronome()
        val interval = 60000 / bpm
        metronomeHandler = Handler(Looper.getMainLooper())
        metronomeRunnable = object : Runnable {
            override fun run() {
                val soundId = soundMap[R.raw.metronome]
                soundId?.let { id ->
                    soundPool.play(id, 1f, 1f, 1, 0, 1f)
                }
                metronomeHandler?.postDelayed(this, interval.toLong())
            }
        }
        metronomeHandler?.post(metronomeRunnable!!)
    }

    private fun stopMetronome() {
        metronomeHandler?.removeCallbacks(metronomeRunnable!!)
    }

    private fun initKeyMapFirst(){
        keyMapFirst.apply {
            put(binding.C4, R.raw.c4)
            put(binding.C4Sharp, R.raw.c4_sharp)
            put(binding.D4, R.raw.d4)
            put(binding.D4Sharp, R.raw.d4_sharp)
            put(binding.E4, R.raw.e4)
            put(binding.F4, R.raw.f4)
            put(binding.F4Sharp, R.raw.f4_sharp)
            put(binding.G4, R.raw.g4)
            put(binding.G4Sharp, R.raw.g4_sharp)
            put(binding.A4, R.raw.a4)
            put(binding.A4Sharp, R.raw.a4_sharp)
            put(binding.B4, R.raw.b4)

            put(binding.C5, R.raw.c5)
            put(binding.C5Sharp, R.raw.c5_sharp)
            put(binding.D5, R.raw.d5)
            put(binding.D5Sharp, R.raw.d5_sharp)
            put(binding.E5, R.raw.e5)
            put(binding.F5, R.raw.f5)
            put(binding.F5Sharp, R.raw.f5_sharp)
            put(binding.G5, R.raw.g5)
            put(binding.G5Sharp, R.raw.g5_sharp)
            put(binding.A5, R.raw.a5)
            put(binding.A5Sharp, R.raw.a5_sharp)
            put(binding.B5, R.raw.b5)
        }
    }

    private fun loadSoundsFirst() {
        keyMapFirst.values.forEach { resId ->
            val soundId = soundPool.load(this, resId, 1)
            soundMapFirst[resId] = soundId
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setPlaySoundsFirst() {
        keyMapFirst.forEach { button, resId ->
            button.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 버튼이 눌렸을 때의 처리
                        val soundId = soundMapFirst[resId]
                        soundId?.let { id ->
                            soundPool.play(id, 1f, 1f, 1, 0, 1f)
                        }
                        button.isPressed = true
                    }
                    MotionEvent.ACTION_MOVE -> button.isPressed = false
                    MotionEvent.ACTION_UP -> button.isPressed = false
                }
                true
            }
        }
    }


    private fun initKeyMap(){
        keyMap.apply {
            put(binding.A0, R.raw.a0)
            put(binding.A0Sharp, R.raw.a0_sharp)
            put(binding.B0, R.raw.b0)

            put(binding.C1, R.raw.c1)
            put(binding.C1Sharp, R.raw.c1_sharp)
            put(binding.D1, R.raw.d1)
            put(binding.D1Sharp, R.raw.d1_sharp)
            put(binding.E1, R.raw.e1)
            put(binding.F1, R.raw.f1)
            put(binding.F1Sharp, R.raw.f1_sharp)
            put(binding.G1, R.raw.g1)
            put(binding.G1Sharp, R.raw.g1_sharp)
            put(binding.A1, R.raw.a1)
            put(binding.A1Sharp, R.raw.a1_sharp)
            put(binding.B1, R.raw.b1)

            put(binding.C2, R.raw.c2)
            put(binding.C2Sharp, R.raw.c2_sharp)
            put(binding.D2, R.raw.d2)
            put(binding.D2Sharp, R.raw.d2_sharp)
            put(binding.E2, R.raw.e2)
            put(binding.F2, R.raw.f2)
            put(binding.F2Sharp, R.raw.f2_sharp)
            put(binding.G2, R.raw.g2)
            put(binding.G2Sharp, R.raw.g2_sharp)
            put(binding.A2, R.raw.a2)
            put(binding.A2Sharp, R.raw.a2_sharp)
            put(binding.B2, R.raw.b2)

            put(binding.C3, R.raw.c3)
            put(binding.C3Sharp, R.raw.c3_sharp)
            put(binding.D3, R.raw.d3)
            put(binding.D3Sharp, R.raw.d3_sharp)
            put(binding.E3, R.raw.e3)
            put(binding.F3, R.raw.f3)
            put(binding.F3Sharp, R.raw.f3_sharp)
            put(binding.G3, R.raw.g3)
            put(binding.G3Sharp, R.raw.g3_sharp)
            put(binding.A3, R.raw.a3)
            put(binding.A3Sharp, R.raw.a3_sharp)
            put(binding.B3, R.raw.b3)



            put(binding.C6, R.raw.c6)
            put(binding.C6Sharp, R.raw.c6_sharp)
            put(binding.D6, R.raw.d6)
            put(binding.D6Sharp, R.raw.d6_sharp)
            put(binding.E6, R.raw.e6)
            put(binding.F6, R.raw.f6)
            put(binding.F6Sharp, R.raw.f6_sharp)
            put(binding.G6, R.raw.g6)
            put(binding.G6Sharp, R.raw.g6_sharp)
            put(binding.A6, R.raw.a6)
            put(binding.A6Sharp, R.raw.a6_sharp)
            put(binding.B6, R.raw.b6)

            put(binding.C7, R.raw.c7)
            put(binding.C7Sharp, R.raw.c7_sharp)
            put(binding.D7, R.raw.d7)
            put(binding.D7Sharp, R.raw.d7_sharp)
            put(binding.E7, R.raw.e7)
            put(binding.F7, R.raw.f7)
            put(binding.F7Sharp, R.raw.f7_sharp)
            put(binding.G7, R.raw.g7)
            put(binding.G7Sharp, R.raw.g7_sharp)
            put(binding.A7, R.raw.a7)
            put(binding.A7Sharp, R.raw.a7_sharp)
            put(binding.B7, R.raw.b7)

            put(binding.C8, R.raw.c8)
        }
    }



    private fun loadSounds() {
        keyMap.values.forEach { resId ->
            val soundId = soundPool.load(this, resId, 1)
            soundMap[resId] = soundId
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setPlaySounds() {
        keyMap.forEach { button, resId ->
            button.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 버튼이 눌렸을 때의 처리
                        val soundId = soundMap[resId]
                        soundId?.let { id ->
                            soundPool.play(id, 1f, 1f, 1, 0, 1f)
                        }
                        button.isPressed = true
                    }
                    MotionEvent.ACTION_MOVE -> button.isPressed = false
                    MotionEvent.ACTION_UP -> button.isPressed = false
                }
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}
