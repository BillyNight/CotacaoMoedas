package com.example.cotacaomoedas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.app.AlertDialog;

import com.example.cotacaomoedas.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    UserLocalStore userLocalStore;

    EditText etEmail, etPassword;

    AlertDialog.Builder dialog;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        userLocalStore = new UserLocalStore(getActivity());
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = (EditText) binding.editTextTextEmailAddress2;
        etPassword = (EditText) binding.editTextTextPassword2;

        dialog = new AlertDialog.Builder(getActivity());

        binding.backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_login);
            }
        });

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if(userLocalStore.logIn(email, password) == false){
                    User registeredData  = new User(email, password);
                    userLocalStore.storeUserData(registeredData);
                    dialog.setTitle("Usuário criado");
                    dialog.setMessage("Efetue login para entrar");
                    dialog.setNeutralButton("OK", null);
                    dialog.show();
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_login);
                }else {
                    dialog.setTitle("Erro de cadastro");
                    dialog.setMessage("Email já cadastrado");
                    dialog.setNeutralButton("OK", null);
                    dialog.show();
                }

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}