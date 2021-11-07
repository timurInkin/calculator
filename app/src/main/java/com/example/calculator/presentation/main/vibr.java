//package com.example.calculator.presentation.main;
//
//import android.accessibilityservice.AccessibilityService;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.view.View;
//import android.widget.Button;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import com.example.calculator.R;
//
//import org.jetbrains.annotations.NotNull;
//
//class Vibe extends Activity {
//    /**
//     * Called when the activity is first created.
//     */
//    private TextView button1;
//    private Vibrator vibrator;
//    private Context context;
//    @NotNull
//    public static Object vibe;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_activity);
//        button1 = (TextView) findViewById(R.id.main_five);
//        button1.setOnClickListener(new View.OnClickListener() {
//            final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
//            public void onClick(View v) {
//                if (v == button1) vibrator.vibrate(300000);
//            }
//        });
//
//        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//    }
//}
//
//
