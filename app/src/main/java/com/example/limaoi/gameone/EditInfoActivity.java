package com.example.limaoi.gameone;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.example.limaoi.gameone.EditActivity.EmailEditActivity;
import com.example.limaoi.gameone.EditActivity.NickNameEditActivity;
import com.example.limaoi.gameone.EditActivity.PhoneEditActivity;
import com.example.limaoi.gameone.EditActivity.SignatureEditActivity;
import com.example.limaoi.gameone.bean.Person;
import com.example.limaoi.gameone.utils.AddressPickTask;


import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.R.attr.type;
import static com.example.limaoi.gameone.widget.ClipViewLayout.getRealFilePathFromUri;

/**
 * Created by limaoi on 2017/7/16.
 * E-mail：autismlm20@vip.qq.com
 */

public class EditInfoActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout ry_head_photo;
    private RelativeLayout ry_nickname;
    private RelativeLayout ry_signature;
    private RelativeLayout ry_sex;
    private RelativeLayout ry_address;
    private RelativeLayout ry_email;
    private RelativeLayout ry_mobilePhoneNumber;
    private ImageView iv_back;

    private CircleImageView circleImageView_head_photo;
    private TextView tv_nickname_value;
    private TextView tv_signature_value;
    private TextView tv_sex_value;
    private TextView tv_address_value;
    private TextView tv_email_value;
    private TextView tv_mobilePhoneNumber_value;

    private ImageView iv_verify_email;
    private ImageView iv_verify_phone;


    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //调用照相机返回图片文件
    private File tempFile;


    @Override
    protected void onStart() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            String nickname = (String) BmobUser.getObjectByKey("nickname");
            String signature = (String) BmobUser.getObjectByKey("signature");
            String sex = (String) BmobUser.getObjectByKey("sex");
            String address = (String) BmobUser.getObjectByKey("address");
            String email = (String) BmobUser.getObjectByKey("email");
            String mobilePhoneNumber = (String) BmobUser.getObjectByKey("mobilePhoneNumber");
            String picurl = (String) BmobUser.getObjectByKey("pic");
            Boolean emailVerified = (Boolean) BmobUser.getObjectByKey("emailVerified");
            Boolean mobilePhoneNumberVerified = (Boolean) BmobUser.getObjectByKey("mobilePhoneNumberVerified");
            tv_nickname_value.setText(nickname);
            tv_signature_value.setText(signature);
            tv_sex_value.setText(sex);
            tv_address_value.setText(address);
            tv_email_value.setText(email);
            new getImageCacheAsyncTask(EditInfoActivity.this).execute(picurl);
            Glide.with(getApplicationContext()).load(picurl).into(circleImageView_head_photo);
            if (email == null) {
                tv_email_value.setText("未填写");
            } else {
                tv_email_value.setText(email);
            }
            if (mobilePhoneNumber == null) {
                tv_mobilePhoneNumber_value.setText("未填写");
            } else {
                tv_mobilePhoneNumber_value.setText(mobilePhoneNumber);
            }
            if (emailVerified != null) {
                if (emailVerified) {
                    iv_verify_email.setVisibility(View.VISIBLE);
                } else {
                    iv_verify_email.setVisibility(View.GONE);
                }
            }
            if (mobilePhoneNumberVerified != null) {
                if (mobilePhoneNumberVerified) {
                    iv_verify_phone.setVisibility(View.VISIBLE);
                } else {
                    iv_verify_phone.setVisibility(View.GONE);
                }
            }
        } else {

        }

        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        MIUISetStatusBarLightMode(this.getWindow(), true);

        initViews();

        initEvents();

    }


    private void initViews() {
        ry_nickname = (RelativeLayout) findViewById(R.id.ry_nickname);
        ry_sex = (RelativeLayout) findViewById(R.id.ry_sex);
        ry_address = (RelativeLayout) findViewById(R.id.ry_address);
        ry_signature = (RelativeLayout) findViewById(R.id.ry_signature);
        ry_email = (RelativeLayout) findViewById(R.id.ry_email);
        ry_mobilePhoneNumber = (RelativeLayout) findViewById(R.id.ry_mobilePhoneNumber);
        ry_head_photo = (RelativeLayout) findViewById(R.id.ry_head_photo);

        tv_nickname_value = (TextView) findViewById(R.id.tv_nickname_value);
        tv_signature_value = (TextView) findViewById(R.id.tv_signature_value);
        tv_sex_value = (TextView) findViewById(R.id.tv_sex_value);
        tv_address_value = (TextView) findViewById(R.id.tv_address_value);
        tv_email_value = (TextView) findViewById(R.id.tv_email_value);
        tv_mobilePhoneNumber_value = (TextView) findViewById(R.id.tv_mobilePhoneNumber_value);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_verify_email = (ImageView) findViewById(R.id.iv_verify_email);
        iv_verify_phone = (ImageView) findViewById(R.id.iv_verify_phone);
        circleImageView_head_photo = (CircleImageView) findViewById(R.id.circleImageView_head_photo);
    }


    private void initEvents() {
        ry_nickname.setOnClickListener(this);
        ry_sex.setOnClickListener(this);
        ry_address.setOnClickListener(this);
        ry_signature.setOnClickListener(this);
        ry_email.setOnClickListener(this);
        ry_mobilePhoneNumber.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ry_head_photo.setOnClickListener(this);
        circleImageView_head_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ry_head_photo:
                uploadHeadImage();
                break;
            case R.id.ry_nickname:
                Intent intent = new Intent(EditInfoActivity.this, NickNameEditActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.ry_sex:
                onOptionPicker();
                break;
            case R.id.ry_address:
                onAddressPicker();
                break;
            case R.id.ry_signature:
                Intent intent2 = new Intent(EditInfoActivity.this, SignatureEditActivity.class);
                startActivityForResult(intent2, 2);
                break;
            case R.id.ry_email:
                Intent intent3 = new Intent(EditInfoActivity.this, EmailEditActivity.class);
                startActivityForResult(intent3, 3);
                break;
            case R.id.ry_mobilePhoneNumber:
                Intent intent4 = new Intent(EditInfoActivity.this, PhoneEditActivity.class);
                startActivityForResult(intent4, 4);
                break;
            case R.id.circleImageView_head_photo:
                uploadHeadImage();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    public void onOptionPicker() {
        OptionPicker picker = new OptionPicker(this, new String[]{
                "男", "女"
        });
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setDividerColor(Color.parseColor("#eeeeee"));
        picker.setShadowColor(Color.WHITE, 60);
        picker.setSelectedIndex(2);
        picker.setCycleDisable(true);
        picker.setTopLineColor(Color.parseColor("#FFFFFF"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setTextColor(Color.parseColor("#000000"));
        picker.setTextSize(15);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                tv_sex_value.setText(item);
                Person newUser = new Person();
                newUser.setSex(item);
                BmobUser bmobUser = BmobUser.getCurrentUser();
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toasty.success(EditInfoActivity.this, "更新信息成功", Toast.LENGTH_SHORT, true).show();
                        } else {

                            Toasty.error(EditInfoActivity.this, "更新信息失败", Toast.LENGTH_SHORT, true).show();
                        }

                    }
                });
            }
        });
        picker.show();
    }


    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                Toasty.error(EditInfoActivity.this, "数据初始化失败", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                tv_address_value.setText(province.getAreaName() + " " + city.getAreaName());
                Person newUser = new Person();
                newUser.setAddress(province.getAreaName() + " " + city.getAreaName());
                BmobUser bmobUser = BmobUser.getCurrentUser();
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toasty.success(EditInfoActivity.this, "更新信息成功", Toast.LENGTH_SHORT, true).show();
                        } else {

                            Toasty.error(EditInfoActivity.this, "更新信息失败", Toast.LENGTH_SHORT, true).show();
                        }

                    }
                });
            }
        });
        task.execute("广东", "佛山");
    }


    private void uploadHeadImage() {
        View view = LayoutInflater.from(EditInfoActivity.this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(EditInfoActivity.this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = EditInfoActivity.this.getWindow().getAttributes();
        params.alpha = 0.5f;
        EditInfoActivity.this.getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                EditInfoActivity.this.getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(EditInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(EditInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(EditInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        //创建拍照存储的图片文件
        tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(EditInfoActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    Log.d("evan", "**********camera uri*******" + Uri.fromFile(tempFile).toString());
                    Log.d("evan", "**********camera path*******" + getRealFilePathFromUri(EditInfoActivity.this, Uri.fromFile(tempFile)));
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    Log.d("evan", "**********pick path*******" + getRealFilePathFromUri(EditInfoActivity.this, uri));
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    final BmobFile pic = new BmobFile(new File(cropImagePath));
                    pic.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                BmobUser bmobUser = BmobUser.getCurrentUser();
                                Person person = new Person();
                                person.setPic(pic.getFileUrl());
                                person.update(bmobUser.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        new getImageCacheAsyncTask(getApplicationContext()).execute(pic.getFileUrl());
                                    }
                                });
                            } else {
                                if (isNetworkConnected(EditInfoActivity.this)) {
                                    Log.i("bmob", "error" + e);
                                    Toasty.error(EditInfoActivity.this, "上传失败", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    Toasty.error(EditInfoActivity.this, "网络不可用", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }

                    });
                    Log.i("bmob", "pic:" + pic);
                }
                break;
            case 1:
                if (requestCode == RESULT_OK) {
                    String nickname_data = intent.getStringExtra("nickname_return");
                    tv_nickname_value.setText(nickname_data);
                }
                break;
            case 2:
                if (requestCode == RESULT_OK) {
                    String signature_data = intent.getStringExtra("signature_return");
                    tv_signature_value.setText(signature_data);
                }
                break;
            case 3:
                if (requestCode == RESULT_OK) {
                    String email_data = intent.getStringExtra("email_return");
                    tv_email_value.setText(email_data);
                }
                break;
            case 4:
                if (requestCode == RESULT_OK) {
                    String phone_data = intent.getStringExtra("phone_return");
                    tv_mobilePhoneNumber_value.setText(phone_data);
                }
                break;
        }
    }

    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(EditInfoActivity.this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    public static class PathGetter {

        public static String getPath(final Context context, final Uri uri) {

            final boolean isUpToKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isUpToKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        public static String getDataColumn(Context context, Uri uri, String selection,
                                           String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {
                    column
            };

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(column_index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

    }

    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public getImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl = params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            Log.e("path", path);
            Bitmap bmp = BitmapFactory.decodeFile(path);
            circleImageView_head_photo.setImageBitmap(bmp);
        }
    }

}



