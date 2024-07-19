package com.example.playall

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playall.databinding.ActivityGuitarBinding

class GuitarActivity : AppCompatActivity() {
    lateinit var binding: ActivityGuitarBinding
    lateinit var soundPool: SoundPool
    lateinit var buttons: Array<Button>
    private val soundMap = mutableMapOf<Int, Int>()

    private var metronomeHandler: Handler? = null
    private var metronomeRunnable: Runnable? = null


    var soundId: Int = 0

    var noteIndices = intArrayOf(0, 0, 0, 0, 0, 0)

    var buttonChords = arrayOf(
        "C", "D", "E", "F", "G", "A", "B", "-"
    )

    var activeButton: Button? = null

    private var lastTouchedButton: ImageButton? = null

    val baseFrequency = 209.88f

    val eStringPitches = floatArrayOf(
        82.41f / baseFrequency,  // E2
        87.31f / baseFrequency,  // F2
        92.50f / baseFrequency,  // F#2
        98.00f / baseFrequency,  // G2
        103.83f / baseFrequency, // G#2
        110.00f / baseFrequency, // A2
        116.54f / baseFrequency, // A#2
        123.47f / baseFrequency, // B2
        130.81f / baseFrequency, // C3
        138.59f / baseFrequency, // C#3
        146.83f / baseFrequency, // D3
        155.56f / baseFrequency, // D#3
        164.81f / baseFrequency  // E3
    )

    val aStringPitches = floatArrayOf(
        110.00f / baseFrequency, // A2
        116.54f / baseFrequency, // A#2
        123.47f / baseFrequency, // B2
        130.81f / baseFrequency, // C3
        138.59f / baseFrequency, // C#3
        146.83f / baseFrequency, // D3
        155.56f / baseFrequency, // D#3
        164.81f / baseFrequency, // E3
        174.61f / baseFrequency, // F3
        185.00f / baseFrequency, // F#3
        196.00f / baseFrequency, // G3
        207.65f / baseFrequency, // G#3
        220.00f / baseFrequency  // A3
    )

    val dStringPitches = floatArrayOf(
        146.83f / baseFrequency, // D3
        155.56f / baseFrequency, // D#3
        164.81f / baseFrequency, // E3
        174.61f / baseFrequency, // F3
        185.00f / baseFrequency, // F#3
        196.00f / baseFrequency, // G3
        207.65f / baseFrequency, // G#3
        220.00f / baseFrequency, // A3
        233.08f / baseFrequency, // A#3
        246.94f / baseFrequency, // B3
        261.63f / baseFrequency, // C4
        277.18f / baseFrequency, // C#4
        293.66f / baseFrequency  // D4
    )

    val gStringPitches = floatArrayOf(
        196.00f / baseFrequency, // G3
        207.65f / baseFrequency, // G#3
        220.00f / baseFrequency, // A3
        233.08f / baseFrequency, // A#3
        246.94f / baseFrequency, // B3
        261.63f / baseFrequency, // C4
        277.18f / baseFrequency, // C#4
        293.66f / baseFrequency, // D4
        311.13f / baseFrequency, // D#4
        329.63f / baseFrequency, // E4
        349.23f / baseFrequency, // F4
        369.99f / baseFrequency, // F#4
        392.00f / baseFrequency  // G4
    )

    val bStringPitches = floatArrayOf(
        246.94f / baseFrequency, // B3
        261.63f / baseFrequency, // C4
        277.18f / baseFrequency, // C#4
        293.66f / baseFrequency, // D4
        311.13f / baseFrequency, // D#4
        329.63f / baseFrequency, // E4
        349.23f / baseFrequency, // F4
        369.99f / baseFrequency, // F#4
        392.00f / baseFrequency, // G4
        415.30f / baseFrequency, // G#4
        440.00f / baseFrequency, // A4
        466.16f / baseFrequency, // A#4
        493.88f / baseFrequency  // B4
    )

    val highEStringPitches = floatArrayOf(
        329.63f / baseFrequency, // E4
        349.23f / baseFrequency, // F4
        369.99f / baseFrequency, // F#4
        392.00f / baseFrequency, // G4
        415.30f / baseFrequency, // G#4
        440.00f / baseFrequency, // A4
        466.16f / baseFrequency, // A#4
        493.88f / baseFrequency, // B4
        523.25f / baseFrequency, // C5
        554.37f / baseFrequency, // C#5
        587.33f / baseFrequency, // D5
        622.25f / baseFrequency, // D#5
        659.25f / baseFrequency  // E5
    )

