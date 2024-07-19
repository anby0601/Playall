package com.example.playall

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.provider.MediaStore
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.playall.databinding.ActivityDrumBinding
import com.google.android.material.tabs.TabLayoutMediator


class DrumActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var snareSound: Int = 0
    private var tomSound1: Int = 0
    private var tomSound2: Int = 0
    private var tomSound3: Int = 0
    private var kickSound: Int = 0
    private var floorSound: Int = 0
    private var crashSound: Int = 0
    private var rideSound: Int = 0
    private var splashSound: Int = 0
    private var openSound: Int = 0
    private var closeSound: Int = 0
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityDrumBinding

    private val soundMap = mutableMapOf<Int, Int>()

    private var metronomeHandler: Handler? = null
    private var metronomeRunnable: Runnable? = null

    private val soundHandler = Handler(Looper.getMainLooper())
    private val soundDelayMillis = 100 // 사운드 간의 딜레이 (밀리초)
    private var isPlaying = false // 사운드 재생 중인지 여부를 나타내는 플래그

    // 사운드 재생 메서드
    private fun playSound(soundId: Int) {
        if (!isPlaying) {
            isPlaying = true
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            soundHandler.postDelayed({ isPlaying = false }, soundDelayMillis.toLong())
        }
    }

    // 진동 효과를 발생시키는 함수
    private fun vibrate(durationMillis: Long) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(durationMillis)
    }


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val selectedImageUri = result.data?.data
            selectedImageUri?.let {
                setLayoutBackground(it)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDrumBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Drum"
        setSupportActionBar(binding.toolbar)
        soundPool = SoundPool.Builder().setMaxStreams(10).build()

        val metronomeSoundId = soundPool.load(this, R.raw.metronome, 1)
        soundMap[R.raw.metronome] = metronomeSoundId

        viewPager = binding.viewpager!!
        setupViewPager()
        val tabTitles = listOf("Right-handed", "Left-handed")
        binding.tabs?.let {
            TabLayoutMediator(it, viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
        constraintLayout = findViewById(R.id.main)

        binding.backBtn?.setOnClickListener {
            finish()
        }

        binding.backchange?.setOnClickListener {
            openImageChooser()
        }

        snareSound = soundPool.load(this, R.raw.snaresound, 1)
        tomSound1 = soundPool.load(this, R.raw.tomsound1, 1)
        tomSound2 = soundPool.load(this, R.raw.tomsound2, 1)
        tomSound3 = soundPool.load(this, R.raw.tomsound3, 1)
        kickSound = soundPool.load(this, R.raw.kicksound, 1)
        floorSound = soundPool.load(this, R.raw.floorsound, 1)
        crashSound = soundPool.load(this, R.raw.crashsound, 1)
        rideSound = soundPool.load(this, R.raw.ridesound, 1)
        splashSound = soundPool.load(this, R.raw.splashsound, 1)
        openSound = soundPool.load(this, R.raw.opensound, 1)
        closeSound = soundPool.load(this, R.raw.closesound, 1)

        binding.snareDrum.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    }.start()
                    playSound(snareSound) // 기존 사운드 재생 코드
                }
            }
            true
        }

        binding.tomDrum1.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    }.start()
                    playSound(tomSound1)
                }
            }
            true
        }

        binding.tomDrum2.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    }.start()
                    playSound(tomSound2)
                }
            }
            true
        }

        binding.tomDrum3.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    }.start()
                    playSound(tomSound3)
                }
            }
            true
        }

        binding.kickDrum1.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    }.start()
                    playSound(kickSound)
                }
            }
            true
        }

        binding.kickDrum2.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    }.start()
                    playSound(kickSound)
                }
            }
            true
        }

        binding.floorDrum.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    }.start()
                    playSound(floorSound)
                }
            }
            true
        }

        binding.crashDrum1.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val pivotX = view.width / 2
                    val pivotY = view.height / 2
                    view.pivotX = pivotX.toFloat()
                    view.pivotY = pivotY.toFloat()

                    val animatorX = ObjectAnimator.ofFloat(view, "rotationX", -10f, 10f)
                    val animatorY = ObjectAnimator.ofFloat(view, "rotationY", -10f, 10f)

                    animatorX.duration = 200
                    animatorY.duration = 200

                    animatorX.interpolator = LinearInterpolator()
                    animatorY.interpolator = LinearInterpolator()

                    animatorX.repeatCount = 5
                    animatorY.repeatCount = 5

                    animatorX.repeatMode = ObjectAnimator.REVERSE
                    animatorY.repeatMode = ObjectAnimator.REVERSE

                    animatorX.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationX = 0f
                        }
                    })

                    animatorY.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationY = 0f
                        }
                    })

                    animatorX.start()
                    animatorY.start()

                    playSound(crashSound)
                    vibrate(500) // 50ms 진동 추가
                }
            }
            true
        }

        binding.crashDrum2.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val pivotX = view.width / 2
                    val pivotY = view.height / 2
                    view.pivotX = pivotX.toFloat()
                    view.pivotY = pivotY.toFloat()

                    val animatorX = ObjectAnimator.ofFloat(view, "rotationX", -10f, 10f)
                    val animatorY = ObjectAnimator.ofFloat(view, "rotationY", -10f, 10f)

                    animatorX.duration = 200
                    animatorY.duration = 200

                    animatorX.interpolator = LinearInterpolator()
                    animatorY.interpolator = LinearInterpolator()

                    animatorX.repeatCount = 5
                    animatorY.repeatCount = 5

                    animatorX.repeatMode = ObjectAnimator.REVERSE
                    animatorY.repeatMode = ObjectAnimator.REVERSE

                    animatorX.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationX = 0f
                        }
                    })

                    animatorY.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationY = 0f
                        }
                    })

                    animatorX.start()
                    animatorY.start()

                    playSound(crashSound)
                    vibrate(500) // 50ms 진동 추가
                }
            }
            true
        }

        binding.rideDrum.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val pivotX = view.width / 2
                    val pivotY = view.height / 2
                    view.pivotX = pivotX.toFloat()
                    view.pivotY = pivotY.toFloat()

                    val animatorX = ObjectAnimator.ofFloat(view, "rotationX", -10f, 10f)
                    val animatorY = ObjectAnimator.ofFloat(view, "rotationY", -10f, 10f)

                    animatorX.duration = 200
                    animatorY.duration = 200

                    animatorX.interpolator = LinearInterpolator()
                    animatorY.interpolator = LinearInterpolator()

                    animatorX.repeatCount = 5
                    animatorY.repeatCount = 5

                    animatorX.repeatMode = ObjectAnimator.REVERSE
                    animatorY.repeatMode = ObjectAnimator.REVERSE

                    animatorX.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationX = 0f
                        }
                    })

                    animatorY.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationY = 0f
                        }
                    })

                    animatorX.start()
                    animatorY.start()

                    playSound(rideSound)
                }
            }
            true
        }

        binding.splashDrum.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val pivotX = view.width / 2
                    val pivotY = view.height / 2
                    view.pivotX = pivotX.toFloat()
                    view.pivotY = pivotY.toFloat()

                    val animatorX = ObjectAnimator.ofFloat(view, "rotationX", -10f, 10f)
                    val animatorY = ObjectAnimator.ofFloat(view, "rotationY", -10f, 10f)

                    animatorX.duration = 200
                    animatorY.duration = 200

                    animatorX.interpolator = LinearInterpolator()
                    animatorY.interpolator = LinearInterpolator()

                    animatorX.repeatCount = 5
                    animatorY.repeatCount = 5

                    animatorX.repeatMode = ObjectAnimator.REVERSE
                    animatorY.repeatMode = ObjectAnimator.REVERSE

                    animatorX.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationX = 0f
                        }
                    })

                    animatorY.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationY = 0f
                        }
                    })

                    animatorX.start()
                    animatorY.start()

                    playSound(splashSound)
                    vibrate(500) // 50ms 진동 추가
                }
            }
            true
        }

        binding.openDrum.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val pivotX = view.width / 2
                    val pivotY = view.height / 2
                    view.pivotX = pivotX.toFloat()
                    view.pivotY = pivotY.toFloat()

                    val animatorX = ObjectAnimator.ofFloat(view, "rotationX", -10f, 10f)
                    val animatorY = ObjectAnimator.ofFloat(view, "rotationY", -10f, 10f)

                    animatorX.duration = 200
                    animatorY.duration = 200

                    animatorX.interpolator = LinearInterpolator()
                    animatorY.interpolator = LinearInterpolator()

                    animatorX.repeatCount = 5
                    animatorY.repeatCount = 5

                    animatorX.repeatMode = ObjectAnimator.REVERSE
                    animatorY.repeatMode = ObjectAnimator.REVERSE

                    animatorX.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationX = 0f
                        }
                    })

                    animatorY.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationY = 0f
                        }
                    })

                    animatorX.start()
                    animatorY.start()

                    playSound(openSound)
                }
            }
            true
        }

        binding.closeDrum.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val pivotX = view.width / 2
                    val pivotY = view.height / 2
                    view.pivotX = pivotX.toFloat()
                    view.pivotY = pivotY.toFloat()

                    val animatorX = ObjectAnimator.ofFloat(view, "rotationX", -10f, 10f)
                    val animatorY = ObjectAnimator.ofFloat(view, "rotationY", -10f, 10f)

                    animatorX.duration = 400
                    animatorY.duration = 400

                    animatorX.interpolator = LinearInterpolator()
                    animatorY.interpolator = LinearInterpolator()

                    animatorX.repeatCount = 3
                    animatorY.repeatCount = 3

                    animatorX.repeatMode = ObjectAnimator.REVERSE
                    animatorY.repeatMode = ObjectAnimator.REVERSE

                    animatorX.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationX = 0f
                        }
                    })

                    animatorY.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            view.rotationY = 0f
                        }
                    })

                    animatorX.start()
                    animatorY.start()

                    playSound(closeSound)
                }
            }
            true
        }


        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){

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

    private fun setupViewPager() {
        viewPager.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = layoutInflater.inflate(R.layout.activity_drum, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun getItemCount(): Int = 2

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                // Not used, but required to override
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonPositions(position)
            }
        })
    }

    private fun updateButtonPositions(position: Int) {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.main)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        when (position) {
            0 -> {
                // Right-handed 페이지 설정
                constraintSet.clear(R.id.floor_drum, ConstraintSet.START)
                constraintSet.clear(R.id.floor_drum, ConstraintSet.END)
                constraintSet.connect(R.id.floor_drum, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)
                constraintSet.clear(R.id.open_drum, ConstraintSet.START)
                constraintSet.clear(R.id.open_drum, ConstraintSet.END)
                constraintSet.connect(R.id.open_drum, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 300)
                constraintSet.clear(R.id.close_drum, ConstraintSet.START)
                constraintSet.clear(R.id.close_drum, ConstraintSet.END)
                constraintSet.connect(R.id.close_drum, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 300)

            }
            1 -> {
                // Left-handed 페이지 설정
                constraintSet.clear(R.id.floor_drum, ConstraintSet.START)
                constraintSet.clear(R.id.floor_drum, ConstraintSet.END)
                constraintSet.connect(R.id.floor_drum, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 300)
                constraintSet.clear(R.id.open_drum, ConstraintSet.START)
                constraintSet.clear(R.id.open_drum, ConstraintSet.END)
                constraintSet.clear(R.id.close_drum, ConstraintSet.START)
                constraintSet.clear(R.id.close_drum, ConstraintSet.END)
                constraintSet.connect(R.id.open_drum, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 100)
                constraintSet.connect(R.id.close_drum, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 100)
            }
        }

        constraintSet.applyTo(constraintLayout)
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun setLayoutBackground(imageUri: Uri) {
        val inputStream = contentResolver.openInputStream(imageUri)
        val drawable = Drawable.createFromStream(inputStream, imageUri.toString())
        constraintLayout.background = drawable
    }





}
