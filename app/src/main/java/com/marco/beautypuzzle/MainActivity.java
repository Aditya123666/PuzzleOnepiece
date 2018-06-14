package com.marco.beautypuzzle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements GamePuzzleListener {

    private GamePuzzlePanel gameView;
    private Button          btnTime, btnPause, btnNow;
    private int currentTime;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDialog();
    }

    private void initView() {
        btnTime = (Button) findViewById(R.id.btnTime);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnNow = (Button) findViewById(R.id.btnNow);

        gameView = (GamePuzzlePanel) findViewById(R.id.gameView);

        width = Utils.getScreenWidth(this) - Utils.dip2px(this, getResources().getDimension(R.dimen.marginNormal) * 2);
        gameView.getLayoutParams().height = gameView.getLayoutParams().width = width;
        gameView.setmListener(this);

        btnNow.setText(getString(R.string.btnNow) + (gameView.getLevel() + 1));
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialogTitle))
                .setMessage(R.string.tipsGameController)
                .setPositiveButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.start();
                    }
                }).create().show();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onBtnPause(View view) {
        if (gameView.isPause()) {
            gameView.resume();
            btnPause.setText(R.string.gamePause);
        } else {
            gameView.pause();
            btnPause.setText(R.string.gameResume);
        }
    }

    public void onBtnChange(View view) {
        int count = (int) (Math.random() * Contants.imgIds.length);
        gameView.setmBitmap(BitmapFactory.decodeResource(getResources(), Contants.imgIds[count]));
        gameView.initBitmap();
        gameView.restart();
    }

    public void onBtnRestart(View view) {
        gameView.restart();
    }

    private Dialog    dialog;
    private ImageView imageView;

    private void initDialog() {
        dialog = new Dialog(this, R.style.Dialog_NoTitle);
        imageView = new ImageView(this);
        imageView.setImageBitmap(gameView.getmBitmap());
        dialog.setContentView(imageView);
    }

    public void onBtnOriginal(View view) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else {
            imageView.setImageBitmap(gameView.getmBitmap());
            if (dialog == null) {
                initDialog();
            }
            dialog.show();
        }
    }

    public void onBtnTime(View view) {
        if (gameView.isTimeEnable()) {
            gameView.setTimeEnable(false);
            btnTime.setText(R.string.gameTimeEnable);
        } else {
            gameView.setTimeEnable(true);
            btnTime.setText(getString(R.string.btnTime) + currentTime);
        }
    }

    @Override
    public void nextLevel(int nextLevel) {
        gameView.nextLevel();
        btnNow.setText(getString(R.string.btnNow) + (gameView.getLevel() + 1));
    }

    @Override
    public void timechanged(int currentTime) {
        this.currentTime = currentTime;
        btnTime.setText(getString(R.string.btnTime) + currentTime);
    }

    @Override
    public void gameover() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialogTitle)
                .setMessage(R.string.gameOver)
                .setPositiveButton(R.string.gameRestart, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.restart();
                    }
                }).create().show();
    }

    @Override
    public void gamesuccess() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialogTitle))
                .setMessage(R.string.gameSuccess)
                .setPositiveButton(R.string.gameNextLevel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.nextLevel();
                    }
                }).create().show();
    }


}
