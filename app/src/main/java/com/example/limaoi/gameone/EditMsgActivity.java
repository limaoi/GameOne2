package com.example.limaoi.gameone;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.limaoi.gameone.adapter.PictureAdapter;
import com.example.limaoi.gameone.bean.Circle;
import com.example.limaoi.gameone.bean.Person;
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
 * Created by limaoi on 2017/9/27.
 * E-mail：autismlm20@vip.qq.com
 */

public class EditMsgActivity extends BaseActivity implements View.OnClickListener {

    private Button mAddPictureButton;
    private TextView tv_cancle;
    private TextView tv_issue;
    private EditText et_dynamic;
    private RecyclerView mRecyclerView;

    private PictureAdapter mPictureAdapter;
    private ArrayList<AlbumFile> mAlbumFiles = null;
    private List<String> dynamicPictureUrl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.initScreen(this);  //Album必须
        setContentView(R.layout.activity_edit_msg);

        MIUISetStatusBarLightMode(this.getWindow(), true);

        //初始化Bmob
        Bmob.initialize(this, "a21a5524eff971e709218fdd5420bec2");

        initViews();

        initEvents();
    }

    private void initViews() {
        mAddPictureButton = (Button) findViewById(R.id.bt_add_picture);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_issue = (TextView) findViewById(R.id.tv_issue);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        et_dynamic = (EditText) findViewById(R.id.et_dynamic);
    }

    private void initEvents() {
        mAddPictureButton.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_issue.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        Divider divider = AlbumUtils.getDivider(Color.WHITE);
        mRecyclerView.addItemDecoration(divider);

        int itemSize = (DisplayUtils.sScreenWidth - (divider.getWidth() * 4)) / 3;
        mPictureAdapter = new PictureAdapter(this, itemSize, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage(position);
            }
        });
        mRecyclerView.setAdapter(mPictureAdapter);
        et_dynamic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAlbumFiles == null) {
                    if (s.length() != 0) {
                        tv_issue.setVisibility(View.VISIBLE);
                    } else {
                        tv_issue.setVisibility(View.GONE);
                    }
                } else {
                    tv_issue.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_picture:
                Album.image(this) // 选择图片。
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
                        .selectCount(9)
                        .checkedList(mAlbumFiles)
                        .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                //接受结果
                                mAlbumFiles = result;
                                mPictureAdapter.notifyDataSetChanged(mAlbumFiles);
                                int size = mAlbumFiles.size();
                                final String[] filePaths = new String[size];
                                for (int i = 0; i < size; i++) {
                                    filePaths[i] = mAlbumFiles.get(i).getPath();
                                }

                                BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                                    @Override
                                    public void onSuccess(List<BmobFile> files, List<String> urls) {
                                        //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                                        //2、urls-上传文件的完整url地址
                                        if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                                            dynamicPictureUrl = urls;
                                            Toasty.success(EditMsgActivity.this, "上传成功", Toast.LENGTH_SHORT, true).show();
                                            /*tv_issue.setTextColor(getResources().getColor(R.color.colorBlack));*/
                                            tv_issue.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                                        //1、curIndex--表示当前第几个文件正在上传
                                        //2、curPercent--表示当前上传文件的进度值（百分比）
                                        //3、total--表示总的上传文件数
                                        //4、totalPercent--表示总的上传进度（百分比）
                                        tv_issue.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(int statuscode, String errormsg) {
                                        if (!isNetworkConnected(EditMsgActivity.this)) {
                                            Toasty.error(EditMsgActivity.this, "网络不可用", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onAlbumCancel(int requestCode) {
                                //取消操作
                            }
                        })
                        .start();
                break;
            case R.id.tv_issue:
                String dynamic = et_dynamic.getText().toString();
                if (!dynamic.isEmpty() || mAlbumFiles != null) {
                    final Circle circle = new Circle();
                    if (!dynamic.isEmpty()) {
                        circle.setDynamic(dynamic);
                    }
                    if (mAlbumFiles != null) {
                        circle.setDynamicPictureUrl(dynamicPictureUrl);
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    if (bmobUser != null) {
                        String objectId = (String) BmobUser.getObjectByKey("objectId");
                        String nickname = (String) BmobUser.getObjectByKey("nickname");
                        String picurl = (String) BmobUser.getObjectByKey("pic");
                        String userType = (String) BmobUser.getObjectByKey("userType");
                        circle.setUserId(objectId);
                        circle.setNickname(nickname);
                        circle.setLikeCount(0);
                        circle.setUserType("1");
                        circle.setCommentCount(0);
                        circle.setHeadPictureUrl(picurl);
                        circle.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null) {
                                    if (isNetworkConnected(EditMsgActivity.this)) {
                                        Log.i("bmob", "error" + e);
                                        Toasty.error(EditMsgActivity.this, "发布失败", Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.error(EditMsgActivity.this, "网络不可用", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            }
                        });
                        finish();
                    }
                }
                if (dynamic.isEmpty() && mAlbumFiles == null) {
                    Toasty.error(EditMsgActivity.this, "请说点什么吧", Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.tv_cancle:
                finish();
                break;
            default:
                break;
        }

    }


    /**
     * Preview image, to album.
     */
    private void previewImage(int position) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            Toast.makeText(this, " NO SELECT", Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newDarkBuilder(this)
                                    .title("选择图片")
                                    .build()
                    )
                    .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                            mPictureAdapter.notifyDataSetChanged(mAlbumFiles);
                        }

                        @Override
                        public void onAlbumCancel(int requestCode) {

                        }
                    })
                    .start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_image, menu);
        return true;
    }

}
