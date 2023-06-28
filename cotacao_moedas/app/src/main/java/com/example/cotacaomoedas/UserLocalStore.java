package com.example.cotacaomoedas;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserLocalStore {
    public static final String fileName = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(fileName, 0);
    }

    public void printData(){
        Map<String, ?> allEntries = userLocalDatabase.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey(); // Obtém a chave (email) atual
            if (entry.getValue() instanceof String) {
                String value = (String) entry.getValue(); // Obtém o valor (email) atual
                // Exibe a chave e o valor na tela (por exemplo, usando Logcat)
                System.out.println("SharedPreferences Chave: " + key + ", Valor: " + value);

            }
        }
    }

    public void storeUserData(User user){
        SharedPreferences.Editor fileEditor = userLocalDatabase.edit();
        fileEditor.putString("email", user.email);
        fileEditor.putString("password", user.password);
        fileEditor.commit();
    }

    public User getLoggedInUser() {
        String email = userLocalDatabase.getString("email", "");
        String password = userLocalDatabase.getString("password", "");

        User storedUser = new User(email, password);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor fileEditor = userLocalDatabase.edit();
        fileEditor.putBoolean("loggedIn", loggedIn);
        fileEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("loggedIn", false) == true){
            return true;
        } else {
            return false;
        }
    }

    public boolean logIn(String targetEmail, String targetPassword){
        Map<String, ?> allEntries = userLocalDatabase.getAll();
        List<Map.Entry<String, ?>> entryList = new ArrayList<>(allEntries.entrySet());
        int listSize = entryList.size();

        for (int i = 0; i < listSize-1; i++) {
            Map.Entry<String, ?> currentEntry = entryList.get(i);
            String savedPassword = (String) currentEntry.getValue();

            Map.Entry<String, ?> nextEntry = entryList.get(i+1);
            String email = (String) nextEntry.getValue();

                System.out.println(email);
                if (email.equals(targetEmail)) {
                    // O email desejado foi encontrado no SharedPreferences
                    // Agora vamos verificar a senha correspondente
                    if (savedPassword.equals(targetPassword)) {
                        // O email e a senha correspondem aos valores armazenados
                        // Faça algo aqui, como exibir uma mensagem ou executar uma ação
                        return true;

                    }
                    break;// Saia do loop se você encontrou o email correspondente
                }
                i++;

        }

        return false;
    }

    public void clearUserData(){
        SharedPreferences.Editor fileEditor = userLocalDatabase.edit();
        fileEditor.clear();
        fileEditor.commit();
        fileEditor.apply(); // Aplica as alterações
    }

}
