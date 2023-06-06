package dev.kuylar.freedit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import dev.kuylar.freedit.api.RedditApi.*
import dev.kuylar.freedit.databinding.ActivityLoginBinding
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
	private lateinit var binding: ActivityLoginBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		WindowCompat.setDecorFitsSystemWindows(window, false)
		super.onCreate(savedInstanceState)

		binding = ActivityLoginBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.buttonLoginSubmit.setOnClickListener {
			doLogin(
				binding.inputLoginUsername.editText!!.editableText.toString().trim(),
				binding.inputLoginPassword.editText!!.editableText.toString().trim()
			)
		}
	}

	private fun doLogin(username: String, password: String) {
		binding.buttonLoginSubmit.isEnabled = false
		thread {
			val response = Static.login(username, password)
			runOnUiThread {
				if (!response.json.errors.isEmpty) {
					when (response.json.errors.get(0).asJsonArray.get(0).asString) {
						"INCORRECT_USERNAME_PASSWORD" -> {
							binding.inputLoginPassword.editText!!.editableText.clear()
							binding.inputLoginPassword.error = getString(R.string.error_incorrect_credentials)
						}

						"WRONG_OTP" -> Toast.makeText(this, R.string.error_otp_invalid, Toast.LENGTH_LONG)
							.show()
					}
				} else if (response.json.data == null) {
					Toast.makeText(this, "Received null response", Toast.LENGTH_LONG).show()
				} else if (response.json.data.details == "TWO_FA_REQUIRED") {
					OtpFragment(username, password).show(supportFragmentManager, null)
				} else {
					val sp = getSharedPreferences("main", MODE_PRIVATE)
					sp.edit {
						putString("cookie", response.json.data.cookie)
						putString("modhash", response.json.data.modhash)
					}
					Toast.makeText(this, "Login success", Toast.LENGTH_LONG).show()
					finish()
				}
				binding.buttonLoginSubmit.isEnabled = true
			}
		}
	}
}