package com.example.simplemusicplayer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView nextIv,playIv,lastIv;
    TextView singerTv,songTv;
    RecyclerView musicRv;
    List<localMusicBean>mDatas;
    private LocalMusicAdapter adapter;
    private LinearLayoutManager layoutManager;
    int currentPosition = -1;
    MediaPlayer mediaPlayer;
    int currentPausePositionInSong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "请给予储存权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                initView();
                mediaPlayer = new MediaPlayer();
            }
        }else {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "请给予储存权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.READ_MEDIA_AUDIO}, 1);

            } else {
                initView();
                mediaPlayer = new MediaPlayer();
            }
        }
        mDatas = new ArrayList<>();
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        loadLocalMusicdata();
        setEventListener();
    }

    private void setEventListener() {
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currentPosition = position;
                Log.d("TAG1", "OnItemClick: "+currentPosition);
                localMusicBean MusicBean = mDatas.get(position);
                playMusicBean(MusicBean);
            }
        });
    }

    public void playMusicBean(localMusicBean MusicBean) {
        singerTv.setText(MusicBean.getSinger());
        songTv.setText(MusicBean.getSong());
        stopMusic();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(MusicBean.getPath());
            currentPausePositionInSong = 0;
            playMusic();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void playMusic() {
        if (mediaPlayer != null && ! mediaPlayer.isPlaying()) {
            if (currentPausePositionInSong == 0) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
                currentPausePositionInSong = 0;
            }

            playIv.setImageResource(R.drawable.icon_pause);
        }
    }
    private void pauseMusic() {
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            currentPausePositionInSong = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.drawable.icon_play);
        }
    }
    private void stopMusic() {
        if(mediaPlayer != null){
            //currentPosition = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.drawable.icon_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void loadLocalMusicdata() {
        ContentResolver resolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri,null,null,null);
        int id = 0;
        while (cursor.moveToNext()){
            @SuppressLint("Range") String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            @SuppressLint("Range") String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            @SuppressLint("Range") String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            @SuppressLint("Range") Long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            String time = simpleDateFormat.format(new Date(duration));
            localMusicBean bean = new localMusicBean(sid, song, singer, album, time, path);
            mDatas.add(bean);
        }
        adapter.notifyDataSetChanged();
    }
    private void initView(){
         nextIv = findViewById(R.id.local_music_bottom_iv_next);
         playIv = findViewById(R.id.local_music_bottom_iv_play);
         lastIv = findViewById(R.id.local_music_bottom_iv_last);
         singerTv = findViewById(R.id.local_music_bottom_tv_singer);
         songTv = findViewById(R.id.local_music_bottom_tv_song);
         musicRv = findViewById(R.id.local_music_rv);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.local_music_bottom_iv_last){
            Log.d("TAG1", String.valueOf("last:" + currentPosition));
            if (currentPosition == 0 ||currentPosition == -1) {
                Toast.makeText(this,"没有上一曲！",Toast.LENGTH_SHORT).show();
            }else {
                currentPosition = currentPosition - 1;
                Log.d("TAG1", String.valueOf("last-1:" + currentPosition));
                localMusicBean localBean = mDatas.get(currentPosition);
                playMusicBean(localBean);
                Log.d("TAG1", String.valueOf("playmusic:" + currentPosition));
            }

        } else if (v.getId() == R.id.local_music_bottom_iv_next) {
            Log.d("TAG1", String.valueOf("next:" + currentPosition));
            if (currentPosition == mDatas.size() - 1) {
                Toast.makeText(this,"没有下一曲！",Toast.LENGTH_SHORT).show();
            }else {
                currentPosition = currentPosition + 1;
                localMusicBean nextBean = mDatas.get(currentPosition);
                playMusicBean(nextBean);
            }
        } else if (v.getId() == R.id.local_music_bottom_iv_play) {
            if (currentPosition == -1) {
                Toast.makeText(this,"未选择音乐",Toast.LENGTH_SHORT).show();
            }
            if(mediaPlayer.isPlaying() == true){
                pauseMusic();
            }else if(mediaPlayer.isPlaying() == false){
                playMusic();
            }
            
        }

    }
}
