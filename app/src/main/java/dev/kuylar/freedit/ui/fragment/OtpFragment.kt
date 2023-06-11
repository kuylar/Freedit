package dev.kuylar.freedit.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.kuylar.freedit.R
import dev.kuylar.freedit.api.RedditApi
import dev.kuylar.freedit.databinding.FragmentOtpBinding
import kotlin.concurrent.thread

class OtpFragment(private val username: String, private val password: String) :
	BottomSheetDialogFragment() {
	private lateinit var binding: FragmentOtpBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentOtpBinding.inflate(inflater)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.buttonOtpSubmit.setOnClickListener {
			doLogin(
				username,
				password,
				binding.inputOtp.editText!!.editableText.toString().trim()
			)
		}

		binding.inputOtp.editText!!.addTextChangedListener {
			binding.inputOtp.error = null
		}
	}

	private fun doLogin(username: String, password: String, otp: String) {
		binding.buttonOtpSubmit.isEnabled = false
		thread {
			val response = RedditApi.Static.loginWithOtp(username, password, otp)
			activity?.runOnUiThread {
				if (!response.json.errors.isEmpty) {
					when (response.json.errors.get(0).asJsonArray.get(0).asString) {
						"INCORRECT_USERNAME_PASSWORD" -> {
							dismissNow()
							Toast.makeText(
								activity,
								R.string.error_incorrect_credentials,
								Toast.LENGTH_LONG
							).show()
						}

						"WRONG_OTP" -> {
							binding.inputOtp.editText!!.editableText.clear()
							binding.inputOtp.error = getString(R.string.error_otp_invalid)
						}
					}
				} else if (response.json.data == null) {
					Toast.makeText(activity, "Received null response", Toast.LENGTH_LONG).show()
				} else if (response.json.data.details == "TWO_FA_REQUIRED") {
					Toast.makeText(activity, "2FA required", Toast.LENGTH_LONG).show()
				} else if (!response.json.data.details.isNullOrEmpty()) {
					Toast.makeText(activity, response.json.data.details, Toast.LENGTH_LONG).show()
				} else {
					val sp = activity?.getSharedPreferences("main", AppCompatActivity.MODE_PRIVATE)
					sp?.edit {
						putString("cookie", response.json.data.cookie)
						putString("modhash", response.json.data.modhash)
					}
					Toast.makeText(activity, "Login success", Toast.LENGTH_LONG).show()
					activity?.finish()
				}
				binding.buttonOtpSubmit.isEnabled = true
			}
		}
	}
}