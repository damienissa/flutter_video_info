package com.example.flutter_video_info;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import io.flutter.plugin.common.MethodChannel;
import org.json.JSONObject;
import java.io.File;
import java.lang.Exception;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Date;
import java.nio.file.Path;
import java.nio.file.Files;

/** FlutterVideoInfoPlugin */
public class FlutterVideoInfoPlugin implements FlutterPlugin, MethodCallHandler {

  private String chName = "flutter_video_info";
  public static Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(),
        "flutter_video_info");
    channel.setMethodCallHandler(new FlutterVideoInfoPlugin());
    context = flutterPluginBinding.getApplicationContext();
  }

  public static void registerWith(Registrar registrar_) {
    final MethodChannel channel = new MethodChannel(registrar_.messenger(), "flutter_video_info");
    channel.setMethodCallHandler(new FlutterVideoInfoPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getVidInfo")) {
      String path = call.argument("path");
      result.success(getVidInfo(path));
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }

  String getVidInfo(String path) {
    File file = new File(path);
    MediaMetadataRetriever mediaRetriever = new MediaMetadataRetriever();
    mediaRetriever.setDataSource(context, Uri.fromFile(file));
    Path vidPath = file.toPath();

    String date = null;
    try {
      BasicFileAttributes attr = Files.readAttributes(vidPath, BasicFileAttributes.class);
      date = attr.creationTime().toString();
    } catch (Exception e) {

    }
    String author = getData(MediaMetadataRetriever.METADATA_KEY_AUTHOR, mediaRetriever);
    String title = getData(MediaMetadataRetriever.METADATA_KEY_TITLE, mediaRetriever);
    String mimeType = getData(MediaMetadataRetriever.METADATA_KEY_MIMETYPE, mediaRetriever);
    String location = getData(MediaMetadataRetriever.METADATA_KEY_LOCATION, mediaRetriever);
    String frameRateStr = getData(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE, mediaRetriever);
    String durationStr = getData(MediaMetadataRetriever.METADATA_KEY_DURATION, mediaRetriever);
    String widthStr = getData(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH, mediaRetriever);
    String heightStr = getData(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT, mediaRetriever);
    double filesize = file.length();
    String orientation;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      orientation = getData(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION, mediaRetriever);
    } else {
      orientation = null;
    }

    mediaRetriever.release();

    JSONObject json = new JSONObject();
    try {
      json.put("path", path);
      json.put("title", title);
      json.put("mimetype", mimeType);
      json.put("author", author);
      json.put("date", date);
      json.put("width", widthStr);
      json.put("height", heightStr);
      json.put("location", location);
      json.put("framerate", frameRateStr);
      json.put("duration", durationStr);
      json.put("filesize", filesize);

      json.put("orientation", orientation);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return json.toString();
  }

  String getData(int key, MediaMetadataRetriever mediaRetriever) {
    try {
      return mediaRetriever.extractMetadata(key);
    } catch (Exception e) {
      return null;
    }
  }

}
