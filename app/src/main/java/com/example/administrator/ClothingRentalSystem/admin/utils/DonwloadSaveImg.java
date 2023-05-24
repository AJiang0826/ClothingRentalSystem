package com.example.administrator.ClothingRentalSystem.admin.utils;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import android.os.Handler;

/**
 * 本类是实现从服务器下载图片到虚拟机本地的工具类
 * 在主类中进行调用
 **/
public class DonwloadSaveImg {
    private String filePath;
    private Bitmap mBitmap;

    /**
     * 可直接在其它类中调用此方法，完成从网络上下载图片到本地
     * 参数：网络地址 利http://192.168.64.114/test
     **/
    public void donwloadImg(String filePaths) {
        filePath = filePaths;
        System.out.println("申请下载地址："+filePath);
        new Thread(saveFileRunnable).start();
    }

    /**
     * 本方法是返回Runnable类，网络上下载图片使用线程处理
     **/
    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (!TextUtils.isEmpty(filePath)) { //网络图片
                    // 对资源链接
                    URL url = new URL(filePath);
                    //打开输入流
                    InputStream inputStream = url.openStream();
                    //对网上资源进行下载转换位图图片
                    mBitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
                saveFile(mBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 保存图片到/sdcard/Pictures/文件夹底下
     */
    public void saveFile(Bitmap bm ) throws IOException {
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath());
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = UUID.randomUUID().toString() + ".png";
//        System.out.println("filename="+fileName);
//        System.out.println("dirFile="+Environment.getExternalStorageDirectory().getPath());
        File myCaptureFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/"+ fileName);
//        System.out.println("相册路径是："+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
        bos.flush();
        bos.close();
    }
}