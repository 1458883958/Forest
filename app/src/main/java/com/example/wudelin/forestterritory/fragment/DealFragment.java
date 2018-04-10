package com.example.wudelin.forestterritory.fragment;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.wudelin.forestterritory.R;
import com.example.wudelin.forestterritory.utils.Logger;
import com.example.wudelin.forestterritory.utils.ToastUtil;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 项目名：  ForestTerritory
 * 包名：    com.example.wudelin.forestterritory.fragment
 * 创建者：   wdl
 * 创建时间： 2018/4/7 21:14
 * 描述：    处理图片
 */

public class DealFragment extends Fragment implements View.OnClickListener {
    private ImageView ivAddPic;
    private Button btnGray;
    private Button btnBinaryzation;
    private static final int PERMISSION_REQUEST = 101;
    private static final int REQUEST_CAMERA = 10;
    private static final int REQUEST_ALBUM = 11;
    private Uri imageUri;
    private PopupWindow popupWindow;
    //判断是否从相册或者拍照获取到图片
    private boolean flag = false;
    static {
            boolean load = OpenCVLoader.initDebug();
            if(load) {
                Logger.i( "Open CV Libraries loaded...");
            }else{
                Logger.i( "Open CV");
            }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal,null);

        findView(view);
        return view;
    }

    private void findView(View view) {
        ivAddPic = view.findViewById(R.id.iv_add_pic);
        ivAddPic.setOnClickListener(this);
        btnGray = view.findViewById(R.id.pic_gray);
        btnGray.setOnClickListener(this);
        btnBinaryzation = view.findViewById(R.id.pic_binaryzation);
        btnBinaryzation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_pic:
                //判断权限
                checkPermission();
                break;
            case R.id.pic_gray:
                //灰度化
                if(flag){
                    gray();
                }else {
                    ToastUtil.showByStr(getActivity(),"未选择图片！");
                }
                break;
            case R.id.pic_binaryzation:
                //二值化
                if(flag){
                    changeBitmap();
                }else {
                    ToastUtil.showByStr(getActivity(),"未选择图片！");
                }
                break;
            case R.id.select_camera:
                //拍照选取
                toCamera();
                popupWindow.dismiss();
                break;
            case R.id.select_album:
                //相册选取
                toAlbum();
                popupWindow.dismiss();
                break;
            case R.id.btn_cancel:
                popupWindow.dismiss();
                break;
            default:
        }
    }


    //二值化
/*
        名称                  常量
    二值阈值化       Imgproc.THRESH_BINARY
    阈值化到零       Imgproc.THRESH_TOZERO
    截断阈值化       Imgproc.THRESH_TRUNC
    反转二值阈值化 Imgproc.THRESH_BINARY_INV
    反转阈值化到零 Imgproc.THRESH_TOZERO_INV
*/
    public void changeBitmap() {
        Bitmap bitmap = ((BitmapDrawable)ivAddPic.getDrawable()).getBitmap();
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Utils.bitmapToMat(bitmap,rgbMat);
        Imgproc.threshold(rgbMat,grayMat,100,255,Imgproc.THRESH_BINARY);
        Utils.matToBitmap(grayMat,bitmap);
        ivAddPic.setImageBitmap(bitmap);
    }
    //灰度化
    private void gray() {
        Bitmap bitmap = ((BitmapDrawable)ivAddPic.getDrawable()).getBitmap();
        Bitmap graybm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Mat src = new Mat();
        Mat dst = new Mat();
        //获取lena彩色图像所对应的像素数据
        Utils.bitmapToMat(bitmap,src);
        //将彩色图像数据转换为灰度图像数据并存储到grayMat中
        Imgproc.cvtColor(src,dst,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst,graybm);
        ivAddPic.setImageBitmap(graybm);
    }
    private void toAlbum() {
        //设置action
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_ALBUM);
    }

    private void toCamera() {
        File outputImg = new File(Environment.getExternalStorageDirectory(),
                "output.jpg");
        //创建
        try {
            if(outputImg.exists()){
                outputImg.delete();
            }
            outputImg.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT > 23) {
            imageUri = FileProvider
                    .getUriForFile(getActivity(),
                            getActivity().getPackageName()+".fileProvider",outputImg);
        }else{
            imageUri = Uri.fromFile(outputImg);
        }
        //拍照意图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //访问权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    //判断权限
    private void checkPermission() {
        List<String> needList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission
                (getActivity(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            needList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission
                (getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            needList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!needList.isEmpty()){
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }else{
            showPopWindow();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_CAMERA:
                    try {
                        Bitmap bitmap = BitmapFactory
                                .decodeStream(getActivity().getContentResolver()
                                .openInputStream(imageUri));
                        ivAddPic.setImageBitmap(bitmap);
                        flag = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_ALBUM:
                    //4.4后
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                    flag = true;
                    break;
                default:
            }
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    //4.4以后
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(),uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String Path=null;
        Cursor cursor=getActivity()
                .getContentResolver()
                .query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                Path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return Path;
    }
    private void displayImage(String Path){
        Bitmap bm=BitmapFactory.decodeFile(Path);
        ivAddPic.setImageBitmap(bm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST){
            if(grantResults.length>0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED) {
                        ToastUtil.showByStr(getActivity(),"权限未授予,不能正常工作");
                        return;
                    }
                }
                //
                showPopWindow();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showPopWindow() {
        //设置pop_view
        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.popwindow_layout,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        popupWindow.setFocusable(true);// 点击空白处时，隐藏掉pop窗口
        popupWindow.setContentView(view);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        //设置各个控件的点击响应
        Button btnSelectCamera = view.findViewById(R.id.select_camera);
        btnSelectCamera.setOnClickListener(this);
        Button btnSelectAlbum = view.findViewById(R.id.select_album);
        btnSelectAlbum.setOnClickListener(this);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        //显示PopupWindow的根布局
        View rootview = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.fragment_deal, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }
}
