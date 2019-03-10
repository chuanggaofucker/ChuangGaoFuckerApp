package net.crigh.cgsport.cgactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import net.crigh.api.service.NetworkService;
import net.crigh.cgsport.R;

import org.json.JSONObject;


public class ChingoSportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgactivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::showLogin);
    }

    public void showLogin(View view) {
        LayoutInflater inflater = this.getLayoutInflater();
        View loginView = inflater.inflate(R.layout.activity_login, null);
        View progressView = loginView.findViewById(R.id.login_progress);
        View loginFormView = loginView.findViewById(R.id.login_form);
        AutoCompleteTextView usernameView = loginView.findViewById(R.id.email);
        EditText passwordView = loginView.findViewById(R.id.password);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(loginView)
                .setTitle(R.string.login_title)
                .setPositiveButton("LOG IN", null)
                .setNegativeButton("CANCEL", null)
                .create();

        dialog.setCanceledOnTouchOutside(false);

        SharedPreferences pref = this.getSharedPreferences("cgsport", Context.MODE_PRIVATE);

        if (pref.contains("key")) {
            dialog.show();

            dialog.setTitle(getString(R.string.please_wait));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
            loginFormView.setVisibility(View.GONE);

            Thread thread = new Thread(() -> {
                try  {
                    NetworkService.run(new JSONObject(pref.getString("key", null)));
                    Snackbar.make(view, R.string.everything_is_done, Snackbar.LENGTH_LONG)
                            .setAction("I KNOW", null)
                            .show();
                } catch (Exception e) {
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("I KNOW", null)
                            .show();
                } finally {
                    dialog.dismiss();
                }
            });
            thread.start();
        } else {
            dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(__view -> {
                        String username = usernameView.getText().toString();
                        String password = passwordView.getText().toString();
                        if (username.length() == 0) {
                            usernameView.setError(getString(R.string.student_id_is_required));
                            usernameView.requestFocus();
                            return;
                        }
                        if (password.length() == 0) {
                            passwordView.setError(getString(R.string.password_is_required));
                            passwordView.requestFocus();
                            return;
                        }

                        dialog.setTitle(getString(R.string.please_wait));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                        progressView.setVisibility(View.VISIBLE);
                        loginFormView.setVisibility(View.GONE);

                        Thread thread = new Thread(() -> {
                            try  {
                                JSONObject c = NetworkService.login(username, password);
                                NetworkService.run(c);

                                this.getSharedPreferences("cgsport", Context.MODE_PRIVATE)
                                        .edit()
                                        .putString("key", c.toString())
                                        .apply();

                                Snackbar.make(view, R.string.everything_is_done, Snackbar.LENGTH_LONG)
                                        .setAction("I KNOW", null)
                                        .show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("I KNOW", null)
                                        .show();
                            } finally {
                                dialog.dismiss();
                            }
                        });
                        thread.start();
                    }));
            dialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cgactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.no_settings_title)
                    .setMessage(R.string.no_settings_msg)
                    .setPositiveButton(R.string.i_will_go_away, null)
                    .create();
            dialog.show();
            return true;
        } else {
            SharedPreferences pref = this.getSharedPreferences("cgsport", Context.MODE_PRIVATE);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Clear Credential")
                    .setMessage("Are u sure to clear credential?")
                    .setPositiveButton("OK", (dialog1, id1) -> pref.edit().remove("key").apply())
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
