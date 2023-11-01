package com.example.androidappauthentication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androidappauthentication.databinding.FragmentLoginBinding
import com.example.androidappauthentication.room.Auth
import com.example.androidappauthentication.utils.Extensions
import com.example.androidappauthentication.utils.Extensions.Companion.getUser
import com.example.androidappauthentication.viewmodel.AuthViewModel
import com.google.gson.Gson
import java.security.cert.Extension

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(Extensions.getUser() != null) {
            Log.d("TAG", "saved Data:" + getUser())
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            val userEmail = binding.etEmail.text.toString().trim()
            val userPassword = binding.etPassword.text.toString().trim()

            if(userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                authViewModel.login(userEmail, userPassword).observe(viewLifecycleOwner) {
                    if(it != null){
                        Toast.makeText(requireContext(), "Logging In", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        Log.d("TAG", "calling from login: $it")
                    } else {
                        Toast.makeText(requireContext(), "Email or password is incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



}