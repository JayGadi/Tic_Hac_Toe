package com.tic_tac_toe.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tic_tac_toe.model.Player;
import com.tic_tac_toe.R;

public class LoginActivity extends AppCompatActivity {

    private TextView onePlayerLabel;
    private TextView twoPlayerLabel;
    private TextView optionsLabel;
    private MediaPlayer intro;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onePlayerLabel = (TextView) findViewById(R.id.one_player_label);
        twoPlayerLabel = (TextView) findViewById(R.id.two_player_label);
        optionsLabel = (TextView) findViewById(R.id.options_label);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Cheveuxdange.ttf");

        onePlayerLabel.setTypeface(type);
        twoPlayerLabel.setTypeface(type);
        optionsLabel.setTypeface(type);




    }


    public void onePlayer(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.one_player_options, null);
        final Intent onePlayerGame = new Intent(LoginActivity.this, OnePlayerActivity.class);
        final RadioButton oMarker = (RadioButton) layout.findViewById(R.id.o_marker);
        final RadioButton xMarker = (RadioButton) layout.findViewById(R.id.x_marker);
        final RadioButton easy = (RadioButton) layout.findViewById(R.id.easy);
        final RadioButton medium = (RadioButton) layout.findViewById(R.id.medium);
        final RadioButton hard = (RadioButton) layout.findViewById(R.id.hard);
        final TextView playerName = (TextView) layout.findViewById(R.id.username);


        oMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oMarker.setBackgroundResource(R.drawable.tic_tac_toe_o_black);
                xMarker.setBackgroundResource(R.drawable.tic_tac_toe_x);
            }
        });

        xMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xMarker.setBackgroundResource(R.drawable.tic_tac_toe_x_black);
                oMarker.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
        });

        builder.setView(layout)
                .setTitle("One Player Game Options")
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if ((playerName.getText().length() > 0) && (oMarker.isChecked() || xMarker.isChecked()) && (easy.isChecked() || medium.isChecked() || hard.isChecked())) {
                            if (oMarker.isChecked()) {
                                if (easy.isChecked()) {
                                    onePlayerGame.putExtra("difficulty", "easy");
                                } else if (medium.isChecked()) {
                                    onePlayerGame.putExtra("difficulty", "medium");
                                } else if (hard.isChecked()) {
                                    onePlayerGame.putExtra("difficulty", "hard");
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please enter a difficulty", Toast.LENGTH_SHORT).show();
                                }

                                Player player = new Player(playerName.getText().toString(), 'o', 0);
                                Player computer = new Player("Computer", 'x', 0);
                                onePlayerGame.putExtra("player", player);
                                onePlayerGame.putExtra("computer", computer);
                            } else if (xMarker.isChecked()) {
                                if (easy.isChecked()) {
                                    onePlayerGame.putExtra("difficulty", "easy");
                                } else if (medium.isChecked()) {
                                    onePlayerGame.putExtra("difficulty", "medium");
                                } else if (hard.isChecked()) {
                                    onePlayerGame.putExtra("difficulty", "hard");
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please enter a difficulty", Toast.LENGTH_SHORT).show();
                                }

                                Player player = new Player(playerName.getText().toString(), 'x', 0);
                                Player computer = new Player("Computer", 'o', 0);
                                onePlayerGame.putExtra("player", player);
                                onePlayerGame.putExtra("computer", computer);
                            }
                            startActivity(onePlayerGame);
                        } else {
                            Toast.makeText(LoginActivity.this, "Please enter a username and select a marker", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create().show();
    }

    public void twoPlayer(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View layout = inflater.inflate(R.layout.two_player_options, null);
        final Intent twoPlayerLocal = new Intent(LoginActivity.this, TwoPlayerActivityLocal.class);
        final TextView playerOneName = (TextView) layout.findViewById(R.id.player_one);
        final TextView playerTwoName = (TextView) layout.findViewById(R.id.player_two);
        final RadioButton localGame = (RadioButton) layout.findViewById(R.id.local_game);
        final RadioButton bluetoothGame = (RadioButton) layout.findViewById(R.id.bluetooth_game);
        final RadioButton oMarker = (RadioButton) layout.findViewById(R.id.o_marker);
        final RadioButton xMarker = (RadioButton) layout.findViewById(R.id.x_marker);

        localGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerTwoName.setVisibility(View.VISIBLE);
                oMarker.setVisibility(View.VISIBLE);
                xMarker.setVisibility(View.VISIBLE);
            }
        });

        bluetoothGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerTwoName.setVisibility(View.GONE);
                oMarker.setVisibility(View.GONE);
                xMarker.setVisibility(View.GONE);

            }
        });

        oMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oMarker.setBackgroundResource(R.drawable.tic_tac_toe_o_black);
                xMarker.setBackgroundResource(R.drawable.tic_tac_toe_x);
            }
        });

        xMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xMarker.setBackgroundResource(R.drawable.tic_tac_toe_x_black);
                oMarker.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
        });
        builder.setView(layout)
                .setTitle("Two Player Game Options")
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (localGame.isChecked()) {

                            if ((playerOneName.getText().length() > 0) && (playerTwoName.getText().length() > 0)) {

                                if (oMarker.isChecked()) {
                                    Player playerOne = new Player(playerOneName.getText().toString(), 'o', 0);
                                    Player playerTwo = new Player(playerTwoName.getText().toString(), 'x', 0);
                                    twoPlayerLocal.putExtra("playerOne", playerOne);
                                    twoPlayerLocal.putExtra("playerTwo", playerTwo);
                                    twoPlayerLocal.putExtra("isTurn", playerOne.getPlayerMarker());
                                    startActivity(twoPlayerLocal);
                                } else if (xMarker.isChecked()) {
                                    Player playerOne = new Player(playerOneName.getText().toString(), 'x', 0);
                                    Player playerTwo = new Player(playerTwoName.getText().toString(), 'o', 0);
                                    twoPlayerLocal.putExtra("playerOne", playerOne);
                                    twoPlayerLocal.putExtra("playerTwo", playerTwo);
                                    startActivity(twoPlayerLocal);
                                }

                                else {
                                    Toast.makeText(LoginActivity.this, "Please select the starting marker", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Please enter a name for the players", Toast.LENGTH_SHORT).show();
                            }
                        } else if (bluetoothGame.isChecked()) {
                            Intent bluetoothIntent = new Intent(LoginActivity.this, TwoPlayerActivityBluetooth.class);
                            String playerOne = null;
                            if (playerOneName.getText().length() > 0) {
                                playerOne = playerOneName.getText().toString();
                                bluetoothIntent.putExtra("playerOne", playerOne);
                                startActivity(bluetoothIntent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Please enter a player name", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            Toast.makeText(LoginActivity.this, "Please Select a game type", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it

        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }


}

