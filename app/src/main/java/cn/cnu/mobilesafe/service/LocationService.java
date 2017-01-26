package cn.cnu.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

/**
 * 获取经纬度坐标的service
 *
 * @author Kevin
 *
 */
public class LocationService extends Service {

	private LocationManager lm;
	private MyLocationListener listener;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		// List<String> allProviders = lm.getAllProviders();// 获取所有位置提供者
		// System.out.println(allProviders);

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);// 是否允许付费,比如使用3g网络定位
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lm.getBestProvider(criteria, true);// 获取最佳位置提供者

		listener = new MyLocationListener();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		// 参1表示位置提供者,参2表示最短更新时间,参3表示最短更新距离
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
	}

	class MyLocationListener implements LocationListener {

		// 位置发生变化
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("get location!");

			// 将获取的经纬度保存在sp中
			mPref.edit()
					.putString(
							"location",
							"j:" + location.getLongitude() + "; w:"
									+ location.getLatitude()).apply();

			//向安全手机发送短信告诉位置
			String safePhone =mPref.getString("safe_phone","");
			Uri smsToUri = Uri.parse("smsto:" + safePhone);
			Intent sendMessageIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);//调用系统发短信
			sendMessageIntent.putExtra("发送内容是",location);
			startActivity(sendMessageIntent);
			//startService(sendMessageIntent);

			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(safePhone,
			null,"这里是短信内容",  null, null);

			stopSelf();//停掉service
		}

		// 位置提供者状态发生变化
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("onStatusChanged");
		}

		// 用户打开gps
		@Override
		public void onProviderEnabled(String provider) {
			System.out.println("onProviderEnabled");
		}

		// 用户关闭gps
		@Override
		public void onProviderDisabled(String provider) {
			System.out.println("onProviderDisabled");
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		lm.removeUpdates(listener);// 当activity销毁时,停止更新位置, 节省电量
	}

}
