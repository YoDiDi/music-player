package com.example.ff.a523;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private SeekBar seekBar;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private TextView textView1,textView_two;
    private MediaPlayer mp = new MediaPlayer();
    String song_path="";
    int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView1 = (TextView) findViewById(R.id.qq);
        textView_two = (TextView) findViewById(R.id.qqq);
//        mp.seekTo(0);
//        seekBar.setProgress(0);
//        seekBar.setOnSeekBarChangeListener(seekbarChangeListener);

        new TimeThread().start();




        if (ActivityCompat.checkSelfPermission( MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 123);
            return;
        }

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
////两个if用来解决android 6.0 7.0 权限问题


        ArrayList<String> list = new ArrayList<String>(); //音乐列表
        File sdpath= Environment.getExternalStorageDirectory(); //获得手机SD卡路径
        File path=new File(sdpath+""); //获得SD卡的mp3文件夹
        //返回以.mp3结尾的文件 (自定义文件过滤)
        File[ ] songFiles = path.listFiles( new MyFilter(".mp3") );
        for (File file :songFiles){
            list.add( file.getAbsolutePath() ); //获取文件的绝对路径
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_single_choice, list );
        ListView li=(ListView)findViewById(R.id.listView);
        li.setAdapter(adapter);
        li.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //单选
        seekBar.setProgress(0);


        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mp.getCurrentPosition());
            }
        };
        mTimer.schedule(mTimerTask, 0, 10);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekBar.setMax(mp.getDuration());//进度条
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this,"正在拖动进度条",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mp.seekTo(seekBar.getProgress());
                Toast.makeText(MainActivity.this,"拖动进度条完成"+seekBar.getProgress(),Toast.LENGTH_SHORT).show();

            }
        });


        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                song_path=((TextView)view).getText().toString();
                try{
                    mp.reset(); //重置
                    mp.setDataSource(song_path);
                    mp.prepare(); //准备
                    mp.start(); //播放
                }catch (Exception e){ }
            }
        });


        final ImageButton btnpause=(ImageButton)findViewById(R.id.btn_pause);
        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (song_path.isEmpty())
                    Toast.makeText(getApplicationContext(), "先选首歌曲先听听",
                            Toast.LENGTH_SHORT).show();
                else {
                    if (mp.isPlaying()) {
                        mp.pause(); //暂停
//                        btnpause.setImageResource(R.drawable.br);
                    } else if (!mp.isPlaying()) {
                        mp.start(); //继续播放
//                        seekBar.setProgress(0);
//                        btnpause.setImageResource(R.drawable.br);

                    }
                }
            }
        });


        Button bt2=(Button)findViewById(R.id.button1);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mp.isPlaying() )
                    mp.pause();
                i--;

                ArrayList<String> list1 = new ArrayList<String>(); //音乐列表
                File sdpath= Environment.getExternalStorageDirectory(); //获得手机SD卡路径
                File path=new File(sdpath+""); //获得SD卡的mp3文件夹
                //返回以.mp3结尾的文件 (自定义文件过滤)
                File[ ] songFiles = path.listFiles( new MyFilter(".mp3") );
                for (File file :songFiles){
                    list1.add( file.getAbsolutePath() ); //获取文件的绝对路径
                }

                if(i<0)
                    i=list1.size()-1;

                song_path=(String) list1.get(i);
                try{
                    mp.reset(); //重置
                    mp.setDataSource(song_path);
                    mp.prepare(); //准备
                    mp.start(); //播放
                }catch (Exception e){
                    e.printStackTrace();
                }
                }
        });



        Button bt3=(Button)findViewById(R.id.button3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mp.isPlaying() )
                    mp.pause();
                i++;
                ArrayList<String> list1 = new ArrayList<String>(); //音乐列表
                File sdpath= Environment.getExternalStorageDirectory(); //获得手机SD卡路径
                File path=new File(sdpath+""); //获得SD卡的mp3文件夹
                //返回以.mp3结尾的文件 (自定义文件过滤)
                File[ ] songFiles = path.listFiles( new MyFilter(".mp3") );
                for (File file :songFiles){
                    list1.add( file.getAbsolutePath() ); //获取文件的绝对路径
                }

                if(i>=list1.size())
                    i=0;

                song_path=(String) list1.get(i);
                try{
                    mp.reset(); //重置
                    mp.setDataSource(song_path);
                    mp.prepare(); //准备
                    mp.start(); //播放
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mp!=null ){
            mp.stop();
            mp.release();
        }
        Toast.makeText(getApplicationContext(), "退出啦", Toast.LENGTH_SHORT).show();

    }


    class TimeThread extends  Thread{
        @Override
        public void run() {
            do{
                try {
                    Thread.sleep(200);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if ( song_path.isEmpty() ){
                        break;
                    }
                    else {
                        int musicTime = mp.getCurrentPosition() / 1000;
                        int Time = mp.getDuration() / 1000;
                        String time = Time / 60 + ":" + Time % 60;
                        String show = musicTime / 60 + ":" + musicTime % 60;
                        textView1.setText(show + "/" + time);
                        break;
                    }
                default:
                    break;
            }
        }
    };
}
