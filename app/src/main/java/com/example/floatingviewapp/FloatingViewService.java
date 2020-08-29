package com.example.floatingviewapp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class FloatingViewService extends Service implements View.OnClickListener , View.OnTouchListener{

    private View floatingView;
    private WindowManager windowManager;
   private View collapsedView,expanded_view,rootView;
   private WindowManager.LayoutParams wdParams;

    int startXPos,startYPos;
    float startTouchX,startTouchY;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        floatingView= LayoutInflater.from(FloatingViewService.this)
                .inflate(R.layout.float_view_layout,null);

        windowManager=(WindowManager) getSystemService(WINDOW_SERVICE);

        wdParams= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        wdParams.gravity= Gravity.TOP| Gravity.LEFT;
        wdParams.x=200;
        wdParams.y=200;

        windowManager.addView(floatingView,wdParams);

        collapsedView=floatingView.findViewById(R.id.collapsed_view);

        ImageView collapsed_closed_button=floatingView.findViewById(R.id.collapsed_closed_button);
        collapsed_closed_button.setOnClickListener(this);

        expanded_view=floatingView.findViewById(R.id.expanded_view);

        ImageView lionImage=floatingView.findViewById(R.id.lionImage);
        lionImage.setOnClickListener(this);

        ImageView btnPrevious=floatingView.findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);

        ImageView leopardImage=floatingView.findViewById(R.id.leopardImage);
        leopardImage.setOnClickListener(this);

        ImageView btnNext=floatingView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        ImageView close_button_expanded=floatingView.findViewById(R.id.close_button_expanded);
        close_button_expanded.setOnClickListener(this);


        ImageView open_button=floatingView.findViewById(R.id.open_button);
        open_button.setOnClickListener(this);

        rootView=floatingView.findViewById(R.id.rootView);
        rootView.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.collapsed_closed_button:
                stopSelf();
                Toast.makeText(this, "The service is stopped completely", Toast.LENGTH_SHORT).show();
                break;

            case R.id.lionImage:
                Toast.makeText(this, "Lion is tapped", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnPrevious:
                Toast.makeText(this, "Previous button is tapped", Toast.LENGTH_SHORT).show();
                break;

            case R.id.leopardImage:
                Toast.makeText(this, "Leopard image is clicked", Toast.LENGTH_SHORT).show();
               break;

            case R.id.btnNext:
                Toast.makeText(this, "Next Button is tapped", Toast.LENGTH_SHORT).show();


            case R.id.close_button_expanded:
                Toast.makeText(this, "expanded close button is tapped", Toast.LENGTH_SHORT).show();

                collapsedView.setVisibility(View.VISIBLE);
                expanded_view.setVisibility(View.GONE);
                break;
        
            case R.id.open_button:
                Toast.makeText(this, "Open image is tapped", Toast.LENGTH_SHORT).show();
                Intent openAppIntent =new Intent(FloatingViewService.this,MainActivity.class);
                openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openAppIntent);
                stopSelf();
                 break;


        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                Toast.makeText(this, "Action down", Toast.LENGTH_SHORT).show();
                //we want to access the start position of widget
                startXPos = wdParams.x;
                startYPos = wdParams.y;

                //we want to access the start touching position of widget
                startTouchX=event.getRawX();
                startTouchY=event.getRawY();

                return true;

            case MotionEvent.ACTION_UP:
                Toast.makeText(this, "Action up", Toast.LENGTH_SHORT).show();
                int startToEndXDifference=(int)(event.getRawX()-startTouchX);
                int startToEndYDifference=(int)(event.getRawY()-startTouchY);

                if(startToEndXDifference<5 && startToEndYDifference<5){
                    if(isWidgetCollapsed()){

                        collapsedView.setVisibility(View.GONE);
                        expanded_view.setVisibility(View.VISIBLE);
                    }
                }
               return true;

            case MotionEvent.ACTION_MOVE:
                Toast.makeText(this, "action move", Toast.LENGTH_SHORT).show();
                wdParams.x=startXPos+(int)(event.getRawX()-startTouchX);
                wdParams.y=startYPos+(int)(event.getRawY()-startTouchY);
                windowManager.updateViewLayout(floatingView,wdParams);
                return true;
        }

          return false;
    }

    private boolean isWidgetCollapsed(){

        return collapsedView.getVisibility()==View.VISIBLE;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(floatingView!=null){

            windowManager.removeView(floatingView);
        }
    }
}
