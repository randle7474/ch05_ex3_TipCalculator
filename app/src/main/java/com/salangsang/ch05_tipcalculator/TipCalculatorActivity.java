package com.salangsang.ch05_tipcalculator;

import java.text.NumberFormat;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TipCalculatorActivity extends Activity 
implements OnEditorActionListener, OnSeekBarChangeListener{

    // define variables for the widgets
    private EditText billAmountEditText;
    private TextView percentTextView;
    private TextView tipTextView;
    private SeekBar percentSeekBar;
    private TextView totalTextView;
    private Button applyButton;
    private Button calButton;

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    // define instance variables that should be saved
    private String billAmountString = "";
    private float tipPercent = .15f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_tip_calculator );

        // get references to the widgets
        billAmountEditText = (EditText) findViewById( R.id.billAmountEditText );
        percentTextView = (TextView) findViewById( R.id.percentTextView );
        percentSeekBar = (SeekBar) findViewById( R.id.percentSeekBar );
        tipTextView = (TextView) findViewById( R.id.tipTextView );
        totalTextView = (TextView) findViewById( R.id.totalTextView );
        applyButton = (Button) findViewById( R.id.applyButton );

        // set the listeners
        billAmountEditText.setOnEditorActionListener( this );
        percentSeekBar.setOnSeekBarChangeListener( this );

        // get SharedPreferences object
        savedValues = getSharedPreferences( "SavedValues", MODE_PRIVATE );
    }

    @Override
    public void onPause() {
        // save the instance variables       
        Editor editor = savedValues.edit();
        editor.putString( "billAmountString", billAmountString );
        editor.putFloat( "tipPercent", tipPercent );
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        billAmountString = savedValues.getString( "billAmountString", "" );
        tipPercent = savedValues.getFloat( "tipPercent", .15f );

        // set the bill amount on its widget
        billAmountEditText.setText( billAmountString );

        // calculate and display
        calculateAndDisplay();
    }

    public void calculateAndDisplay() {

        // get the bill amount
        billAmountString = billAmountEditText.getText().toString();
        float billAmount;
        if (billAmountString.equals( "" )) {
            billAmount = 0;
        } else {
            billAmount = Float.parseFloat( billAmountString );
        }
        int progress = percentSeekBar.getProgress();
        tipPercent = (float) progress /100;

        // calculate tip and total 
        float tipAmount = billAmount * tipPercent;
        float totalAmount = billAmount + tipAmount;

        // display the other results with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText( currency.format( tipAmount ) );
        totalTextView.setText( currency.format( totalAmount ) );

        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText( percent.format( tipPercent ) );
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        percentTextView.setText( progress + "%" );

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {


                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        calculateAndDisplay();

                    }
                }