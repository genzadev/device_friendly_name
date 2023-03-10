package com.genza.device_friendly_name;

import androidx.annotation.NonNull;

import android.content.Context;
import android.provider.Settings;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * DeviceFriendlyNamePlugin
 */
public class DeviceFriendlyNamePlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context context;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "device_friendly_name");
        channel.setMethodCallHandler(new DeviceFriendlyNamePlugin());
    }

    private void onAttachedToEngine(Context applicationContext, FlutterPluginBinding flutterPluginBinding) {
        this.context = applicationContext;
        System.out.println(applicationContext);
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "device_friendly_name");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getDeviceFriendlyName")) {
            if (android.os.Build.VERSION.SDK_INT < 17) {
                return;
            }
            String friendlyName = Settings.Secure.getString(context.getContentResolver(), "bluetooth_name");
            if ((friendlyName == null) || friendlyName.isEmpty()) {
                friendlyName = Settings.Global.getString(context.getContentResolver(), "device_name");
            }
            result.success(friendlyName);
        } else if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
