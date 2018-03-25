package com.example.limaoi.gameone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limaoi.gameone.adapter.VideoAdapter;
import com.example.limaoi.gameone.adapter.VideoPicAdapter;
import com.example.limaoi.gameone.bean.Person;
import com.example.limaoi.gameone.bean.Video;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumListener;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.widget.divider.Divider;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import es.dmoral.toasty.Toasty;

/**
 * Created by limaoi on 2017/12/11.
 * E-mail：autismlm20@vip.qq.com
 */

public class AddVideoActivity extends BaseActivity implements View.OnClickListener {

    private Button mAddVideoButton;
    private Button mAddVideoPicButton;
    private TextView tv_cancle;
    private TextView tv_issue;
    private EditText et_title;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewPic;

    private ArrayList<AlbumFile> mAlbumFiles = null;
    private ArrayList<AlbumFile> mPicAlbumFiles = null;
    private VideoAdapter mVideoAdapter;
    private VideoPicAdapter mVideoPicAdapter;
    private String videoUrl;
    private String videoPicUrl;
    private String items[] = {"英雄联盟", "王者荣耀", "绝地求生", "职业联赛", "主播糗事", "其他"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.initScreen(this);  //Album必须
        setContentView(R.layout.activity_add_video);

        MIUISetStatusBarLightMode(this.getWindow(), true);

        //初始化Bmob
        Bmob.initialize(this, "a21a5524eff971e709218fdd5420bec2");

        initViews();

        initEvents();

    }

    private void initViews() {
        mAddVideoButton = (Button) findViewById(R.id.bt_add_video);
        mAddVideoPicButton = (Button) findViewById(R.id.bt_add_videoPic);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_issue = (TextView) findViewById(R.id.tv_issue);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerViewPic = (RecyclerView) findViewById(R.id.recycler_view_pic);
        et_title = (EditText) findViewById(R.id.et_title);
    }

