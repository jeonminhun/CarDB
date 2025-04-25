package com.<your>.<application>
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.bumptech.glide.Glide
class Screen8 : AppCompatActivity() {
	private var editTextValue1: String = ""
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_screen8)
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/x0sg2g84_expires_30_days.png").into(findViewById(R.id.ruscdc580d9r))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/3f3mcu4u_expires_30_days.png").into(findViewById(R.id.r40orswerz52))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/32lpx4w4_expires_30_days.png").into(findViewById(R.id.r3n68zdxrvxy))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/3kad3u5l_expires_30_days.png").into(findViewById(R.id.r85ahlc9kz3x))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/zkgvp2iu_expires_30_days.png").into(findViewById(R.id.rp2zvpnv9soc))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/b73ji7yn_expires_30_days.png").into(findViewById(R.id.rpu3bvvx9pth))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/9txrab5m_expires_30_days.png").into(findViewById(R.id.r9b3f2xpypd))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/m1oqgtx2_expires_30_days.png").into(findViewById(R.id.rg3zo7pu2pv8))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/joudgoro_expires_30_days.png").into(findViewById(R.id.rihdt4qatrzl))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/e7ftkvqv_expires_30_days.png").into(findViewById(R.id.r6fmex12jlwf))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/ni0ug085_expires_30_days.png").into(findViewById(R.id.rdsahshvopau))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/ygsfshfl_expires_30_days.png").into(findViewById(R.id.r73ut6vfm78x))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/31p2qy18_expires_30_days.png").into(findViewById(R.id.rarkai1ijzvd))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/3mv01lv0_expires_30_days.png").into(findViewById(R.id.r99xfyapokrr))
		Glide.with(this).load("https://storage.googleapis.com/tagjs-prod.appspot.com/v1/rMe8LyGkUp/wcsibule_expires_30_days.png").into(findViewById(R.id.ragtmo2cycmn))
		val editText1: EditText = findViewById(R.id.rloa39mzwog7)
		editText1.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
				// before Text Changed
			}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				editTextValue1 = s.toString()  // on Text Changed
			}
			override fun afterTextChanged(s: Editable?) {
				// after Text Changed
			}
		})
		val button1: View = findViewById(R.id.r8qtl5bcqe28)
		button1.setOnClickListener {
			println("Pressed")
		}
		val button2: View = findViewById(R.id.rc3buuy5is78)
		button2.setOnClickListener {
			println("Pressed")
		}
		val button3: View = findViewById(R.id.ruw5vpopy8j)
		button3.setOnClickListener {
			println("Pressed")
		}
		val button4: View = findViewById(R.id.r70izwo4cda)
		button4.setOnClickListener {
			println("Pressed")
		}
		val button5: View = findViewById(R.id.rsh6ydhrzud)
		button5.setOnClickListener {
			println("Pressed")
		}
	}
}