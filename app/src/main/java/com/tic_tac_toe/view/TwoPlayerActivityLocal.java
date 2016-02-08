package com.tic_tac_toe.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tic_tac_toe.control.GameLogic;
import com.tic_tac_toe.model.Player;
import com.tic_tac_toe.R;


public class TwoPlayerActivityLocal extends Activity implements View.OnClickListener {
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

    private TextView playerOneLabel;
    private TextView playerTwoLabel;

    private GameLogic gameLogic;

    private Player playerOne;
    private String playerOneName;
    private char playerOneMarker;
    private int playerOneScore;

    private Player playerTwo;
    private String playerTwoName;
    private char playerTwoMarker;
    private int playerTwoScore;

    private MediaPlayer sound;
    private MediaPlayer winSound;

    //set X
    char isTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);




        gameLogic = new GameLogic();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            playerOne = (Player) bundle.getSerializable("playerOne");
            playerTwo = (Player) bundle.getSerializable("playerTwo");
            isTurn = bundle.getChar("isTurn");
        }

        if(isTurn == playerOne.getPlayerMarker()){
            Toast.makeText(this, playerOne.getName() + ", it is your turn!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, playerTwo.getName() + ", it is your turn!", Toast.LENGTH_SHORT).show();
        }

        playerOneName = playerOne.getName();
        playerOneMarker = playerOne.getPlayerMarker();
        playerOneScore = playerOne.getScore();

        playerTwoName = playerTwo.getName();
        playerTwoMarker = playerTwo.getPlayerMarker();
        playerTwoScore = playerTwo.getScore();


        //initialize buttons
        one_one = (ImageButton)findViewById(R.id.one_one);
        one_two = (ImageButton)findViewById(R.id.one_two);
        one_three = (ImageButton)findViewById(R.id.one_three);
        two_one = (ImageButton)findViewById(R.id.two_one);
        two_two = (ImageButton)findViewById(R.id.two_two);
        two_three = (ImageButton)findViewById(R.id.two_three);
        three_one = (ImageButton)findViewById(R.id.three_one);
        three_two = (ImageButton)findViewById(R.id.three_two);
        three_three = (ImageButton)findViewById(R.id.three_three);
        playerOneLabel = (TextView)findViewById(R.id.player_one_score);
        playerTwoLabel = (TextView)findViewById(R.id.player_two_score);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Cheveuxdange.ttf");

        playerOneLabel.setTypeface(type);
        playerTwoLabel.setTypeface(type);

        playerOneLabel.setText(playerOneName + ": " + playerOneScore);
        playerTwoLabel.setText(playerTwoName + ": " + playerTwoScore);

        sound = MediaPlayer.create(this, R.raw.pencil);
        winSound = MediaPlayer.create(this, R.raw.woohoo);
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
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent loginActivity = new Intent(TwoPlayerActivityLocal.this, LoginActivity.class);
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
    public void onClick(View v){

        if(isTurn == 'x'){

            switch (v.getId()){
                case R.id.one_one:
                    gameLogic.addMarker('x', 0, 0);
                    checkGameStatus();
                    one_one.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    one_one.setEnabled(false);
                    break;
                case R.id.one_two:
                    gameLogic.addMarker('x', 0, 1);
                    checkGameStatus();
                    one_two.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    one_two.setEnabled(false);
                    break;
                case R.id.one_three:
                    gameLogic.addMarker('x', 0, 2);
                    checkGameStatus();

                    one_three.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    one_three.setEnabled(false);
                    break;
                case R.id.two_one:
                    gameLogic.addMarker('x', 1, 0);
                    checkGameStatus();

                    two_one.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    two_one.setEnabled(false);
                    break;
                case R.id.two_two:
                    gameLogic.addMarker('x', 1, 1);
                    checkGameStatus();

                    two_two.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    two_two.setEnabled(false);
                    break;
                case R.id.two_three:
                    gameLogic.addMarker('x', 1, 2);
                    checkGameStatus();

                    two_three.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    two_three.setEnabled(false);
                    break;
                case R.id.three_one:
                    gameLogic.addMarker('x', 2, 0);
                    checkGameStatus();

                    three_one.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    three_one.setEnabled(false);
                    break;
                case R.id.three_two:
                    gameLogic.addMarker('x', 2, 1);
                    checkGameStatus();

                    three_two.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    three_two.setEnabled(false);
                    break;
                case R.id.three_three:
                    gameLogic.addMarker('x', 2, 2);
                    checkGameStatus();

                    three_three.setBackgroundResource(R.drawable.tic_tac_toe_x);
                    sound.start();
                    three_three.setEnabled(false);
                    break;
            }
            isTurn = 'o';
        }
        else{

            switch (v.getId()){
                case R.id.one_one:
                    gameLogic.addMarker('o', 0, 0);
                    checkGameStatus();

                    one_one.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    one_one.setEnabled(false);
                    break;
                case R.id.one_two:
                    gameLogic.addMarker('o', 0, 1);
                    checkGameStatus();

                    one_two.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    one_two.setEnabled(false);
                    break;
                case R.id.one_three:
                    gameLogic.addMarker('o', 0, 2);
                    checkGameStatus();

                    one_three.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    one_three.setEnabled(false);
                    break;
                case R.id.two_one:
                    gameLogic.addMarker('o', 1, 0);
                    checkGameStatus();

                    two_one.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    two_one.setEnabled(false);
                    break;
                case R.id.two_two:
                    gameLogic.addMarker('o', 1, 1);
                    checkGameStatus();

                    two_two.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    two_two.setEnabled(false);
                    break;
                case R.id.two_three:
                    gameLogic.addMarker('o', 1, 2);
                    checkGameStatus();

                    two_three.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    two_three.setEnabled(false);
                    break;
                case R.id.three_one:
                    gameLogic.addMarker('o', 2, 0);
                    checkGameStatus();

                    three_one.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    three_one.setEnabled(false);
                    break;
                case R.id.three_two:
                    gameLogic.addMarker('o', 2, 1);
                    checkGameStatus();

                    three_two.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    three_two.setEnabled(false);
                    break;
                case R.id.three_three:
                    gameLogic.addMarker('o', 2, 2);
                    checkGameStatus();

                    three_three.setBackgroundResource(R.drawable.tic_tac_toe_o);
                    sound.start();
                    three_three.setEnabled(false);
                    break;
            }
            isTurn = 'x';
        }
    }

    public void checkGameStatus(){
        if (gameLogic.hasWon()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (gameLogic.getWinningMarker() == playerOneMarker) {
                builder.setTitle("Congratulations " + playerOneName + ", You've Won!");
                isTurn = playerTwoMarker;
                playerOne.setScore(playerOneScore + 1);
            } else {
                builder.setTitle("Congratulations " + playerTwoName + ", You've Won!");
                isTurn = playerOneMarker;
                playerTwo.setScore(playerTwoScore + 1);
            }

            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent newGame = new Intent(TwoPlayerActivityLocal.this, TwoPlayerActivityLocal.class);
                    newGame.putExtra("playerOne", playerOne);
                    newGame.putExtra("playerTwo", playerTwo);
                    newGame.putExtra("isTurn", isTurn);
                    startActivity(newGame);
                }
            }).setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent loginActivity = new Intent(TwoPlayerActivityLocal.this, LoginActivity.class);
                    startActivity(loginActivity);
                }
            });
            winSound.start();
            builder.create().show();

        } else if (gameLogic.checkFull()) {

            if(isTurn == playerOneMarker){

                isTurn = playerTwoMarker;
            }else if(isTurn == playerTwoMarker){

                isTurn = playerOneMarker;

            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tie Game, Better Luck Next Time")
                    .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent newGame = new Intent(TwoPlayerActivityLocal.this, TwoPlayerActivityLocal.class);
                            newGame.putExtra("playerOne", playerOne);
                            newGame.putExtra("playerTwo", playerTwo);
                            newGame.putExtra("isTurn", isTurn);
                            startActivity(newGame);
                        }
                    })
                    .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent loginActivity = new Intent(TwoPlayerActivityLocal.this, LoginActivity.class);
                            startActivity(loginActivity);
                        }
                    });
            winSound = MediaPlayer.create(this, R.raw.crowd_boo);
            winSound.start();
            builder.create().show();

        }
    }

}
