package com.example.mdp_project;

import static android.speech.tts.TextToSpeech.ERROR;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int SPEECH_REQUEST_CODE = 0;
    PageAdapter pageAdapter;
    ViewPager2 viewPager2;
    ArrayList<Fragment> fragList = new ArrayList<Fragment>();
    private TextToSpeech tts;

    private static final String DEVICE_ADDRESS = "2C:CF:67:28:A6:D6"; // 라즈베리 파이 블루투스 주소
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static BluetoothAdapter bluetoothAdapter;
    private static BluetoothSocket bluetoothSocket;
    private static OutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                    tts.setPitch(1.0f);         // 음성 톤은 기본 설정
                    tts.setSpeechRate(2.0f);    //음성 속도 기본 설정
                    tts.speak("기본 화면 입니다.", TextToSpeech.QUEUE_ADD, null);
                }
            }
        });
        new Thread(new Runnable() {
            public void run() {
                try {
                    bluetoothSocket = createBluetoothSocket(device);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        String[] permissions = new String[]{Manifest.permission.BLUETOOTH_CONNECT};
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, 01);
                    }
                    bluetoothSocket.connect();
                    tts.speak("연결되었습니다", TextToSpeech.QUEUE_ADD, null);
                    outputStream = bluetoothSocket.getOutputStream();
                    Log.d("MainActivity", "Bluetooth connected");
                } catch (IOException e) {
                    Log.e("MainActivity", "Error connecting to Bluetooth", e);
                }
            }
        }).start();

        viewPager2 = findViewById(R.id.ViewPater2);
        //프로그먼트 선언
        Fragment0 fragment0 = new Fragment0();
        Fragment1 fragment1 = new Fragment1();
        Fragment2 fragment2 = new Fragment2();
        Fragment3 fragment3 = new Fragment3();
        //프래그먼트 리스트에 등록
        fragList.add(fragment0);
        fragList.add(fragment1);
        fragList.add(fragment2);
        fragList.add(fragment3);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.cancel();
                }
                if (position == 0) {
                    tts.stop();
                    tts.setPitch(1.0f);         // 음성 톤은 기본 설정
                    tts.setSpeechRate(2.0f);    //음성 속도 2배 설정
                    tts.speak("기본 화면 입니다.", TextToSpeech.QUEUE_ADD, null);
                } else if (position == 1) {
                    tts.stop();
                    tts.setPitch(1.0f);         // 음성 톤은 기본 설정
                    tts.setSpeechRate(2.0f);    //음성 속도 2배 설정
                    tts.speak("적색맹 화면 입니다.", TextToSpeech.QUEUE_ADD, null);
                } else if (position == 2) {
                    tts.stop();
                    tts.setPitch(1.0f);         // 음성 톤은 기본 설정
                    tts.setSpeechRate(2.0f);    //음성 속도 2배 설정
                    tts.speak("청색맹 화면 입니다.", TextToSpeech.QUEUE_ADD, null);
                } else if (position == 3) {
                    tts.stop();
                    tts.setPitch(1.0f);         // 음성 톤은 기본 설정
                    tts.setSpeechRate(2.0f);    //음성 속도 2배 설정
                    tts.speak("전색맹 화면 입니다.", TextToSpeech.QUEUE_ADD, null);
                }
            }
        });

        //어뎁터에 리스트 등록
        pageAdapter = new PageAdapter(this, fragList);
        //어뎁터 뷰페이저에 등록
        viewPager2.setAdapter(pageAdapter);
        //화면 표시 적용
        DotsIndicator indicator = findViewById(R.id.dots_indicator);
        indicator.setViewPager2(viewPager2);
    }

    @SuppressLint("MissingPermission")
    public static BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            return (BluetoothSocket) m.invoke(device, 1);
        } catch (Exception e) {
            Log.e("MainActivity", "Could not create Insecure RFComm Connection", e);
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    public static void sendSignal(String signal) {
        try {
            outputStream.write(signal.getBytes());
            Log.d("MainActivity", "Signal sent: " + signal);
        } catch (IOException e) {
            Log.e("MainActivity", "Error sending signal", e);
        }
    }
}
