package cn.cnu.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


import cn.cnu.mobilesafe.R;
import cn.cnu.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 *
 * @author Fly Wu
 */
public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;// 设置升级
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //记录是否自动更新等设置的配置信息，存储在SharedPreferences中
        mPref = getSharedPreferences("config", MODE_PRIVATE);

        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        //sivUpdate.setTitle("自动更新设置");
       // sivUpdate.setDesc("自动更新已开启");

        boolean autoUpdate = mPref.getBoolean("auto_update", true);

        if (autoUpdate) {
           // sivUpdate.setDesc("自动更新已开启");
            sivUpdate.setChecked(true);
        } else {
           //sivUpdate.setDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }

        sivUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 判断当前的勾选状态
                if (sivUpdate.isChecked()) {
                    // 设置不勾选
                    sivUpdate.setChecked(false);
                  //  sivUpdate.setDesc("自动更新已关闭");
                    // 更新SharedPreferences
                    mPref.edit().putBoolean("auto_update", false).apply();
                } else {
                    sivUpdate.setChecked(true);
                  //  sivUpdate.setDesc("自动更新已开启");
                    // 更新SharedPreferences
                    mPref.edit().putBoolean("auto_update", true).apply();
                }
            }
        });
    }
}
