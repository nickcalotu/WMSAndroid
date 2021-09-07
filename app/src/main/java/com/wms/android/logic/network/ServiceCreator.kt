package com.wms.android.logic.network

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.wms.android.base.BaseApplication
import com.wms.android.logic.model.BaseResult
import com.wms.android.util.shared_port
import com.wms.android.util.shared_url
import com.wms.android.util.showToast
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

object ServiceCreator {
//    private  val  BASE_URL = "http://$shared_url:$shared_port/api/"
//    private  val  APK_URL = "http://$shared_url:$shared_port/apk/app-release.apk"
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()


    private lateinit var  BASE_URL:String
    private lateinit var  APK_URL :String
    private lateinit var retrofit :Retrofit

    fun initWebService(){
        BASE_URL = "http://$shared_url:$shared_port/api/"
        APK_URL = "http://$shared_url:$shared_port/apk/app-release.apk"
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun <T> create(serviceClass:Class<T>):T = retrofit.create(serviceClass)

    //利用回调函数block封装了繁琐的返回方法
    fun <T: BaseResult> go(block:((res:T)->Unit), block2: (() -> Unit)? =null): Callback<T>{
        return  object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                var result = response.body()
                if(result!=null){
                    //错误直接处理，返回20000情况
                    if(result.code!=20000) {
                        showToast(result.message, Toast.LENGTH_LONG)
                        if (block2 != null) {
                            block2()
                        }
                    }
                    else block(result)
                }else{
                    showToast(response.raw().message(),Toast.LENGTH_LONG)
                }
            }
            //非常正情况返回
            override fun onFailure(call: Call<T>, t: Throwable) {
                t.printStackTrace()
                if (block2 != null) {
                    block2()
                }
                showToast(t.message.toString(),Toast.LENGTH_LONG)
            }

        }
    }


    //通知模式实现下载apk
    fun downAPK():Long{
        val request = DownloadManager.Request(Uri.parse(APK_URL)).apply {
            //只允许wifi下载
            //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            setDestinationInExternalFilesDir(BaseApplication.context, Environment.DIRECTORY_DOWNLOADS,"wms.apk")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            // 设置 Notification 信息
            setTitle("正在下更新...")
            setDescription("下载完成后请点击运行！")
            //request.setVisibleInDownloadsUi(true)
            //allowScanningByMediaScanner()
            setMimeType("application/vnd.android.package-archive")
        }
        // 实例化DownloadManager 对象
        val downloadManager = BaseApplication.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return downloadManager.enqueue(request)
    }




    /**直接线程中下载
     * @param listener 下载监听
     */
    fun download(listener: OnDownloadListener) {
        //这个路径pda真实机访问不了
        //private var DEFAULT_SAVE_APK_PATH = (BaseApplication.context.externalCacheDir.toString())
        var DEFAULT_SAVE_APK_PATH = Environment.getExternalStorageDirectory().toString()+ File.separator + "com.wms.android"+ File.separator+"apk"+ File.separator
        var DEFAULT_APK_NAME = "app-release.apk"
        var client = OkHttpClient.Builder()
            .connectTimeout(200, TimeUnit.SECONDS)//设置连接超时时间
            .readTimeout(200, TimeUnit.SECONDS)//设置读取超时时间
            .build()
        val request: Request = Request.Builder().url(APK_URL).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call?, e: IOException?) {
                e?.printStackTrace()
                listener.onDownloadFailed()
            }

            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response) {
                var inputStream: InputStream? = null
                val buf = ByteArray(2048)
                var len = 0
                var fos: FileOutputStream? = null
                try {
                    inputStream = response.body()?.byteStream()
                    val total: Long = response.body()?.contentLength()!!
                    var downloadPath = File(DEFAULT_SAVE_APK_PATH)
                    if (!downloadPath.exists())
                    {
                        val res = downloadPath.mkdirs()
                    }
                    val file = File(downloadPath.absoluteFile, DEFAULT_APK_NAME)
                    file.createNewFile()
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    while (inputStream?.read(buf).also { len = it!! } != -1) {
                        fos?.write(buf, 0, len)
                        sum += len.toLong()
                        val progress = (sum * 1.0f / total * 100).toInt()
                        listener.onDownloading(progress)
                    }
                    fos?.flush()
                    listener.onDownloadSuccess(file)
                } catch (e: Exception) {
                    listener.onDownloadFailed()
                } finally {
                    try {
                        inputStream?.close()
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


    interface OnDownloadListener {
        /**
         * 下载成功
         */
        fun onDownloadSuccess(file: File)

        /**
         * @param progress
         * 下载进度
         */
        fun onDownloading(progress: Int)

        /**
         * 下载失败
         */
        fun onDownloadFailed()
    }


}