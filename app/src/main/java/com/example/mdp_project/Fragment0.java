package com.example.mdp_project;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

// 1. viewPager2를 삭제해본다.<MainActivity/ 챗gpt>
public class Fragment0 extends Fragment {
    View view;
    Button button;
    Vibrator vr;
    int flag = 1;
    long[] pattern = {2000, 1000, 2000, 1000};
    int[] amplitudes = {50, 5, 50, 5};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment

         */
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_0, container, false);
        button = viewGroup.findViewById(R.id.button2);
        view = viewGroup.findViewById(R.id.view);
        vr = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = flag * -1;
                if (flag == -1) {
                    MainActivity.sendSignal("a");
                    if (Build.VERSION.SDK_INT >= 26) {
                        vr.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, 0));
                    } else {
                        vr.vibrate(1000);
                    }
                }
                else if(flag == 1){
                    vr.cancel();
                    flag = 1;
                }
            }
        });
        flag = 1;
        return viewGroup;
    }
//    public void send(int data){
//        try {
//            Toast.makeText(getActivity(), "소켓 연결 완료.", Toast.LENGTH_SHORT).show();
//
//            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//            outputStream.writeObject(data);
//            outputStream.flush();
//            Toast.makeText(getActivity(), "데이터 전송 완료.", Toast.LENGTH_SHORT).show();
//
//            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
//            Toast.makeText(getActivity(), "서버로부터 받음"+inputStream.readObject(), Toast.LENGTH_SHORT).show();
////            socket.close();
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
    public void onDestroyView() {
        super.onDestroyView();
        if (vr != null) {
            vr.cancel();
            flag = 1;
        }
    }
}