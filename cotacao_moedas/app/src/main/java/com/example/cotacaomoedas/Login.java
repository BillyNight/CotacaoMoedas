package com.example.cotacaomoedas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;

import androidx.navigation.fragment.NavHostFragment;



import com.example.cotacaomoedas.databinding.FragmentLoginBinding;
import com.example.cotacaomoedas.databinding.FragmentSecondBinding;



public class Login extends Fragment {



    UserLocalStore userLocalStore;
    private FragmentLoginBinding binding;

    EditText etEmail, etPassword;

    AlertDialog.Builder dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userLocalStore = new UserLocalStore(getActivity());

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEmail = (EditText) binding.editTextTextEmailAddress;
        etPassword = (EditText) binding.editTextTextPassword;
        dialog = new AlertDialog.Builder(getActivity());
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User user = new User(null, null);
                //userLocalStore.storeUserData(user);
                //userLocalStore.setUserLoggedIn(true);
                String email, password;
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                System.out.println(userLocalStore.logIn(email, password));
                if(userLocalStore.logIn(email, password)){
                    NavHostFragment.findNavController(Login.this)
                            .navigate(R.id.action_login_to_FirstFragment);
                }else {
                    dialog.setTitle("Erro no login");
                    dialog.setMessage("Email ou senha inválidos");
                    dialog.setNeutralButton("OK", null);
                    dialog.show();
                }

            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Login.this)
                        .navigate(R.id.action_login_to_SecondFragment2);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}