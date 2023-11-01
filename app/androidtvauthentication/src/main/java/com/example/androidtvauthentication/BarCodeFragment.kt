package com.example.androidtvauthentication

import android.R
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.example.androidtvauthentication.databinding.FragmentBarCodeBinding
import com.example.androidtvauthentication.viewmodel.AuthViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class BarCodeFragment : Fragment() {

    private lateinit var binding: FragmentBarCodeBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private var token: String = ""
    
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("SuspiciousIndentation")
        override fun onReceive(context: Context?, intent: Intent?) {
            val extras = intent?.extras
            val email = extras?.getString("email")
            if(email != null) {
                findNavController().navigate(com.example.androidtvauthentication.R.id.action_barCodeFragment_to_homeFragment)
                Log.d("TAG", "calling from barcode email:$email")

            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBarCodeBinding.inflate(layoutInflater, container, false)

        authViewModel.getToken()
        authViewModel.getToken.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Log.d("TAG", "token: $it")
            }
            token = it
            val mWriter = MultiFormatWriter()
            try {
                val mMatrix =
                    mWriter.encode(it, BarcodeFormat.QR_CODE, 400, 400)
                val mEncoder = BarcodeEncoder()
                val mBitmap =
                    mEncoder.createBitmap(mMatrix)
                binding.ivQrCode.setImageBitmap(mBitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
//            authViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
//                if(userDetails != null) {
//                    Toast.makeText(requireContext(), "Logging In", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(com.example.androidtvauthentication.R.id.action_barCodeFragment_to_homeFragment)
//                }
//        }


        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val filter = IntentFilter("com.example.androidtvauthentication.onMessageReceived")
        context.registerReceiver(receiver, filter)
    }

    override fun onDetach() {
        super.onDetach()
        context?.unregisterReceiver(receiver)
    }
//    override fun onResume() {
//        super.onResume()
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("com.example.androidtvauthentication.onMessageReceived")
//        val receiver = MyBroadcastReceiver()
//        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, intentFilter)
//
//    }

//    inner class MyBroadcastReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            val extras = intent?.extras
//            val email = extras?.getString("email")
//            if (email != null) {
//                getDetails(email)
//            }
//        }
//    }

//    fun getDetails(email: String) {
//        Log.d("TAG", "barcode frag: $email")
//    }
}