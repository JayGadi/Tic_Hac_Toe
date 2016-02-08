package com.tic_tac_toe.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tic_tac_toe.control.GameLogic;
import com.tic_tac_toe.model.Player;
import com.tic_tac_toe.R;

import java.util.Random;


public class OnePlayerActivity extends Activity implements View.OnClickListener {
    //First row buttons
    private ImageButton one_one;
    private ImageButton one_two;
    private ImageButton one_three;
    //Second row buttons
    private ImageButton two_one;
    private ImageButton two_two;
    private ImageButton two_three;
    //Third row buttons
    private ImageButton three_one;
    private ImageButton three_two;
    private ImageButton three_three;

    private TextView playerScoreLabel;
    private TextView computerScoreLabel;

    private GameLogic gameLogic;
    private Random rowAI;
    private Random colAI;
    //set X
    private char playerMarker;
    private char computerMarker;
    private String playerName;
    private String computerName;

    private Player player;
    private Player computer;

    private int playerScore;
    private int computerScore;

    private String difficulty;
    private MediaPlayer sound;
    private MediaPlayer winSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_player);

        Bundle extras = getIntent().getExtras();

        gameLogic = new GameLogic();

        //initialize buttons
        one_one = (ImageButton) findViewById(R.id.one_one);
        one_two = (ImageButton) findViewById(R.id.one_two);
        one_three = (ImageButton) findViewById(R.id.one_three);
        two_one = (ImageButton) findViewById(R.id.two_one);
        two_two = (ImageButton) findViewById(R.id.two_two);
        two_three = (ImageButton) findViewById(R.id.two_three);
        three_one = (ImageButton) findViewById(R.id.three_one);
        three_two = (ImageButton) findViewById(R.id.three_two);
        three_three = (ImageButton) findViewById(R.id.three_three);

        playerScoreLabel = (TextView) findViewById(R.id.player_score);
        computerScoreLabel = (TextView) findViewById(R.id.computer_score);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Cheveuxdange.ttf");

        playerScoreLabel.setTypeface(type);
        computerScoreLabel.setTypeface(type);

        if (extras != null) {
            player = (Player) extras.getSerializable("player");
            computer = (Player) extras.getSerializable("computer");
            difficulty = extras.getString("difficulty");
        }

        playerMarker = player.getPlayerMarker();
        playerScore = player.getScore();
        playerName = player.getName();

        computerName = computer.getName();
        computerScore = computer.getScore();
        computerMarker = computer.getPlayerMarker();
        gameLogic.setCompMarker(computerMarker);

        playerScoreLabel.setText(playerName + ": " + playerScore);
        computerScoreLabel.setText(computerName + ": " + computerScore);

        rowAI = new Random();
        colAI = new Random();

        sound = MediaPlayer.create(this, R.raw.pencil);

        //set click listeners
        one_one.setOnClickListener(this);
        one_two.setOnClickListener(this);
        one_three.setOnClickListener(this);
        two_one.setOnClickListener(this);
        two_two.setOnClickListener(this);
        two_three.setOnClickListener(this);
        three_one.setOnClickListener(this);
        three_two.setOnClickListener(this);
        three_three.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent loginActivity = new Intent(OnePlayerActivity.this, LoginActivity.class);
                        startActivity(loginActivity);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one_one:
                gameLogic.addMarker(playerMarker, 0, 0);
                checkGameStatus();
                updateUI(playerMarker, 0, 0);
                computerLogic();
                one_one.setEnabled(false);
                break;
            case R.id.one_two:
                gameLogic.addMarker(playerMarker, 0, 1);
                checkGameStatus();
                updateUI(playerMarker, 0, 1);
                computerLogic();
                one_two.setEnabled(false);
                break;
            case R.id.one_three:
                gameLogic.addMarker(playerMarker, 0, 2);
                checkGameStatus();
                updateUI(playerMarker, 0, 2);
                computerLogic();
                one_three.setEnabled(false);
                break;
            case R.id.two_one:
                gameLogic.addMarker(playerMarker, 1, 0);
                checkGameStatus();
                updateUI(playerMarker, 1, 0);
                computerLogic();
                two_one.setEnabled(false);
                break;
            case R.id.two_two:
                gameLogic.addMarker(playerMarker, 1, 1);
                checkGameStatus();
                updateUI(playerMarker, 1, 1);
                computerLogic();
                two_two.setEnabled(false);
                break;
            case R.id.two_three:
                gameLogic.addMarker(playerMarker, 1, 2);
                checkGameStatus();
                updateUI(playerMarker, 1, 2);
                computerLogic();
                two_three.setEnabled(false);
                break;
            case R.id.three_one:
                gameLogic.addMarker(playerMarker, 2, 0);
                checkGameStatus();
                updateUI(playerMarker, 2, 0);
                computerLogic();
                three_one.setEnabled(false);
                break;
            case R.id.three_two:
                gameLogic.addMarker(playerMarker, 2, 1);
                checkGameStatus();
                updateUI(playerMarker, 2, 1);
                computerLogic();
                three_two.setEnabled(false);
                break;
            case R.id.three_three:
                gameLogic.addMarker(playerMarker, 2, 2);
                checkGameStatus();
                updateUI(playerMarker, 2, 2);
                computerLogic();
                three_three.setEnabled(false);
                break;
        }
    }

    public void computerLogic() {

        if(!gameLogic.checkFull()&& !gameLogic.hasWon()) {

            int [] location = null;

            if(difficulty.equals("easy")){
                location = gameLogic.computerAIEasy();
            }else if(difficulty.equals("medium")){

                location = gameLogic.computerAIMedium();
            }else if(difficulty.equals("hard")){

                location = gameLogic.computerAIHard();
            }

            final int row = location[0];
            final int col = location[1];


            Log.d("AI ROW", String.valueOf(row));
            Log.d("AI COL", String.valueOf(col));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameLogic.addMarker(computerMarker, row, col);
                    updateUI(computerMarker, row, col);

                }
            }, 2000);


        }
    }

    public void updateUI(char marker, int row, int col){

        if (row == 0 && col == 0) {
            if (marker == 'x') {
                one_one.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                one_one.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            one_one.setEnabled(false);
        } else if (row == 0 && col == 1) {
            if (marker == 'x') {
                one_two.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                one_two.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            one_two.setEnabled(false);
        } else if (row == 0 && col == 2) {
            if (marker == 'x') {
                one_three.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                one_three.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            one_three.setEnabled(false);
        } else if (row == 1 && col == 0) {
            if (marker == 'x') {
                two_one.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                two_one.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            two_one.setEnabled(false);
        } else if (row == 1 && col == 1) {
            if (marker == 'x') {
                two_two.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                two_two.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            two_two.setEnabled(false);
        } else if (row == 1 && col == 2) {
            if (marker == 'x') {
                two_three.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                two_three.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            two_three.setEnabled(false);
        } else if (row == 2 && col == 0) {
            if (marker == 'x') {
                three_one.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                three_one.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            three_one.setEnabled(false);
        } else if (row == 2 && col == 1) {
            if (marker == 'x') {
                three_two.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                three_two.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            three_two.setEnabled(false);
        } else if (row == 2 && col == 2) {
            if (marker == 'x') {
                three_three.setBackgroundResource(R.drawable.tic_tac_toe_x);
            } else {
                three_three.setBackgroundResource(R.drawable.tic_tac_toe_o);
            }
            sound.start();
            three_three.setEnabled(false);
        }
        checkGameStatus();
    }

    public void checkGameStatus() {

        if (gameLogic.hasWon()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (gameLogic.getWinningMarker() == playerMarker) {
                builder.setTitle("Congratulations, You Won!");
                winSound = MediaPlayer.create(this, R.raw.woohoo);
                player.setScore(playerScore + 1);
            } else {
                builder.setTitle("You Lost, Better Luck Next Time!");
                winSound = MediaPlayer.create(this, R.raw.crowd_boo);
                computer.setScore(computerScore + 1 );

            }

            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent newGame = new Intent(OnePlayerActivity.this, OnePlayerActivity.class);
                    newGame.putExtra("player", player);
                    newGame.putExtra("computer", computer);
                    newGame.putExtra("difficulty", difficulty);
                    startActivity(newGame);
                }
            }).setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent loginActivity = new Intent(OnePlayerActivity.this, LoginActivity.class);
                    startActivity(loginActivity);
                }
            });
            winSound.start();
            builder.create().show();
        } else if (gameLogic.checkFull()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tie Game, Better Luck Next Time")
                    .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent newGame = new Intent(OnePlayerActivity.this, OnePlayerActivity.class);
                            newGame.putExtra("player", player);
                            newGame.putExtra("computer", computer);
                            newGame.putExtra("difficulty", difficulty);
                            startActivity(newGame);
                        }
                    })
                    .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent loginActivity = new Intent(OnePlayerActivity.this, LoginActivity.class);
                            startActivity(loginActivity);
                        }
                    });
            winSound = MediaPlayer.create(this, R.raw.crowd_boo);
            winSound.start();
            builder.create().show();

        }
    }

}
