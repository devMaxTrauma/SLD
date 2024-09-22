package com.example.mdp_project;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Fragment1 extends Fragment {
    Button button;
    Vibrator vr;
    int flag = 0;
    long[] pattern = {600, 1100, 800, 1100};
    int[] amplitudes = {0, 50, 0, 50};
    private Handler handler;
    private Runnable myRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this  fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);
        button = viewGroup.findViewById(R.id.button3);
        vr = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.sendSignal("b");
                flag = 1;
                if (Build.VERSION.SDK_INT >= 26) {
                    vr.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, 0));
                }
                return true;
            }
        });

        handler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                MainActivity.sendSignal("red");
            }
        };
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (flag == 1) {
                        MainActivity.sendSignal("c");
                        vr.cancel();
                        flag = 0;
                    }
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.sendSignal("a");
                if (Build.VERSION.SDK_INT >= 26) {
                    vr.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1));
                } else {
                    vr.vibrate(1000);
                }
            }
        });
        return viewGroup;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (vr != null) {
            vr.cancel();
            flag = 0;
        }
        handler.removeCallbacks(myRunnable);
    }
    public void onResume() {
        super.onResume();
        // 프래그먼트가 화면에 보일 때 실행되는 코드
        handler.postDelayed(myRunnable, 5000);  // 즉시 실행
    }

    @Override
    public void onPause() {
        super.onPause();
        // 프래그먼트가 화면에서 사라질 때 실행 중인 작업 중지
        handler.removeCallbacks(myRunnable);
    }
}