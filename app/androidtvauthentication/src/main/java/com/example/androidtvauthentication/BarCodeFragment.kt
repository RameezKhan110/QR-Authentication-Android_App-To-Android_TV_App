package com.example.androidtvauthentication

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
            val name = extras?.getString("name")
            if (email != null && name != null) {
                val bundle = Bundle()
                bundle.putString("email", email)
                bundle.putString("name", name)
                findNavController().navigate(com.example.androidtvauthentication.R.id.action_barCodeFragment_to_homeFragment, bundle)
                Log.d("TAG", "calling from barcode email and name :$email $name")
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBarCodeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}