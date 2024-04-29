package com.example.appbanthietbidientu.Activity;

import android.app.Application;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class PayPal extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PayPalCheckout.setConfig(new CheckoutConfig(
                    this,
                    "AYTnvL4KGi4jYP7QsasL4X0FT5en-6VUSBOUHbAsmXAkxdbK7-nfzXfP5dZg6ysC_Q4AaqNr4XWKVdsr",
                    Environment.SANDBOX,
                    CurrencyCode.USD,
                    UserAction.PAY_NOW,
                    "com.example.appbanthietbidientu://paypalpay"
            ));
        }
    }
}
