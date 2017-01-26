package cn.cnu.mobilesafe.service;


import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import cn.cnu.mobilesafe.receiver.AdminReceiver;


public class LockScreenService extends Service {
    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminSample;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 获取设备策略服务
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 设备管理组件
        mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);// 设备管理组件
        // 一键锁屏
        if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
            mDPM.lockNow();// 立即锁屏
            mDPM.resetPassword("1234", 0);
        } else {
            Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
        }


    }
}
