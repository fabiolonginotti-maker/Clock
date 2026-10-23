package com.example.batterydisneyoverlay;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class BatteryOverlayService extends Service {

    private WindowManager windowManager;
    private View overlayView;
    private BatteryReceiver batteryReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        // Crea l'overlay view
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_battery, null);
        
        // Imposta i parametri della finestra
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : 
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | 
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT
        );
        
        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 100;
        params.y = 100;
        
        // Aggiungi la view alla finestra
        windowManager.addView(overlayView, params);
        
        // Registra il receiver per il livello della batteria
        batteryReceiver = new BatteryReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
        
        // Aggiorna subito il livello della batteria
        updateBatteryLevel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        if (overlayView != null && windowManager != null) {
            windowManager.removeView(overlayView);
        }
        
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryIntent != null) {
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int batteryPercentage = (level * 100) / scale;
            
            TextView batteryText = overlayView.findViewById(R.id.batteryLevel);
            batteryText.setText(batteryPercentage + "%");
            
            // Cambia il personaggio Disney in base al livello della batteria
            ImageView disneyImage = overlayView.findViewById(R.id.disneyCharacter);
            if (batteryPercentage <= 20) {
                // Batteria bassa - Mickey triste
                disneyImage.setImageResource(R.drawable.mickey_mouse_sad);
            } else if (batteryPercentage <= 50) {
                // Batteria media - Mickey neutro
                disneyImage.setImageResource(R.drawable.mickey_mouse);
            } else {
                // Batteria alta - Mickey felice
                disneyImage.setImageResource(R.drawable.mickey_mouse_happy);
            }
        }
    }

    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryLevel();
        }
    }
}
