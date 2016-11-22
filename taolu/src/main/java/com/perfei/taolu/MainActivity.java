package com.perfei.taolu;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.facepp.error.FaceppParseException;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.maxleap.MLUser;
import com.perfei.taolu.utils.BaseActivity;
import com.perfei.taolu.utils.CircleImageView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final int PICK_CODE = 0x110;
    private String mCurrentPhotosStr;
    private Bitmap mBitmap;
    private Paint mPaint;
    private Menu mMenu;
    private boolean isNewImage;

    private ImageView mPhoto;
    private TextView mTint,mName;
    private CircleImageView mCircleImageView;
    private View mWaiting;

    private String[] mData = {"我的收藏", "个人中心"};

    private ListView mListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private IWXAPI api;//微信的api
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //初始化控件
        initView();
        //初始化侧栏
        initEvent();
        //初始化mBitmap
        mBitmap = decodeResource(getResources(), R.drawable.we);
        isNewImage = true;
        mPaint = new Paint();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initEvent() {
        //初始化微信api
        api = WXAPIFactory.createWXAPI(mContext,Constant.WX_APP_ID);
        //将app_id注册到微信
        api.registerApp(Constant.WX_APP_ID);
        //listview设置adapter和点击事件
        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mData));
        mListView.setOnItemClickListener(this);
        //设置头像
        Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/TAOLU/head.jpg");
        mCircleImageView.setImageBitmap(bitmap);
        //ToolBar
        setUpToolbar();
        mToolbar.setTitle("TAOLU");//设置Toolbar标题
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawopen,
                R.string.drawclose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //toolbar.setTitle("侧滑栏");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //toolbar.setTitle("APP FrameWork");
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    //optionMenus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    private void hiddenShareMenu(){
        if(null != mMenu){
                mMenu.getItem(0).setVisible(false);
                mMenu.getItem(0).setEnabled(false);
        }
    }

    private void showShareMenu(){
        if(null != mMenu){
                mMenu.getItem(0).setVisible(true);
                mMenu.getItem(0).setEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //此处进行Item逻辑判断处理
        switch (item.getItemId()) {
            case R.id.action_settings:
                File picfile = new File(Constant.HEAD_PATH + "head.jpg");
                if(picfile.exists()){
                    picfile.delete();//如果文件存在，删除掉
                }
                MLUser.logOut();
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                return true;
            case R.id.action_share:
                //将bitmap分享到微信朋友圈
                shareToWX();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void shareTo(Bitmap mbitmap,Boolean isFrind){
        //第一步 获取二进制图像的bitmap；
        Bitmap bitmap = mbitmap;
        //第二步创建WXImageObject对象，并包装bitmap
        WXImageObject imgObj = new WXImageObject(bitmap);
        //第三步 创建WXMediaMessage对象，并包装imgObj
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        //第四步 压缩图片为缩略图 msg装载缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap,80,100,true);
       // bitmap.recycle();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        thumbBmp.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        msg.thumbData = outputStream.toByteArray();
        //thumbBmp.recycle();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //第五步 创建SendMessageToWX.Req对象，发送数据
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;//装载msg
        req.scene = isFrind  ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }
    //为请求生成一个标示
    private String buildTransaction (final String type){
        return (type == null) ? String.valueOf(System.currentTimeMillis()):type+System.currentTimeMillis();
    }
    //分享到微信朋友或者朋友圈的点击事件
    public void share_frind (View view){
        switch (view.getId()){
            case R.id.tofriend://分享到朋友圈
                shareTo(mBitmap,true);
                break;
            case R.id.toweixin://分享到微信朋友
                shareTo(mBitmap,false);
                break;
        }
    }
    //分享到微信朋友或者朋友圈的dialog
    private void shareToWX() {
        LayoutInflater infalter = LayoutInflater.from(mContext);
        View view = infalter.inflate(R.layout.share_to_wx, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle("分享到");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //显示出optionmenu的按钮图标，使用反射让其显示出来

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {

                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

        return false;//关闭系统menu按键
    }




    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    /*
    //初始化控件
     */
    private void initView() {
        mPhoto = (ImageView) findViewById(R.id.mphoto);
        mTint = (TextView) findViewById(R.id.tip);
        mWaiting = findViewById(R.id.waiting);
        mListView = (ListView) findViewById(R.id.id_lv);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
        mCircleImageView = (CircleImageView) findViewById(R.id.civ);
        mName = (TextView) findViewById(R.id.mname);

    }

    /*
    //Button 设置点击事件
     */
    public void mClick(View v) {
        switch (v.getId()) {
            case R.id.get_imge:
                //这个按钮的功能是获取图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //跳转到图库，然后返回接受数据。
                startActivityForResult(intent, PICK_CODE);
                break;
            case R.id.detect:

                if (isNewImage) {
                    // 设置progress bar 可见
                    isNewImage = false;
                    mWaiting.setVisibility(View.VISIBLE);

                    FaceppDetent.detect(mBitmap, new FaceppDetent.CallBack() {
                        @Override
                        public void success(JSONObject result) {
                            Message msg = new Message();
                            msg.what = MSG_SUCCESS;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }

                        @Override
                        public void error(FaceppParseException exception) {
                            Message msg = new Message();
                            msg.what = MSG_ERROR;
                            msg.obj = exception.getErrorMessage();
                            mHandler.sendMessage(msg);

                        }
                    });
                }


                break;
        }
    }

    /*
    //创建一个handler  接受到子线程传回的json数据，在ui线程里面解析和绘图
     */
    private static final int MSG_SUCCESS = 0x111;
    private static final int MSG_ERROR = 0x112;

    private Handler mHandler = new Handler() {

        //复写handlermessage 接受传回的json
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SUCCESS:
                    mWaiting.setVisibility(View.GONE);
                    //取出json数据
                    JSONObject jsonobject = (JSONObject) msg.obj;
                    //json解析并且绘图  创建一个prepareRSBitmap()的方法
                    prepareRSBitmap(jsonobject);
                    //把绘制好的bitmap设置到imageview上
                    mPhoto.setImageBitmap(mBitmap);
                    //设置share图标可见
                    showShareMenu();

                    break;

                case MSG_ERROR:
                    mWaiting.setVisibility(View.GONE);
                    String errorMsg = (String) msg.obj;

                    if (TextUtils.isEmpty(errorMsg)) {
                        mTint.setText("ERROR");
                    } else {
                        mTint.setText(errorMsg);
                    }

                    break;
            }

        }
    };

    /*
      //  json解析并且绘图  创建一个prepareRSBitmap()的方法
       */
    private void prepareRSBitmap(JSONObject jsonobject) {
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mBitmap, 0, 0, null);

        try {

            JSONArray faces = jsonobject.getJSONArray("face");
            int faceCount = faces.length();
            mTint.setText("FIND " + faceCount + " face");
            for (int i = 0; i < faceCount; i++) {
                //拿到单独的face对象
                JSONObject face = faces.getJSONObject(i);
                JSONObject position = face.getJSONObject("position");

                //解析出xyhw
                float x = (float) position.getJSONObject("center").getDouble("x");
                float y = (float) position.getJSONObject("center").getDouble("y");

                float h = (float) position.getDouble("height");
                float w = (float) position.getDouble("width");

                x = x / 100 * bitmap.getWidth();
                y = y / 100 * bitmap.getHeight();
                h = h / 100 * bitmap.getHeight();
                w = w / 100 * bitmap.getWidth();

                //用canvas画脸部边框  需要point
                mPaint.setColor(0xFFF43BE1);
                mPaint.setStrokeWidth(5);

                canvas.drawLine(x - w / 2, y - h / 2, x - w / 2, y + h / 2, mPaint);
                canvas.drawLine(x - w / 2, y + h / 2, x + w / 2, y + h / 2, mPaint);
                canvas.drawLine(x - w / 2, y - h / 2, x + w / 2, y - h / 2, mPaint);
                canvas.drawLine(x + w / 2, y - h / 2, x + w / 2, y + h / 2, mPaint);

                //解析出age gender
                int age = face.getJSONObject("attribute").getJSONObject("age").getInt("value");
                String gender = face.getJSONObject("attribute").getJSONObject("gender").getString("value");

                //构造buildagebitmap（age ， ismale） 返回bitmap
                Bitmap ageBitmap = buildAgeBitmap(age, "Male".equals(gender));
                //缩放agebitmap
                int ageWidth = ageBitmap.getWidth();
                int ageHeight = ageBitmap.getHeight();
                if (bitmap.getWidth() < mBitmap.getWidth() && bitmap.getHeight() < mBitmap.getHeight()) {
                    //算出缩放比例ratio
                    float ratio = Math.max(bitmap.getWidth() * 1.0f / mBitmap.getWidth(),
                            bitmap.getHeight() * 1.0f / mBitmap.getHeight());
                    //缩放
                    ageBitmap = Bitmap.createScaledBitmap(ageBitmap, (int) (ageWidth * ratio),
                            (int) (ageHeight * ratio), false);
                }
                canvas.drawBitmap(ageBitmap, x - ageBitmap.getWidth() / 2,
                        y - h / 2 - ageBitmap.getHeight(), null);

                mBitmap = bitmap;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //构造buildagebitmap（age ， ismale） 返回bitmap
    private Bitmap buildAgeBitmap(int age, boolean isMale) {
        TextView tv = (TextView) mWaiting.findViewById(R.id.age_and_gender);
        tv.setText(age + "");
        if (isMale) {
            tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);
        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);
        }
        tv.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(tv.getDrawingCache());
        tv.destroyDrawingCache();

        return bitmap;
    }

    /*
   //返回activity 回调onActivityResuly    获得data
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CODE) {
            if (data != null) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    //获取图片 列的索引 idx
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    //通过idx 获得图片的路径 str
                    mCurrentPhotosStr = cursor.getString(idx);

                    //关闭cursor
                    cursor.close();
                }


                //压缩图片，调用压缩图片的方法resize（）；
                reSizePhoto();
                mPhoto.setImageBitmap(mBitmap);
                mTint.setText("试试TaoLu==>");
                isNewImage = true;
                //影藏share图标
                hiddenShareMenu();

            }
        }
    }

    //压缩图片的方法
    private void reSizePhoto() {
        //首先获得一个option对象
        BitmapFactory.Options options = new BitmapFactory.Options();
        //将injustdecodebounds设置为trus
        options.inJustDecodeBounds = true;
        //injustdecodebounds设置为trus, decodefile 将不会加载图片，只是获取图片的信息，图片的宽高
        BitmapFactory.decodeFile(mCurrentPhotosStr, options);
        //option中就有图片的宽高信息了，定义ratio为缩放比例
        double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);
        //进行缩放   设置  options.inSampleSize
        options.inSampleSize = (int) Math.ceil(ratio);
        //获得bitm之前 要把inJustDecdeBounds 设置回false
        options.inJustDecodeBounds = false;
        //得到缩放好 的 bitmap
        mBitmap = BitmapFactory.decodeFile(mCurrentPhotosStr, options);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.perfei.taolu/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.perfei.taolu/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences spf = getSharedPreferences("person_info",MODE_PRIVATE);
        mName.setText(spf.getString("name",""));
        Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/TAOLU/head.jpg");
        mCircleImageView.setImageBitmap(bitmap);

    }

    //listview 的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:

                break;
            case 1:
                Intent intent = new Intent(MainActivity.this,PersonActivity.class);
                startActivity(intent);
                break;
        }
    }
}