    private val chords = arrayOf(
        arrayOf(
            intArrayOf(0, 3, 2, 0, 1, 0),  // C
            intArrayOf(0, 3, 2, 0, 1, 3),  // Cm
            intArrayOf(0, 3, 2, 0, 1, 1),  // C7
            intArrayOf(0, 3, 2, 0, 1, 2),  // Cm7
            intArrayOf(0, 3, 2, 0, 0, 0),  // CM7
            intArrayOf(0, 1, 2, 0, 1, 0),  // Cdim
            intArrayOf(0, 3, 3, 0, 1, 0),  // Csus4
            intArrayOf(0, 3, 2, 0, 3, 0),  // C6
            intArrayOf(0, 3, 2, 3, 1, 0),  // C9
            intArrayOf(0, 3, 2, 0, 1, 3),  // C11
        ),
        arrayOf(
            intArrayOf(1, 4, 3, 1, 2, 1),  // C#
            intArrayOf(1, 4, 3, 1, 2, 4),  // C#m
            intArrayOf(1, 4, 3, 1, 2, 2),  // C#7
            intArrayOf(1, 4, 3, 1, 2, 3),  // C#m7
            intArrayOf(1, 4, 3, 1, 1, 1),  // C#M7
            intArrayOf(1, 2, 3, 1, 2, 1),  // C#dim
            intArrayOf(1, 4, 4, 1, 2, 1),  // C#sus4
            intArrayOf(1, 4, 3, 1, 4, 1),  // C#6
            intArrayOf(1, 4, 3, 4, 2, 1),  // C#9
            intArrayOf(1, 4, 3, 1, 2, 4),  // C#11
        ),
        arrayOf(
            intArrayOf(1, 4, 3, 1, 2, 1),  // Db (same as C#)
            intArrayOf(1, 4, 3, 1, 2, 4),  // Dbm (same as C#m)
            intArrayOf(1, 4, 3, 1, 2, 2),  // Db7 (same as C#7)
            intArrayOf(1, 4, 3, 1, 2, 3),  // Dbm7 (same as C#m7)
            intArrayOf(1, 4, 3, 1, 1, 1),  // DbM7 (same as C#M7)
            intArrayOf(1, 2, 3, 1, 2, 1),  // Dbdim (same as C#dim)
            intArrayOf(1, 4, 4, 1, 2, 1),  // Dbsus4 (same as C#sus4)
            intArrayOf(1, 4, 3, 1, 4, 1),  // Db6 (same as C#6)
            intArrayOf(1, 4, 3, 4, 2, 1),  // Db9 (same as C#9)
            intArrayOf(1, 4, 3, 1, 2, 4),  // Db11 (same as C#11)
        ),
        arrayOf(
            intArrayOf(0, 0, 0, 2, 3, 2),  // D
            intArrayOf(0, 0, 0, 2, 3, 1),  // Dm
            intArrayOf(0, 0, 0, 2, 1, 2),  // D7
            intArrayOf(0, 0, 0, 1, 3, 1),  // Dm7
            intArrayOf(2, 0, 0, 2, 2, 2),  // DM7
            intArrayOf(2, 3, 4, 2, 3, 2),  // Ddim
            intArrayOf(0, 0, 0, 2, 3, 3),  // Dsus4
            intArrayOf(0, 0, 0, 2, 0, 2),  // D6
            intArrayOf(2, 0, 0, 2, 2, 0),  // D9
            intArrayOf(2, 0, 0, 2, 2, 3),  // D11
        ),
        arrayOf(
            intArrayOf(1, 1, 1, 3, 4, 3),  // D#
            intArrayOf(1, 1, 1, 3, 4, 2),  // D#m
            intArrayOf(1, 1, 1, 3, 4, 4),  // D#7
            intArrayOf(1, 1, 1, 3, 4, 1),  // D#m7
            intArrayOf(3, 1, 1, 3, 3, 3),  // D#M7
            intArrayOf(3, 4, 5, 3, 4, 3),  // D#dim
            intArrayOf(1, 1, 1, 3, 4, 4),  // D#sus4
            intArrayOf(1, 1, 1, 3, 1, 3),  // D#6
            intArrayOf(3, 1, 1, 3, 3, 1),  // D#9
            intArrayOf(3, 1, 1, 3, 3, 4),  // D#11
        ),
        arrayOf(
            intArrayOf(1, 1, 1, 3, 4, 3),  // Eb (same as D#)
            intArrayOf(1, 1, 1, 3, 4, 2),  // Ebm (same as D#m)
            intArrayOf(1, 1, 1, 3, 4, 4),  // Eb7 (same as D#7)
            intArrayOf(1, 1, 1, 3, 4, 1),  // Ebm7 (same as D#m7)
            intArrayOf(3, 1, 1, 3, 3, 3),  // EbM7 (same as D#M7)
            intArrayOf(3, 4, 5, 3, 4, 3),  // Ebdim (same as D#dim)
            intArrayOf(1, 1, 1, 3, 4, 4),  // Ebsus4 (same as D#sus4)
            intArrayOf(1, 1, 1, 3, 1, 3),  // Eb6 (same as D#6)
            intArrayOf(3, 1, 1, 3, 3, 1),  // Eb9 (same as D#9)
            intArrayOf(3, 1, 1, 3, 3, 4),  // Eb11 (same as D#11)
        ),
        arrayOf(
            intArrayOf(0, 2, 2, 1, 0, 0),  // E
            intArrayOf(0, 2, 2, 0, 0, 0),  // Em
            intArrayOf(0, 2, 0, 1, 0, 0),  // E7
            intArrayOf(0, 2, 0, 0, 0, 0),  // Em7
            intArrayOf(0, 2, 1, 1, 0, 0),  // EM7
            intArrayOf(0, 1, 2, 1, 2, 1),  // Edim
            intArrayOf(0, 2, 2, 2, 0, 0),  // Esus4
            intArrayOf(0, 2, 2, 1, 0, 2),  // E6
            intArrayOf(2, 2, 0, 1, 2, 0),  // E9
            intArrayOf(2, 2, 0, 1, 0, 0),  // E11
        ),
        arrayOf(
            intArrayOf(1, 3, 3, 2, 1, 1),  // F
            intArrayOf(1, 3, 3, 1, 1, 1),  // Fm
            intArrayOf(1, 3, 1, 2, 1, 1),  // F7
            intArrayOf(1, 2, 3, 1, 1, 1),  // Fm7
            intArrayOf(1, 3, 2, 2, 1, 1),  // FM7
            intArrayOf(1, 2, 3, 2, 3, 2),  // Fdim
            intArrayOf(1, 3, 3, 3, 1, 1),  // Fsus4
            intArrayOf(1, 3, 3, 2, 1, 3),  // F6
            intArrayOf(3, 3, 1, 2, 3, 1),  // F9
            intArrayOf(3, 3, 1, 2, 1, 1),  // F11
        ),
        arrayOf(
            intArrayOf(2, 4, 4, 3, 2, 2),  // F#
            intArrayOf(2, 4, 4, 2, 2, 2),  // F#m
            intArrayOf(2, 4, 2, 3, 2, 2),  // F#7
            intArrayOf(2, 3, 4, 2, 2, 2),  // F#m7
            intArrayOf(2, 4, 3, 3, 2, 2),  // F#M7
            intArrayOf(2, 3, 4, 3, 4, 3),  // F#dim
            intArrayOf(2, 4, 4, 4, 2, 2),  // F#sus4
            intArrayOf(2, 4, 4, 3, 2, 4),  // F#6
            intArrayOf(4, 4, 2, 3, 4, 2),  // F#9
            intArrayOf(4, 4, 2, 3, 2, 2),  // F#11
        ),
        arrayOf(
            intArrayOf(2, 4, 4, 3, 2, 2),  // Gb (same as F#)
            intArrayOf(2, 4, 4, 2, 2, 2),  // Gbm (same as F#m)
            intArrayOf(2, 4, 2, 3, 2, 2),  // Gb7 (same as F#7)
            intArrayOf(2, 3, 4, 2, 2, 2),  // Gbm7 (same as F#m7)
            intArrayOf(2, 4, 3, 3, 2, 2),  // GbM7 (same as F#M7)
            intArrayOf(2, 3, 4, 3, 4, 3),  // Gbdim (same as F#dim)
            intArrayOf(2, 4, 4, 4, 2, 2),  // Gbsus4 (same as F#sus4)
            intArrayOf(2, 4, 4, 3, 2, 4),  // Gb6 (same as F#6)
            intArrayOf(4, 4, 2, 3, 4, 2),  // Gb9 (same as F#9)
            intArrayOf(4, 4, 2, 3, 2, 2),  // Gb11 (same as F#11)
        ),
        arrayOf(
            intArrayOf(3, 2, 0, 0, 0, 3),  // G
            intArrayOf(3, 2, 0, 0, 3, 3),  // Gm
            intArrayOf(3, 2, 0, 0, 0, 1),  // G7
            intArrayOf(3, 1, 0, 0, 3, 1),  // Gm7
            intArrayOf(3, 2, 0, 0, 0, 2),  // GM7
            intArrayOf(3, 1, 0, 0, 0, 0),  // Gdim
            intArrayOf(3, 3, 0, 0, 3, 3),  // Gsus4
            intArrayOf(3, 2, 0, 0, 3, 0),  // G6
            intArrayOf(3, 2, 0, 0, 2, 0),  // G9
            intArrayOf(3, 2, 0, 0, 3, 3),  // G11
        ),
        arrayOf(
            intArrayOf(4, 3, 1, 1, 1, 4),  // G#
            intArrayOf(4, 3, 1, 1, 4, 4),  // G#m
            intArrayOf(4, 3, 1, 1, 1, 2),  // G#7
            intArrayOf(4, 2, 1, 1, 4, 2),  // G#m7
            intArrayOf(4, 3, 1, 1, 1, 3),  // G#M7
            intArrayOf(4, 2, 1, 1, 1, 1),  // G#dim
            intArrayOf(4, 4, 1, 1, 4, 4),  // G#sus4
            intArrayOf(4, 3, 1, 1, 4, 1),  // G#6
            intArrayOf(4, 3, 1, 1, 3, 1),  // G#9
            intArrayOf(4, 3, 1, 1, 4, 4),  // G#11
        ),
        arrayOf(
            intArrayOf(4, 3, 1, 1, 1, 4),  // Ab (same as G#)
            intArrayOf(4, 3, 1, 1, 4, 4),  // Abm (same as G#m)
            intArrayOf(4, 3, 1, 1, 1, 2),  // Ab7 (same as G#7)
            intArrayOf(4, 2, 1, 1, 4, 2),  // Abm7 (same as G#m7)
            intArrayOf(4, 3, 1, 1, 1, 3),  // AbM7 (same as G#M7)
            intArrayOf(4, 2, 1, 1, 1, 1),  // Abdim (same as G#dim)
            intArrayOf(4, 4, 1, 1, 4, 4),  // Absus4 (same as G#sus4)
            intArrayOf(4, 3, 1, 1, 4, 1),  // Ab6 (same as G#6)
            intArrayOf(4, 3, 1, 1, 3, 1),  // Ab9 (same as G#9)
            intArrayOf(4, 3, 1, 1, 4, 4),  // Ab11 (same as G#11)
        ),
        arrayOf(
            intArrayOf(0, 0, 2, 2, 2, 0),  // A
            intArrayOf(0, 0, 2, 2, 1, 0),  // Am
            intArrayOf(0, 0, 2, 0, 2, 0),  // A7
            intArrayOf(0, 0, 2, 0, 1, 0),  // Am7
            intArrayOf(0, 0, 2, 1, 2, 0),  // AM7
            intArrayOf(0, 2, 3, 2, 3, 2),  // Adim
            intArrayOf(0, 0, 2, 2, 3, 0),  // Asus4
            intArrayOf(0, 0, 2, 2, 2, 2),  // A6
            intArrayOf(2, 0, 2, 0, 2, 0),  // A9
            intArrayOf(2, 0, 2, 0, 3, 0),  // A11
        ),
        arrayOf(
            intArrayOf(1, 1, 3, 3, 3, 1),  // A#
            intArrayOf(1, 1, 3, 3, 2, 1),  // A#m
            intArrayOf(1, 1, 3, 1, 3, 1),  // A#7
            intArrayOf(1, 1, 3, 1, 2, 1),  // A#m7
            intArrayOf(1, 1, 3, 2, 3, 1),  // A#M7
            intArrayOf(1, 3, 4, 3, 4, 3),  // A#dim
            intArrayOf(1, 1, 3, 3, 4, 1),  // A#sus4
            intArrayOf(1, 1, 3, 3, 3, 3),  // A#6
            intArrayOf(3, 1, 3, 1, 3, 1),  // A#9
            intArrayOf(3, 1, 3, 1, 4, 1),  // A#11
        ),
        arrayOf(
            intArrayOf(1, 1, 3, 3, 3, 1),  // Bb (same as A#)
            intArrayOf(1, 1, 3, 3, 2, 1),  // Bbm (same as A#m)
            intArrayOf(1, 1, 3, 1, 3, 1),  // Bb7 (same as A#7)
            intArrayOf(1, 1, 3, 1, 2, 1),  // Bbm7 (same as A#m7)
            intArrayOf(1, 1, 3, 2, 3, 1),  // BbM7 (same as A#M7)
            intArrayOf(1, 3, 4, 3, 4, 3),  // Bbdim (same as A#dim)
            intArrayOf(1, 1, 3, 3, 4, 1),  // Bbsus4 (same as A#sus4)
            intArrayOf(1, 1, 3, 3, 3, 3),  // Bb6 (same as A#6)
            intArrayOf(3, 1, 3, 1, 3, 1),  // Bb9 (same as A#9)
            intArrayOf(3, 1, 3, 1, 4, 1),  // Bb11 (same as A#11)
        ),
        arrayOf(
            intArrayOf(2, 2, 4, 4, 4, 2),  // B
            intArrayOf(2, 2, 4, 4, 3, 2),  // Bm
            intArrayOf(2, 2, 4, 2, 4, 2),  // B7
            intArrayOf(2, 0, 4, 2, 3, 2),  // Bm7
            intArrayOf(2, 2, 4, 3, 4, 2),  // BM7
            intArrayOf(2, 3, 4, 3, 4, 3),  // Bdim
            intArrayOf(2, 2, 4, 4, 5, 2),  // Bsus4
            intArrayOf(2, 2, 4, 4, 4, 4),  // B6
            intArrayOf(2, 2, 4, 4, 2, 2),  // B9
            intArrayOf(2, 2, 4, 4, 5, 4),  // B11
        )
    )

