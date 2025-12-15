package com.example.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class BoaViagemActivity extends Activity {

    private SharedPreferences preferencias;

    private EditText usuario;
    private EditText senha;
    private CheckBox manterConectado;

    private GoogleSignInClient googleClient;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usuario = findViewById(R.id.usuario);
        senha = findViewById(R.id.senha);
        manterConectado = findViewById(R.id.manterConectado);

        preferencias = getSharedPreferences(Constantes.PREFERENCIAS, MODE_PRIVATE);

        // Configuração Google Login
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        googleClient = GoogleSignIn.getClient(this, gso);

        // 🔁 LOGIN AUTOMÁTICO
        if (preferencias.getBoolean(Constantes.MANTER_CONECTADO, false)) {
            GoogleSignInAccount conta = GoogleSignIn.getLastSignedInAccount(this);
            if (conta != null) {
                iniciarDashboard();
                return;
            }
        }

        if (getActionBar() != null) {
            getActionBar().setTitle("Login");
        }
    }

    private void iniciarDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    public void entrarOnclick(View v) {
        Intent intent = googleClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount conta = task.getResult(Exception.class);

                if (conta != null) {
                    salvarLogin(conta.getEmail());
                    iniciarDashboard();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void salvarLogin(String email) {
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString(Constantes.NOME_CONTA, email);
        editor.putBoolean(Constantes.MANTER_CONECTADO, manterConectado.isChecked());
        editor.apply();
    }

    // ✅ CONSTANTES CENTRALIZADAS
    public static class Constantes {
        public static final String PREFERENCIAS = "preferencias_globais";
        public static final String MANTER_CONECTADO = "manter_conectado";
        public static final String NOME_CONTA = "nome_conta";
    }
}
