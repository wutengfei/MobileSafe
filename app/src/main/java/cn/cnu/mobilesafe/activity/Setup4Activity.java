package cn.cnu.mobilesafe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import cn.cnu.mobilesafe.R;
import cn.cnu.mobilesafe.receiver.AdminReceiver;

/**
 * 第4个设置向导页
 */
public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cbProtect;
    // 设备管理组件
    private ComponentName mDeviceAdminSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
        cbProtect = (CheckBox) findViewById(R.id.cb_protect);

        boolean protect = mPref.getBoolean("protect", false);

        // 根据sp保存的状态,更新checkbox
        if (protect) {
            cbProtect.setText("防盗保护已经开启");
            cbProtect.setChecked(true);
        } else {
            cbProtect.setText("防盗保护没有开启");
            cbProtect.setChecked(false);
        }

        // 当checkbox发生变化时,回调此方法
        cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    cbProtect.setText("防盗保护已经开启");
                    mPref.edit().putBoolean("protect", true).apply();
                } else {
                    cbProtect.setText("防盗保护没有开启");
                    mPref.edit().putBoolean("protect", false).apply();
                }
            }
        });
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this, LostFindActivity.class));
        finish();

        // 两个界面切换的动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画

        mPref.edit().putBoolean("configed", true).apply();// 更新sp,表示已经展示过设置向导了,下次进来就不展示啦

    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();

        // 两个界面切换的动画
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    // 激活设备管理器, 也可以在设置->安全->设备管理器中手动激活
    public void openAdmin(View view) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "哈哈哈, 我们有了超级设备管理器, 好NB!");
        startActivity(intent);

    }


}