    val chordMap = mapOf(
        "C" to 0, "Cm" to 1, "C7" to 2, "Cm7" to 3, "CM7" to 4, "Cdim" to 5, "Csus4" to 6, "C6" to 7, "C9" to 8, "C11" to 9,
        "C#" to 0, "C#m" to 1, "C#7" to 2, "C#m7" to 3, "C#M7" to 4, "C#dim" to 5, "C#sus4" to 6, "C#6" to 7, "C#9" to 8, "C#11" to 9,
        "Db" to 0, "Dbm" to 1, "Db7" to 2, "Dbm7" to 3, "DbM7" to 4, "Dbdim" to 5, "Dbsus4" to 6, "Db6" to 7, "Db9" to 8, "Db11" to 9,
        "D" to 0, "Dm" to 1, "D7" to 2, "Dm7" to 3, "DM7" to 4, "Ddim" to 5, "Dsus4" to 6, "D6" to 7, "D9" to 8, "D11" to 9,
        "D#" to 0, "D#m" to 1, "D#7" to 2, "D#m7" to 3, "D#M7" to 4, "D#dim" to 5, "D#sus4" to 6, "D#6" to 7, "D#9" to 8, "D#11" to 9,
        "Eb" to 0, "Ebm" to 1, "Eb7" to 2, "Ebm7" to 3, "EbM7" to 4, "Ebdim" to 5, "Ebsus4" to 6, "Eb6" to 7, "Eb9" to 8, "Eb11" to 9,
        "E" to 0, "Em" to 1, "E7" to 2, "Em7" to 3, "EM7" to 4, "Edim" to 5, "Esus4" to 6, "E6" to 7, "E9" to 8, "E11" to 9,
        "F" to 0, "Fm" to 1, "F7" to 2, "Fm7" to 3, "FM7" to 4, "Fdim" to 5, "Fsus4" to 6, "F6" to 7, "F9" to 8, "F11" to 9,
        "F#" to 0, "F#m" to 1, "F#7" to 2, "F#m7" to 3, "F#M7" to 4, "F#dim" to 5, "F#sus4" to 6, "F#6" to 7, "F#9" to 8, "F#11" to 9,
        "Gb" to 0, "Gbm" to 1, "Gb7" to 2, "Gbm7" to 3, "GbM7" to 4, "Gbdim" to 5, "Gbsus4" to 6, "Gb6" to 7, "Gb9" to 8, "Gb11" to 9,
        "G" to 0, "Gm" to 1, "G7" to 2, "Gm7" to 3, "GM7" to 4, "Gdim" to 5, "Gsus4" to 6, "G6" to 7, "G9" to 8, "G11" to 9,
        "G#" to 0, "G#m" to 1, "G#7" to 2, "G#m7" to 3, "G#M7" to 4, "G#dim" to 5, "G#sus4" to 6, "G#6" to 7, "G#9" to 8, "G#11" to 9,
        "Ab" to 0, "Abm" to 1, "Ab7" to 2, "Abm7" to 3, "AbM7" to 4, "Abdim" to 5, "Absus4" to 6, "Ab6" to 7, "Ab9" to 8, "Ab11" to 9,
        "A" to 0, "Am" to 1, "A7" to 2, "Am7" to 3, "AM7" to 4, "Adim" to 5, "Asus4" to 6, "A6" to 7, "A9" to 8, "A11" to 9,
        "A#" to 0, "A#m" to 1, "A#7" to 2, "A#m7" to 3, "A#M7" to 4, "A#dim" to 5, "A#sus4" to 6, "A#6" to 7, "A#9" to 8, "A#11" to 9,
        "Bb" to 0, "Bbm" to 1, "Bb7" to 2, "Bbm7" to 3, "BbM7" to 4, "Bbdim" to 5, "Bbsus4" to 6, "Bb6" to 7, "Bb9" to 8, "Bb11" to 9,
        "B" to 0, "Bm" to 1, "B7" to 2, "Bm7" to 3, "BM7" to 4, "Bdim" to 5, "Bsus4" to 6, "B6" to 7, "B9" to 8, "B11" to 9
    )

