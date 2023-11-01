package com.example.androidappauthentication

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.androidappauthentication.databinding.FragmentHomeBinding
import com.example.androidappauthentication.room.Auth
import com.example.androidappauthentication.utils.Extensions
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_PERMISSION_CODE = 101
    private val fcmEndpoint = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "AAAAa96YgPk:APA91bG7tR_3V5l1qljRXBvqWF9bbkSHlOdWnMF9xEOu_qKj7U4LyIHuYRtyUy6Ap5nUGrCoVm5d_nk3GSw9NR4yGH0Cbfb5gUIIstMsQmcNyUXV_Z4gEGJUZnrS3ZzrnlgW7gkI7lcG"
    var recipientToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeScanner = CodeScanner(requireActivity(), binding.qrScanner)
        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                Log.d("TAG", "data from barcode: " + it.text)
                recipientToken = it.text
                val userEmail = Extensions.getUser()?.email
                val userName = Extensions.getUser()?.name

                val message = """
{
  "to": "$recipientToken",
  "data": {
    "email": "$userEmail",
    "name": "$userName"
  }
}
"""
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = message.toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(fcmEndpoint)
                    .header("Authorization", "key=$serverKey")
                    .post(requestBody)
                    .build()

                val client = OkHttpClient()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("TAG", "response in failure : " + e.message)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            Log.d("TAG", "response of req: " + response.networkResponse)
                            if(response.body != null) {
                                Log.d("TAG", "response body is not null")
                            }
                        }
                    }
                })

            }
        }


        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission()
        } else {
            codeScanner.startPreview()
        }
        binding.btnOpenScanner.setOnClickListener {
            binding.qrScanner.visibility = View.VISIBLE
        }

        binding.qrScanner.setOnClickListener {
            codeScanner.startPreview()
        }
        binding.logout.setOnClickListener {
            Extensions.clearUser()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()

    }

    override fun onDestroy() {
        super.onDestroy()
        codeScanner.releaseResources()

    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                codeScanner.startPreview()
            } else {
                requestCameraPermission()
            }
        }
    }


}