package com.wms.android.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wms.android.R
import com.wms.android.util.*
import kotlinx.android.synthetic.main.activity_config.*
import java.lang.Exception

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        //带出Shared中的值
        getSharedData()
        txt_ip.setText(shared_url)
        txt_port.setText(shared_port.toString())

        btn_submit.setOnClickListener {
            try {
                shared_url = txt_ip.text.toString();
                shared_port = txt_port.text.toString().toInt()
                if(shared_port<1|| shared_port>65535){
                    showToast("端口非法")
                    return@setOnClickListener
                }
            }catch (e:Exception){
                shared_port = 0
            }
            setSharedData()
            finish()
        }
    }
}