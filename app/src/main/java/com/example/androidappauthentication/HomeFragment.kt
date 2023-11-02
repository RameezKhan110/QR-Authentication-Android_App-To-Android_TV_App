package com.example.androidappauthentication

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.androidappauthentication.databinding.FragmentHomeBinding
import com.example.androidappauthentication.utils.Extensions
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_PERMISSION_CODE = 101
    private val fcmEndpoint = "https://fcm.googleapis.com/fcm/send"
    //sender server key
    private val serverKey = "AAAAjUykSjM:APA91bHKrIQLfCTfMKRNm9A6ciYDt2Fal-NKFPGcUyB6qF5EYCe32NeAb8GN5v2DKNZFfDMcsw2wUm624Tw6HyCg7wnYszRhTVMorS72yBL-LBL4xVVyu6JxhF3N61SNBVjPCax-5lsb"
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