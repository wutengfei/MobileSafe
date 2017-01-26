package cn.cnu.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import cn.cnu.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();

        // 两个界面切换的动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画
    }

    @Override
    public void showPreviousPage() {

    }

}
