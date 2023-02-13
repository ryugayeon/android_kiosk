package org.tensorflow.lite.examples.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    private TextToSpeech tts;              // TTS 변수 선언
    private Button button0;

    //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
    private int count = TimerCount.COUNT;
    private CountDownTimer countDownTimer;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        button0 = (Button) findViewById(R.id.button0);

        countDownTimer();
        countDownTimer.start();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                button0.setEnabled(true);
                String text = "음료를 주문하는 무인 기기입니다.주문을 할 시 클릭하면 음성을 다시 들을 수 있고 길게 누르면 다음 단계로 넘어갑니다. 이해하셨으면 주문을 진행해주세요.";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");


                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 18000);// 18초 정도 딜레이를 준 후 시작
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.removeMessages(0);

                //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
                countDownTimer.cancel();
                countDownTimer.start();

                String text = "테스트입니다";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });

        button0.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

                handler.removeMessages(0);
                Intent intent = new Intent(StartActivity.this, SelectWhereActivity.class);
                startActivity(intent);
                finish();
                return true;  //true 설정
            }
        });
    }

    //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
    public void countDownTimer(){

        countDownTimer = new CountDownTimer(TimerCount.MILLISINFUTURE, TimerCount.COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                count --;
            }
            public void onFinish() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        };
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;

        super.onDestroy();
    }

}