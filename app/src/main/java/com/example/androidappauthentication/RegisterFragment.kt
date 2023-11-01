package com.example.androidappauthentication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson

import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.androidappauthentication.databinding.FragmentRegisterBinding
import com.example.androidappauthentication.room.Auth
import com.example.androidappauthentication.utils.Extensions
import com.example.androidappauthentication.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private var userEmail: String = ""
    private var userName: String = ""
    private var userPassword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            userName = binding.etName.text.toString().trim()
            userEmail = binding.etEmail.text.toString().trim()
            userPassword = binding.etPassword.text.toString().trim()
            val userChangePass = binding.etChangePassword.text.toString().trim()

            if (userName.isNotEmpty() && userEmail.isNotEmpty()
                && userPassword.isNotEmpty() && userChangePass.isNotEmpty()
                && userName.isNotEmpty()
            ) {
                if (userPassword == userChangePass) {
                    val auth = Auth(0, userName, userEmail, userPassword)
                    authViewModel.register(auth)
//                    saveUser()
                    authViewModel.login(auth.email?:"", auth.password?:"").observe(viewLifecycleOwner) {
                            Log.d("TAG", "it: $it")
                            Extensions.saveUser(it)
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                    }
                } else {
                    binding.tvError.text = "Password should be same"
                    binding.tvError.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireContext(), "Fields can't be empty", Toast.LENGTH_SHORT).show()
            }

        }

    }



}