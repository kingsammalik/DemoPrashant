package com.samapps.demoprashant;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    TextView emulator,root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        emulator = findViewById(R.id.emulator);
        root = findViewById(R.id.root);

        if(Build.FINGERPRINT.contains("generic")){
            //Toast.makeText(this," emulator",Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(this,"not emulator",Toast.LENGTH_SHORT).show();
        }

        if (checkDeviceRoot()){
            root.setText("Device is rooted");
        }
        else{
            root.setText("Device is not rooted");
        }

        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            Toast.makeText(this,"not emulator",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"emulator",Toast.LENGTH_SHORT).show();
        }

        EmulatorDetector.with(this)
                .setCheckTelephony(true)
                .setDebug(true)
                .detect(isEmulator -> {
                    if (isEmulator){
                        emulator.setText("Device is emulator");
                    }
                    else{
                        emulator.setText("Device is not emulator");
                    }
                });


    }

    private boolean checkEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

    private static boolean checkDeviceRoot() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
}