    val chordIndexMap = mapOf(
        "C" to 0, "C#" to 1, "Db" to 2, "D" to 3, "D#" to 4, "Eb" to 5, "E" to 6, "F" to 7, "F#" to 8, "Gb" to 9, "G" to 10,
        "G#" to 11, "Ab" to 12, "A" to 13, "A#" to 14, "Bb" to 15, "B" to 16
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuitarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Guitar"



        soundPool = SoundPool.Builder().setMaxStreams(6).build()

        val metronomeSoundId = soundPool.load(this, R.raw.metronome, 1)
        soundMap[R.raw.metronome] = metronomeSoundId

        soundId = soundPool.load(this, R.raw.guitar_sound, 1)

        binding.stringE.setOnTouchListener { view, event -> handleTouch(view, event, 0) }
        binding.stringA.setOnTouchListener { view, event -> handleTouch(view, event, 1) }
        binding.stringD.setOnTouchListener { view, event -> handleTouch(view, event, 2) }
        binding.stringG.setOnTouchListener { view, event -> handleTouch(view, event, 3) }
        binding.stringB.setOnTouchListener { view, event -> handleTouch(view, event, 4) }
        binding.stringHighE.setOnTouchListener { view, event -> handleTouch(view, event, 5) }

        buttons = arrayOf(
            binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8
        )
        updateButton(buttons, buttonChords)


        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                if (activeButton == buttons[i]) {
                    activeButton = null
                    setGuitarNotes(intArrayOf(0, 0, 0, 0, 0, 0)) // 기본 상태로 리셋
                } else {
                    activeButton = buttons[i]

                    val chordPosition = getChordPosition(buttonChords[i])
                    if (chordPosition != null) {
                        val (index, subIndex) = chordPosition
                        setGuitarNotes(chords[index][subIndex]) // 해당 코드 설정
                    }
                }
            }
        }

        binding.editButton.setOnClickListener {
            activeButton = null  // 버튼 선택 해제
            setGuitarNotes(intArrayOf(0, 0, 0, 0, 0, 0))  // 모든 줄의 음을 초기 상태로 설정
            val intent = Intent(this, EditChordActivity::class.java)
            //intent.putExtra("chordArray", buttonChords)
            startActivityForResult(intent, 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.guitar_metronome, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            buttonChords = data?.getStringArrayExtra("chordArray") ?: buttonChords
            updateButton(buttons, buttonChords)
        }
    }

    override fun onResume() {
        super.onResume()
        updateButton(buttons, buttonChords)
    }

    // 버튼 아닌 화면 터치 시 동작
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val touchedButton = getButtonFromRawX(event.rawX)
                if (touchedButton != null && touchedButton != lastTouchedButton) {
                    lastTouchedButton = touchedButton
                    playSoundForButton(touchedButton)
                    vibrateString(touchedButton)
                }
            }
            MotionEvent.ACTION_UP -> {
                lastTouchedButton = null
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getButtonFromRawX(rawX: Float): CustomImageButton? {
        val buttons = arrayOf(
            binding.stringE, binding.stringA, binding.stringD,
            binding.stringG, binding.stringB, binding.stringHighE
        )

        buttons.forEach { button ->
            val location = IntArray(2)
            button.getLocationOnScreen(location)
            val buttonStartX = location[0]
            val buttonEndX = buttonStartX + button.width

            if (rawX >= buttonStartX && rawX <= buttonEndX) {
                return button
            }
        }

        return null
    }

    private fun playSoundForButton(button: ImageButton) {
        when (button) {
            binding.stringE -> playSound(eStringPitches[noteIndices[0]])
            binding.stringA -> playSound(aStringPitches[noteIndices[1]])
            binding.stringD -> playSound(dStringPitches[noteIndices[2]])
            binding.stringG -> playSound(gStringPitches[noteIndices[3]])
            binding.stringB -> playSound(bStringPitches[noteIndices[4]])
            binding.stringHighE -> playSound(highEStringPitches[noteIndices[5]])
        }
    }
    // 버튼 터치 시 동작
    private fun handleTouch(view: View, event: MotionEvent, stringIndex: Int): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val touchedButton = getButtonFromRawX(event.rawX)
                if (touchedButton != null && touchedButton != lastTouchedButton) {
                    lastTouchedButton = touchedButton
                    playSoundForButton(touchedButton)
                    vibrateString(touchedButton)
                }
            }
            MotionEvent.ACTION_UP -> {
                lastTouchedButton = null
                view.performClick() // performClick을 호출하여 접근성 이벤트를 처리
            }
        }
        return true
    }

    private fun vibrateString(button: ImageButton) {
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -10f, 10f, -8f, 8f, -6f, 6f, -4f, 4f, -2f, 2f, 0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(button, translationX)
        animator.duration = 500 // 진동 효과 지속 시간
        animator.interpolator = OvershootInterpolator()
        animator.start()
    }

    private fun getChordPosition(chord: String): Pair<Int, Int>? {
        val rootRegex = """[A-G][#b]?""".toRegex()
        val typeRegex = """m7|7|m|M7|dim|sus4|6|9|11""".toRegex()

        val rootMatch = rootRegex.find(chord)
        val typeMatch = typeRegex.find(chord, rootMatch?.range?.last?.plus(1) ?: 0)

        val root = rootMatch?.value ?: return null
        val type = typeMatch?.value ?: ""

        val fullChord = root + type
        Log.d("kkang", "fullChord: $fullChord")

        val index = chordIndexMap[root] ?: return null
        val subIndex = chordMap[fullChord] ?: return null

        return Pair(index, subIndex)
    }


    private fun updateButton(buttons: Array<Button>, labels: Array<String>) {
        for (i in buttons.indices) {
            buttons[i].text = labels[i]
            if (labels[i] == "-") {
                buttons[i].visibility = View.GONE
            } else {
                buttons[i].visibility = View.VISIBLE
            }
        }
    }


    private fun setGuitarNotes(notes: IntArray) {
        noteIndices = notes
    }

    private fun playSound(pitch: Float) {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, pitch)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}