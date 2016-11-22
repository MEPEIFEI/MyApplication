package com.perfei.taolu;

import android.graphics.Bitmap;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 *  这是一个detect工具类
 *  将bitmap数据 转成二进制数据
 *  将二进制数据上传到服务器
 *  并且把服务器返回的json数据传出去解析
 */
public class FaceppDetent {
    /*
    这个工具类要返回两种情况
    需要定义一个接口来实现回调
     */
    public interface CallBack{
        //成功调用success 返回Jsonobjectresult
        //失败调用error  返回FaceppParseException exception
        void success(JSONObject result) ;
        void error(FaceppParseException exception) ;
    }
    public static void detect (final Bitmap bitmap ,final CallBack callBack)
    {
        //耗时操作，使用线程
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //请求 request   将bitmap 转成二进制数据  字节数组
                    HttpRequests requests = new HttpRequests(Constant.KEY , Constant.SECRET , true , true ) ;
                    //先创建一个smallbitmap  要对这个smallbitmap进行操作
                    Bitmap smallbitmap = Bitmap.createBitmap(bitmap, 0 , 0 ,bitmap.getWidth() ,bitmap.getHeight());
                    //创建ByteArrayOutputStream 对象，将bitmap压缩到二进制输出流stream
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    smallbitmap.compress(Bitmap.CompressFormat.JPEG , 100 , stream );

                    //通过stream.tobytearray  获得二进制数据  字节数组
                    byte[] arrays = stream.toByteArray();
                    //创建一个parameters   并把二进制数据传入parameters对象
                    // 通过requests.detectionDetect(parameters);获得返回的jsonobject数据
                    PostParameters parameters = new PostParameters();
                    parameters.setImg(arrays);
                    JSONObject jsonObject = requests.detectionDetect(parameters);
                    //如果成功获得jsonobject,调用callback.success 把jsonobject传递出去解析
                    if(callBack != null){
                        callBack.success(jsonObject);
                    }

                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    //如果失败，返回e
                    if(callBack != null)
                    {
                        callBack.error(e);
                    }
                }

            }
        }).start();
    }

}
