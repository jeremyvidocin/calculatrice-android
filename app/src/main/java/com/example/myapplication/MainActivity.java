package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Calculatrice simple : +, -, x, /, %, +/-
public class MainActivity extends AppCompatActivity {

    // état courant
    private String  saisie        = "0";
    private double  memoire       = 0;
    private String  operateur     = "";
    private boolean nouveauNombre = true;
    private boolean enErreur      = false;

    private TextView textOperation;
    private TextView textResultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOperation = findViewById(R.id.textOperation);
        textResultat  = findViewById(R.id.textResultat);

        // chiffres
        findViewById(R.id.btn0).setOnClickListener(v -> ajouterChiffre("0"));
        findViewById(R.id.btn1).setOnClickListener(v -> ajouterChiffre("1"));
        findViewById(R.id.btn2).setOnClickListener(v -> ajouterChiffre("2"));
        findViewById(R.id.btn3).setOnClickListener(v -> ajouterChiffre("3"));
        findViewById(R.id.btn4).setOnClickListener(v -> ajouterChiffre("4"));
        findViewById(R.id.btn5).setOnClickListener(v -> ajouterChiffre("5"));
        findViewById(R.id.btn6).setOnClickListener(v -> ajouterChiffre("6"));
        findViewById(R.id.btn7).setOnClickListener(v -> ajouterChiffre("7"));
        findViewById(R.id.btn8).setOnClickListener(v -> ajouterChiffre("8"));
        findViewById(R.id.btn9).setOnClickListener(v -> ajouterChiffre("9"));

        findViewById(R.id.btnVirgule).setOnClickListener(v -> ajouterVirgule());

        // operateurs
        findViewById(R.id.btnAdditionner).setOnClickListener(v -> choisirOperateur("+"));
        findViewById(R.id.btnSoustraire ).setOnClickListener(v -> choisirOperateur("−"));
        findViewById(R.id.btnMultiplier ).setOnClickListener(v -> choisirOperateur("×"));
        findViewById(R.id.btnDiviser    ).setOnClickListener(v -> choisirOperateur("÷"));

        findViewById(R.id.btnEgal).setOnClickListener(v -> calculer());
        findViewById(R.id.btnAC  ).setOnClickListener(v -> reinitialiser());

        findViewById(R.id.btnPlusMoins).setOnClickListener(v -> changerSigne());
        findViewById(R.id.btnPourcent ).setOnClickListener(v -> appliquerPourcent());

        afficher();
    }

    private void ajouterChiffre(String c) {
        if (enErreur) reinitialiser();

        if (nouveauNombre) {
            saisie = c;
            nouveauNombre = false;
        } else if (saisie.equals("0")) {
            saisie = c;
        } else {
            saisie = saisie + c;
        }
        afficher();
    }

    private void ajouterVirgule() {
        if (enErreur) reinitialiser();

        if (nouveauNombre) {
            saisie = "0,";
            nouveauNombre = false;
        } else if (!saisie.contains(",")) {
            saisie = saisie + ",";
        }
        afficher();
    }

    private void choisirOperateur(String op) {
        if (enErreur) return;

        // si un calcul attend déjà un résultat, on l'enchaîne
        if (!operateur.isEmpty() && !nouveauNombre) {
            calculer();
            if (enErreur) return;
        } else {
            memoire = lireSaisie();
        }

        operateur = op;
        nouveauNombre = true;
        textOperation.setText(formate(memoire) + " " + operateur);
    }

    private void calculer() {
        if (operateur.isEmpty() || enErreur) return;

        double a = memoire;
        double b = lireSaisie();
        double resultat;

        switch (operateur) {
            case "+": resultat = a + b; break;
            case "−": resultat = a - b; break;
            case "×": resultat = a * b; break;
            case "÷":
                if (b == 0) { afficherErreur(); return; }
                resultat = a / b;
                break;
            default: return;
        }

        textOperation.setText(formate(a) + " " + operateur + " " + formate(b) + " =");
        memoire = resultat;
        saisie = formate(resultat);
        operateur = "";
        nouveauNombre = true;
        afficher();
    }

    private void reinitialiser() {
        saisie = "0";
        memoire = 0;
        operateur = "";
        nouveauNombre = true;
        enErreur = false;
        textOperation.setText("");
        afficher();
    }

    private void changerSigne() {
        if (enErreur || saisie.equals("0")) return;
        if (saisie.startsWith("-")) saisie = saisie.substring(1);
        else                         saisie = "-" + saisie;
        afficher();
    }

    private void appliquerPourcent() {
        if (enErreur) return;
        saisie = formate(lireSaisie() / 100);
        afficher();
    }

    // saisie (virgule) -> double (point)
    private double lireSaisie() {
        return Double.parseDouble(saisie.replace(',', '.'));
    }

    // formate un double en texte : 8 au lieu de 8.0, virgule à la française
    private String formate(double d) {
        if (d == (long) d) return String.valueOf((long) d);
        return String.valueOf(d).replace('.', ',');
    }

    private void afficher() {
        textResultat.setText(saisie);
    }

    private void afficherErreur() {
        textResultat.setText("Erreur");
        textOperation.setText("");
        enErreur = true;
    }
}
