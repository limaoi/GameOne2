package com.example.limaoi.gameone.Fragment;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.limaoi.gameone.BuildConfig;
import com.example.limaoi.gameone.ClipImageActivity;
import com.example.limaoi.gameone.LoginActivity;
import com.example.limaoi.gameone.R;
import com.example.limaoi.gameone.SettingActivity;
import com.example.limaoi.gameone.adapter.MeAdapter;
import com.example.limaoi.gameone.bean.Person;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.R.attr.type;
import static android.app.Activity.RESULT_OK;
import static cn.bmob.v3.Bmob.getApplicationContext;
import static com.example.limaoi.gameone.widget.ClipViewLayout.getRealFilePathFromUri;

/**
 * Created by limaoi on 2017/6/18.
 * E-mail：autismlm20@vip.qq.com
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private NestedScrollView mNestedScrollView;
    private List<String> mTitles;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MeAdapter mMeAdapter;
    private AppBarLayout mAppBarLayout;
    private TextView tv_title;
    private TextView tv_username;
    private TextView tv_signature;
    private TextView tv_hint_login;
    private TextView tv_certified;
    private ImageView iv_more;
    private ImageView iv_v2;
    private ImageView iv_v3;
    private ImageView iv_v4;
    private ImageView iv_v5;
    private CircleImageView circleImageView_user;
    private CircleImageView circleImageView_blank;

    public static final String[] tabTitle = new String[]{"个人资料", "动态", "收藏"};

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
    public void onStart() {

        mTitles = new ArrayList<>();
        for (int i = 0; i < tabTitle.length; i++) {
            mTitles.add(tabTitle[i]);
        }

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(MeTabLayoutFragment.newInstance(1));
        fragments.add(DynamicFragment.newInstance(1));
        fragments.add(CollectionFragment.newInstance(1));

        mMeAdapter = new MeAdapter(this.getChildFragmentManager(), fragments, mTitles);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mMeAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //设置可以滑动
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);


        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            circleImageView_user.setVisibility(View.VISIBLE);
            circleImageView_blank.setVisibility(View.GONE);
            tv_username.setVisibility(View.VISIBLE);
            tv_hint_login.setVisibility(View.GONE);
            String nickname = (String) BmobUser.getObjectByKey("nickname");
            String signature = (String) BmobUser.getObjectByKey("signature");
            String certified = (String) BmobUser.getObjectByKey("certified");
            tv_username.setText(nickname);
            tv_title.setText(nickname);
            String userType = (String) BmobUser.getObjectByKey("userType");
            if (userType.equals("2")) {
                iv_v2.setVisibility(View.VISIBLE);
                iv_v3.setVisibility(View.INVISIBLE);
                iv_v4.setVisibility(View.INVISIBLE);
                iv_v5.setVisibility(View.INVISIBLE);
                tv_certified.setVisibility(View.VISIBLE);
                tv_certified.setText("认证：" + certified);
                tv_signature.setVisibility(View.GONE);
            } else if (userType.equals("3")) {
                iv_v2.setVisibility(View.INVISIBLE);
                iv_v3.setVisibility(View.VISIBLE);
                iv_v4.setVisibility(View.INVISIBLE);
                iv_v5.setVisibility(View.INVISIBLE);
                tv_certified.setVisibility(View.VISIBLE);
                tv_certified.setText("认证：" + certified);
                tv_signature.setVisibility(View.GONE);
            } else if (userType.equals("4")) {
                iv_v2.setVisibility(View.INVISIBLE);
                iv_v3.setVisibility(View.INVISIBLE);
                iv_v4.setVisibility(View.VISIBLE);
                iv_v5.setVisibility(View.INVISIBLE);
                tv_certified.setVisibility(View.VISIBLE);
                tv_certified.setText("认证：" + certified);
                tv_signature.setVisibility(View.GONE);
            } else if (userType.equals("5")) {
                iv_v2.setVisibility(View.INVISIBLE);
                iv_v3.setVisibility(View.INVISIBLE);
                iv_v4.setVisibility(View.INVISIBLE);
                iv_v5.setVisibility(View.VISIBLE);
                tv_certified.setVisibility(View.VISIBLE);
                tv_certified.setText("认证：" + certified);
                tv_signature.setVisibility(View.GONE);
            } else if (userType.equals("1")) {
                tv_signature.setVisibility(View.VISIBLE);
                tv_signature.setText("个性签名：" + signature);
                tv_certified.setVisibility(View.GONE);
            }
            String picurl = (String) BmobUser.getObjectByKey("pic");
            if (picurl != null) {
                new getImageCacheAsyncTask(getActivity()).execute(picurl);
                Glide.with(getApplicationContext()).load(picurl).into(circleImageView_user);
            }
        } else {
            circleImageView_user.setVisibility(View.GONE);
            circleImageView_blank.setVisibility(View.VISIBLE);
            tv_username.setVisibility(View.GONE);
            tv_signature.setVisibility(View.GONE);
            tv_hint_login.setVisibility(View.VISIBLE);
            String title = tv_hint_login.getText().toString();
            tv_title.setText(title);
        }
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MIUISetStatusBarLightMode(getActivity().getWindow(), false);

        //初始化Bmob
        Bmob.initialize(getActivity(), "a21a5524eff971e709218fdd5420bec2");


        initViews(view);

        initEvents();


        return view;
    }

    private void initViews(View view) {

        mViewPager = (ViewPager) view.findViewById(R.id.vp_viewPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tl_tabs);
        mNestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        mToolbar = (Toolbar) view.findViewById(R.id.tb_toolbar);
        mAppBarLayout = (AppBarLayout) view.findViewById(R.id.ap_appBar);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_username = (TextView) view.findViewById(R.id.tv_username);
        tv_signature = (TextView) view.findViewById(R.id.tv_signature);
        tv_hint_login = (TextView) view.findViewById(R.id.tv_hint_login);
        tv_certified = (TextView) view.findViewById(R.id.tv_certified);
        iv_more = (ImageView) view.findViewById(R.id.iv_more);
        circleImageView_user = (CircleImageView) view.findViewById(R.id.circleImageView_user);
        circleImageView_blank = (CircleImageView) view.findViewById(R.id.circleImageView_blank);
        iv_v2 = (ImageView) view.findViewById(R.id.iv_v2);
        iv_v3 = (ImageView) view.findViewById(R.id.iv_v3);
        iv_v4 = (ImageView) view.findViewById(R.id.iv_v4);
        iv_v5 = (ImageView) view.findViewById(R.id.iv_v5);

        circleImageView_user.setOnClickListener(this);
        circleImageView_blank.setOnClickListener(this);
        tv_hint_login.setOnClickListener(this);
        iv_more.setOnClickListener(this);
    }

    private void initEvents() {

        mToolbar.setTitle("");

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRangle = appBarLayout.getTotalScrollRange();
                //初始verticalOffset为0，不能参与计算。
                if (verticalOffset == 0) {
                    tv_title.setAlpha(0.0f);
                    MIUISetStatusBarLightMode(getActivity().getWindow(), false);
                    iv_more.setImageResource(R.mipmap.ic_more);
                } else {
                    //保留一位小数
                    float alpha = Math.abs(Math.round(1.0f * verticalOffset / scrollRangle) * 10) / 10;
                    tv_title.setAlpha(alpha);
                    tv_title.setTextColor(Color.parseColor("#333333"));
                    MIUISetStatusBarLightMode(getActivity().getWindow(), true);
                    iv_more.setImageResource(R.mipmap.ic_more_fill);
                }
            }
        });


        //设置 NestedScrollView 的内容是否拉伸填充整个视图，
        //这个设置是必须的，否者我们在里面设置的ViewPager会不可见
        mNestedScrollView.setFillViewport(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circleImageView_blank:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.circleImageView_user:
                uploadHeadImage();
                break;
            case R.id.tv_hint_login:
                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent2);
                break;
            case R.id.iv_more:
                Intent intent3 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    private void uploadHeadImage() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
            Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
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
                    Log.d("evan", "**********camera path*******" + getRealFilePathFromUri(getActivity(), Uri.fromFile(tempFile)));
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    Log.d("evan", "**********pick path*******" + getRealFilePathFromUri(getActivity(), uri));
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
                    //此处后面可以将bitMap转为二进制上传后台网络
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
                                if (isNetworkConnected(getActivity())) {
                                    Log.i("bmob", "error" + e);
                                    Toasty.error(getActivity(), "上传失败", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    Toasty.error(getActivity(), "网络不可用", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }

                    });
                    Log.i("bmob", "pic:" + pic);
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
        intent.setClass(getActivity(), ClipImageActivity.class);
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
            circleImageView_user.setImageBitmap(bmp);
        }
    }
}
