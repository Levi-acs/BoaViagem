package com.example.boaviagem;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.os.Bundle;

public class AutenticacaoHelper {

    private AccountManager accountManager;

    public AutenticacaoHelper(Context context) {
        this.accountManager = AccountManager.get(context);
    }

    // Busca uma conta Google pelo nome
    public Account getContaGoogle(String nomeConta) {
        Account[] contas = accountManager.getAccountsByType("com.google");

        for (Account c : contas) {
            if (c.name.equalsIgnoreCase(nomeConta)) {
                return c;
            }
        }

        return null;
    }

    // Confirma credenciais (não solicita senha!)
    public void confirmarCredenciais(Account conta, AccountManagerCallback<Bundle> callback) {
        accountManager.confirmCredentials(conta, null, null, callback, null);
    }
}
