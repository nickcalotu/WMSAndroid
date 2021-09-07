package com.wms.android.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.wms.android.MainActivity
import com.wms.android.R
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.ResultOne
import com.wms.android.logic.model.Token
import com.wms.android.logic.model.User
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.logic.network.TestService
import com.wms.android.logic.network.UserService
import com.wms.android.util.getVersion
import com.wms.android.util.setSharedData
import com.wms.android.util.shared_username
import com.wms.android.util.showAlertDialog
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File
import java.util.ArrayList


class LoginActivity : BaseActivity() {

    //定义服务
    private lateinit var userService:UserService
    private lateinit var testService :TestService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //带出历史输入用户名
        txt_username.setText(shared_username)

        btn_login.setOnClickListener {
            btn_login.isEnabled = false
            userService.login(User(txt_username.text.toString(), txt_pwd.text.toString()))
                .enqueue(ServiceCreator.go(fun(res: ResultOne<Token>) {
                    var token = res.model.token
                    userService.info(token)
                        .enqueue(ServiceCreator.go(fun(res: ResultOne<User>) {
                            btn_login.isEnabled = true
                            val menunames = res.model.menunames
                            User.user = res.model

                            //保存用户
                            shared_username = txt_username.text.toString()
                            setSharedData()

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putStringArrayListExtra("menu", menunames as ArrayList<String>?)
                            startActivity(intent)

                        }))
                }, fun() {
                    btn_login.isEnabled = true
                }))
        }

        lbl_config.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        ServiceCreator.initWebService()
        userService = ServiceCreator.create(UserService::class.java)
        testService = ServiceCreator.create(TestService::class.java)

        val version = getVersion(this)
        lbl_version.text = "版本:"+version.toString()
        //检验版本更新
        testService.getNowVersion()
            .enqueue(ServiceCreator.go(fun(res: ResultOne<Double>) {
                val newVersion = res.model
                if (newVersion > version) {
                    showAlertDialog(this, "提示", "检测到新版本！是否立即更新?") {
                        //downAPK()//下载更新,使用通知方式下载更新
                        //showToast("请稍后查看通知栏进度！")
                        pb.visibility = View.VISIBLE
                        lbl_rate.visibility = View.VISIBLE
                        ServiceCreator.download(object : ServiceCreator.OnDownloadListener {
                            override fun onDownloadSuccess(file: File) {
                                runOnUiThread { pb.visibility = View.GONE
                                    lbl_rate.visibility = View.GONE}
                                installApk(file);
                            }
                            override fun onDownloading(progress: Int) {
                                runOnUiThread { pb.progress = progress
                                    lbl_rate.text = "正在下载..."+progress.toString()+"%"}

                            }
                            override fun onDownloadFailed() {
                                //Log.e(TAG,"onDownloadFailed")
                            }
                        })

                    }
                } else {
                    //showToast("当前是最新版本！")
                }

            }))
    }

    private fun installApk(apkFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri = FileProvider.getUriForFile(this, "$packageName.FileProvider", apkFile)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }
        startActivity(intent)
    }
}


