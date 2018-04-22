package com.example.ishaycena.math_exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Stream;

public class Game_Activity extends AppCompatActivity {

    //#region Global Vars

    //Equation textBox
    TextView tvEquation;
    //four answer buttons
    Button btn1, btn2, btn3, btn4;
    //the two operands
    Integer num1, num2;
    //the operator to be performed on the operands
    Character operator;

    //#region listener init
    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (v instanceof Button) {



                    Button tmpBtn = (Button) v;
                    String resValue = String.format(Locale.ENGLISH, "%.2f", result);

                    if (tmpBtn.getText().equals(resValue)) {
                        // if this is the first guess in this round
                        if (isFirstGuess){
                            ++totalScore;//increment total score
                            //TODO: Add score to a SharedPreference @Dor @Ya Mesanen
                            //editor.putInt("Your score:",totalScore);
                            //editor.commit();
                        }

                        Toast.makeText(Game_Activity.this, "Correct !", Toast.LENGTH_SHORT).show();
                        ++roundNum;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //my_button.setBackgroundResource(R.drawable.defaultcard);
                                btnsArr[arrBtnPosition].setBackgroundColor(Color.GREEN);
                            }
                        }, 0);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                StartGameRound(roundNum);

                            }
                        }, 2000);

                    } else {
                        isFirstGuess = false;
                        tmpBtn.setEnabled(false);
                        tmpBtn.setBackgroundColor(Color.RED);
                        Toast.makeText(Game_Activity.this, "Incorrect !", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception ex){
                Toast.makeText(Game_Activity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    //#endregion

    // saving the answer buttons here
    Button[] btnsArr = new Button[4];
    // other fake results values in here
    Double[] fakeResults = new Double[3];
    // using key to roll the operator in each round
    int key = 1;
    // the position of the answer button in the btns array (Will be rolled each round)
    int arrBtnPosition = 0;
    //the result of the current equation (updates on each round)
    double result = 0.0;
    // if you need to ask what this is for, you should quit programming
    Random rnd = new Random(System.currentTimeMillis());
    // Operators map ( +, -, *, / )
    final HashMap<Integer, Character> MAP_OPERATORS = new HashMap<Integer, Character>();

    //#endregion

    //TODO: Organise this thingy please I can't be arsed
    Button btnStartGame;
    int roundNum = 0;
    boolean isFirstGuess = true;// guesses in each round, t
    int totalScore = 0;// out of 3, because there're only 3 rounds
    //SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
    //SharedPreferences.Editor editor = prefs.edit();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_);

        try {

            //#region init

            btn1 = findViewById(R.id.btnAns1);
            btn2 = findViewById(R.id.btnAns2);
            btn3 = findViewById(R.id.btnAns3);
            btn4 = findViewById(R.id.btnAns4);
            btnStartGame = findViewById(R.id.btnStartGame);
            tvEquation = findViewById(R.id.tvEquation);

            btn1.setOnClickListener(btnListener);
            btn2.setOnClickListener(btnListener);
            btn3.setOnClickListener(btnListener);
            btn4.setOnClickListener(btnListener);

            btnStartGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnStartGame.setVisibility(View.INVISIBLE);
                    StartGameRound(roundNum);
                }
            });

            //#endregion

            //editor.putInt()
            Init_Components();
            DisableAllButtons();

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Disables all the buttons (until the user clicks "start game")
     */
    protected void DisableAllButtons(){
        for (int i = 0; i < btnsArr.length; i++) {
            btnsArr[i].setVisibility(View.INVISIBLE);
            btnsArr[i].setEnabled(false);
        }
    }


    /**
     * using this to init the components, i.e: map, btnsArr etc.,  when the user hits "start game"
     */
    protected void Init_Components(){
        PopulateBtnsArr();


        EnableButtons();


        PopulateMap();
    }

    /**
     * Initiates a new Game Round
     */
    protected void StartGameRound(int roundNum){
        if (roundNum == 3){
            Toast.makeText(Game_Activity.this, "You've finished the game !", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Game_Activity.this, MainActivity.class));
        }

        try {
            EnableButtons();
            RollMainValues();

            switch (operator) {
                case '+':
                    result = (double) (num1 + num2);
                    break;
                case '-':
                    result = (double) (num1 - num2);
                    break;
                case '*':
                    result = (double) (num1 * num2);
                    break;
                case '/':
                    result = (double) (num1 / num2);
                    DecimalFormat df = new DecimalFormat("#.##");
                    String tmp = df.format((double) (num1 / num2));
                    double pt = ( (double) (num1 / num2) ) - Math.floor( (double) (num1 / num2) );
                    Log.d("FakeRes", "switch: " + tmp);
                    break;
                default:
                    Log.d("Logger", "Rolled out of range");
                    break;
            }
            //end of switch
            btnsArr[arrBtnPosition].setText(String.format(Locale.ENGLISH, "%.2f", result));//correct button
            tvEquation.setText(String.format(Locale.ENGLISH, "%d %c %d", num1, operator, num2));//equation text

            RollFakes();
            PutFakeValues();

        }catch (Exception ex){
            Toast.makeText(Game_Activity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Enables all of the buttons at the end of a new round
     */
    protected void EnableButtons(){
        for (int i = 0; i < 4; i++) {
            btnsArr[i].setEnabled(true);
            btnsArr[i].setVisibility(View.VISIBLE);
            btnsArr[i].setBackgroundColor(Color.parseColor("#E99616"));
        }
    }


    /**
     * Puts the fake result values in the buttons
     */
    protected void PutFakeValues(){
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (i == arrBtnPosition){
                continue;
            }
            btnsArr[i].setText(String.format(Locale.ENGLISH, "%.2f", fakeResults[count++]));
            Log.d("Logger", "Fake res #" + i + " is: " + fakeResults[count - 1]);
        }
    }


    /**
     * Rolls fake values for the other answer buttons
     */
    protected void RollFakes(){
        int tmp = (int)result;
        int maxRange = 0, minRange = 0;
        double xrnd = result;
        double point = result - Math.floor(result);
        Log.d("FakeRes", "pts: " + (String.valueOf(result - Math.floor(result))));
        Log.d("FakeRes", "result: " + String.valueOf(result));

        for (int i = 0; i < 3; i++) {
            //proofing not to get the same number as the result
            maxRange = tmp + 5;
            minRange = tmp - 5;

            maxRange = Math.abs(maxRange);
            minRange = Math.abs(minRange);
            if (maxRange < minRange){

                maxRange ^= minRange;
                minRange ^= maxRange;
                maxRange ^= minRange;
            }

            while (xrnd == result) {
                xrnd = (double) (rnd.nextInt(maxRange) + minRange);
                while (Arrays.asList(fakeResults).contains(xrnd)){
                    xrnd = (double) (rnd.nextInt(maxRange) + minRange);
                }
            }
           xrnd += point;
            Log.d("FakeRes", "Point is: " + String.format("%f", point));
            Log.d("FakeRes", "xrnd is: " + String.format("%f", xrnd));
            fakeResults[i] = xrnd;
            xrnd = result;
        }
    }


    /**
     * Rolls equation values on each round
     */
    protected void RollMainValues(){
        num1 = rnd.nextInt(200) + 10;
        num2 = rnd.nextInt(200) + 10;

        //making sure the first num is bigger b/c of division and minus
        if (num1 < num2){
            num1 ^= num2;
            num2 ^= num1;
            num1 ^= num2;
        }

        //rolls the button number in which the answer will be at
        arrBtnPosition = rnd.nextInt(4);
        //rolls which operator will be performed in the equation
        key = rnd.nextInt(4) + 1;
        operator = MAP_OPERATORS.get(key);//operator from map
    }


    /**
     * Puts the Answer buttons in the array
     */
    protected void PopulateBtnsArr(){
        btnsArr[0] = btn1;
        btnsArr[1] = btn2;
        btnsArr[2] = btn3;
        btnsArr[3] = btn4;
    }


    /**
     * Populates the Operators Map with operators values
     */
    protected void PopulateMap(){
        MAP_OPERATORS.put(1, '+');
        MAP_OPERATORS.put(2, '-');
        MAP_OPERATORS.put(3, '*');
        MAP_OPERATORS.put(4, '/');
    }

}