    private void initEvents() {
        mAddVideoButton.setOnClickListener(this);
        mAddVideoPicButton.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_issue.setOnClickListener(this);

        mRecyclerViewPic.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        Divider divider = AlbumUtils.getDivider(Color.WHITE);
        mRecyclerViewPic.addItemDecoration(divider);
        mRecyclerView.addItemDecoration(divider);

        int itemSize = (DisplayUtils.sScreenWidth - (divider.getWidth() * 4)) / 3;
        mVideoAdapter = new VideoAdapter(this, itemSize, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewVideo(position);
            }
        });
        mVideoPicAdapter = new VideoPicAdapter(this, itemSize, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage(position);
            }
        });
        mRecyclerView.setAdapter(mVideoAdapter);
        mRecyclerViewPic.setAdapter(mVideoPicAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_videoPic:
                Album.image(this)
                        .multipleChoice()
                        .widget(Widget.newLightBuilder(this)
                                .title("选择图片")
                                .statusBarColor(getResources().getColor(R.color.colorPrimaryDark)) // 状态栏颜色。
                                .toolBarColor(getResources().getColor(R.color.colorPrimaryDark)) // Toolbar颜色。
                                .build()
                        )
                        .requestCode(200)
                        .camera(true)
                        .columnCount(3)
                        .selectCount(1)
                        .checkedList(mPicAlbumFiles)
                        .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                mPicAlbumFiles = result;
                                mVideoPicAdapter.notifyDataSetChanged(mPicAlbumFiles);
                                if (mPicAlbumFiles.size() > 0) {
                                    mAddVideoPicButton.setVisibility(View.GONE);
                                } else {
                                    mAddVideoPicButton.setVisibility(View.VISIBLE);
                                }
                                final String[] filePaths = new String[1];
                                filePaths[0] = mPicAlbumFiles.get(0).getPath();
                                final ProgressDialog dialog = new ProgressDialog(AddVideoActivity.this);
                                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                dialog.setMessage("上传中");
                                dialog.setMax(100);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                                    @Override
                                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                                        if (list.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                                            videoPicUrl = list1.get(0);
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onProgress(int i, int i1, int i2, int i3) {
                                        dialog.setProgress(i3);
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        if (!isNetworkConnected(AddVideoActivity.this)) {
                                            Toasty.error(AddVideoActivity.this, "网络不可用", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onAlbumCancel(int requestCode) {

                            }
                        })
                        .start();
                break;
            case R.id.bt_add_video:
                Album.video(this)
                        .multipleChoice()
                        .widget(Widget.newLightBuilder(this)
                                .title("选择视频")
                                .statusBarColor(getResources().getColor(R.color.colorPrimaryDark)) // 状态栏颜色。
                                .toolBarColor(getResources().getColor(R.color.colorPrimaryDark)) // Toolbar颜色。
                                .buttonStyle(Widget.ButtonStyle.newLightBuilder(this) // 同Widget的Builder模式。
                                        .setButtonSelector(getResources().getColor(R.color.colorPrimaryDark), Color.WHITE) // 按钮的选择器。
                                        .build())
                                .build()
                        )
                        .requestCode(201)
                        .camera(true)
                        .columnCount(3)
                        .selectCount(1)
                        .checkedList(mAlbumFiles)
                        .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                mAlbumFiles = result;
                                mVideoAdapter.notifyDataSetChanged(mAlbumFiles);
                                if (mAlbumFiles.size() > 0) {
                                    mAddVideoButton.setVisibility(View.GONE);
                                } else {
                                    mAddVideoButton.setVisibility(View.VISIBLE);
                                }
                                final String[] filePaths = new String[1];
                                filePaths[0] = mAlbumFiles.get(0).getPath();
                                final ProgressDialog dialog = new ProgressDialog(AddVideoActivity.this);
                                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                dialog.setMessage("上传中");
                                dialog.setMax(100);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                                    @Override
                                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                                        if (list.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                                            videoUrl = list1.get(0);
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onProgress(int i, int i1, int i2, int i3) {
                                        dialog.setProgress(i1);
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        if (!isNetworkConnected(AddVideoActivity.this)) {
                                            Toasty.error(AddVideoActivity.this, "网络不可用", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onAlbumCancel(int requestCode) {

                            }
                        })
                        .start();
                break;
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.tv_issue:
                String title = et_title.getText().toString();
                if (!title.isEmpty() && mAlbumFiles != null && mPicAlbumFiles != null) {
                    final Video video = new Video();
                    video.setTitle(title);
                    video.setVideoUrl(videoUrl);
                    video.setVideoPicUrl(videoPicUrl);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    if (bmobUser != null) {
                        String objectId = (String) BmobUser.getObjectByKey("objectId");
                        String nickname = (String) BmobUser.getObjectByKey("nickname");
                        String picurl = (String) BmobUser.getObjectByKey("pic");
                        String userType = (String) BmobUser.getObjectByKey("userType");
                        video.setNickname(nickname);
                        video.setUserId(objectId);
                        video.setUserType(userType);
                        video.setCommentCount(0);
                        video.setLikeCount(0);
                        video.setPlayCount(0);
                        video.setHeadPictureUrl(picurl);
                        AlertDialog dialog = new AlertDialog.Builder(AddVideoActivity.this)
                                .setIcon(R.mipmap.ic_label)
                                .setTitle("选择分类")
                                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        video.setLabel(items[which]);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        video.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e != null) {
                                                    if (isNetworkConnected(AddVideoActivity.this)) {
                                                        Log.i("bmob", "error" + e);
                                                        Toasty.error(AddVideoActivity.this, "发布失败", Toast.LENGTH_SHORT, true).show();
                                                    } else {
                                                        Toasty.error(AddVideoActivity.this, "网络不可用", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                }
                                            }
                                        });
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).create();
                        dialog.show();

                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);

                    }
                }
                if (title.isEmpty() || mAlbumFiles == null || mPicAlbumFiles == null) {
                    Toasty.error(AddVideoActivity.this, "内容不能为空", Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_video, menu);
        return true;
    }

    private void previewVideo(int position) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            Toast.makeText(this, " NO SELECT", Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newDarkBuilder(this)
                                    .title("选择视频")
                                    .build()
                    )
                    .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                            mVideoAdapter.notifyDataSetChanged(mAlbumFiles);
                            if (mAlbumFiles.size() > 0) {
                                mAddVideoButton.setVisibility(View.GONE);
                            } else {
                                mAddVideoButton.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onAlbumCancel(int requestCode) {

                        }
                    })
                    .start();
        }
    }

    private void previewImage(int position) {
        if (mPicAlbumFiles == null || mPicAlbumFiles.size() == 0) {
            Toast.makeText(this, " NO SELECT", Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mPicAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newDarkBuilder(this)
                                    .title("选择图片")
                                    .build()
                    )
                    .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                            mPicAlbumFiles = result;
                            mVideoPicAdapter.notifyDataSetChanged(mPicAlbumFiles);
                            if (mPicAlbumFiles.size() > 0) {
                                mAddVideoPicButton.setVisibility(View.GONE);
                            } else {
                                mAddVideoPicButton.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onAlbumCancel(int requestCode) {

                        }
                    })
                    .start();
        }
    }
}
