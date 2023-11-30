package ru.tibedox.buyassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textPrice;
    TextView textWeight;
    TextView textDiscount;
    TextView textFinalPrice;
    TextView textPrices;

    EditText editPrice;
    EditText editWeight;
    EditText editDiscount;

    Button buttonClear;
    Button buttonAdd;

    ArrayList<String> prices = new ArrayList<>();
    String strPrices;
    float price, weight, discount;
    float finalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textPrice = findViewById(R.id.textPrice);
        textWeight = findViewById(R.id.textWeight);
        textDiscount = findViewById(R.id.textDiscount);
        textFinalPrice = findViewById(R.id.textFinalPrice);
        textPrices = findViewById(R.id.textPrices);

        editPrice = findViewById(R.id.editPrice);
        editWeight = findViewById(R.id.editWeight);
        editDiscount = findViewById(R.id.editDiscount);

        buttonClear = findViewById(R.id.buttonClear);
        buttonAdd = findViewById(R.id.buttonAdd);

        editPrice.setFocusableInTouchMode(true);
        editPrice.setFocusable(true);
        editPrice.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(editPrice, InputMethodManager.SHOW_IMPLICIT);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editPrice.addTextChangedListener(watcher);
        editWeight.addTextChangedListener(watcher);
        editDiscount.addTextChangedListener(watcher);
    }

    public void add(View view) {
        if(finalPrice == 0) return;
        if(prices.size()>33){
            prices.remove(prices.size()-1);
        }
        prices.add(0, round(price)+"р: "+round(finalPrice)+"р/кг");
        pricesToString();
        textPrices.setText(strPrices);
    }

    private void pricesToString(){
        strPrices = "";
        for(String a: prices){
            strPrices += a+"\n\n";
        }
    }

    private String round(float x) {
        if(x-(int)x > 0) return Float.toString(x);
        return Integer.toString((int)x);
    }

    private void calculate() {
        String s = editPrice.getText() == null ? "0" : editPrice.getText().toString();
        price = s.equals("") ? 0 : Float.parseFloat(s);

        s = editWeight.getText() == null ? "0" : editWeight.getText().toString();
        weight = s.equals("") ? 0 : Float.parseFloat(s);

        s = editDiscount.getText() == null ? "0" : editDiscount.getText().toString();
        discount = s.equals("") ? 0 : Float.parseFloat(s);

        if(weight > 0.0001) {
            try {
                finalPrice = (float) (Math.ceil(price / weight * 1000 * (100 - discount))/100);
            } catch (Exception e) {
                finalPrice = 0;
            }
        } else if(discount > 0.0001) {
            try {
                finalPrice = (float) (Math.ceil(price * (100 - discount))/100);
            } catch (Exception e) {
                finalPrice = 0;
            }
        } else finalPrice = 0;
        textFinalPrice.setText(round(finalPrice));
    }

    public void clear(View view) {
        editPrice.setText("");
        editWeight.setText("");
        editDiscount.setText("");
    }

    public void clearList(View view) {
        prices.clear();
        textPrices.setText("");
    }
}