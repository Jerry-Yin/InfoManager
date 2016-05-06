package cn.edu.infomanager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.infomanager.R;
import cn.edu.infomanager.module.User;
import cn.edu.infomanager.util.UserService;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private EditText username;
    private EditText password;
    private UserService uService;
    private SharedPreferences spf;
    private CheckBox cb_savepass;
    private View v_findpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        findViews();

        // TODO: 4/24/16 显示notify来通知之前没看的通知
//		showLastNotify();

        showPassword();
    }

    private void showLastNotify() {
        NotificationManager notificationService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationService.notify();
//		notificationService.cancel(tag);
    }

    private void findViews() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        cb_savepass = (CheckBox) findViewById(R.id.cb_savepass);
    }

    /**
     * 初始化，记住的密码
     * author
     */
    private void showPassword() {
        spf = getSharedPreferences("userinfo", MODE_WORLD_READABLE + MODE_WORLD_WRITEABLE);
        username.setText(spf.getString("name", ""));
        password.setText(spf.getString("password", ""));

        if (!"".equals(spf.getString("name", ""))) {
            cb_savepass.setChecked(true);
        }
    }

    /**
     * BT	登录
     *
     * @param view
     */
    public void bt_Login(View view) {
        String name = username.getText().toString();
        String pass = password.getText().toString();
        Log.i("TAG", name + "_" + pass);
        uService = new UserService(LoginActivity.this);
        User user_info = uService.login(name, pass);
        if (user_info != null) {
            Log.i("TAG", "登录成功");
            if (cb_savepass.isChecked()) {
                Editor edit = spf.edit();
                edit.putString("name", name);
                edit.putString("password", pass);
                edit.commit();
            }
            Intent intent = new Intent(this, BusinessManager.class);
//            intent.setClass(this, BusinessManager.class);
//            intent.setClass(this, NavigationDrawerActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt("id", user_info.getId());
            bundle.putString("name", user_info.getUsername());

            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            Log.i("TAG", "登录失败");
            Toast.makeText(LoginActivity.this, "用户名或密码错误!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * BT	注册
     *
     * @param view
     */
    public void bt_Register(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 记住密码
     * author
     */
    public void cb_SavePassword(View view) {
        if (cb_savepass.isChecked()) {
            Editor edit = spf.edit();
            edit.putString("name", username.getText().toString());
            edit.putString("password", password.getText().toString());
            edit.commit();
        } else {
            Editor edit = spf.edit();
            edit.clear();
            edit.commit();
        }
    }

    /**
     * 找回密码(View)
     * author
     */
    public void bt_findPassword(View view) {
        v_findpass = getLayoutInflater().inflate(R.layout.findpassword, null);
        Log.d(TAG, "点击了按钮！！！");
        new AlertDialog.Builder(this)
                .setTitle("输入用户名和邮箱找回密码")
                .setView(v_findpass)
                .create()
                .show();
    }


    /**
     * 找回密码(Method)
     * author
     */
    public void findPassword(View view) {
        TextView tv_name = (TextView) v_findpass.findViewById(R.id.find_name);
        TextView tv_email = (TextView) v_findpass.findViewById(R.id.find_email);

        uService = new UserService(LoginActivity.this);
        User user = uService.findPassword(tv_name.getText().toString(), tv_email.getText().toString());

        if (user != null && user.getPassword() != null && !"".equals(user.getPassword())) {
            Toast.makeText(this, "您的密码为：" + user.getPassword(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (uService != null) {
            uService.closeDB();
        }
    }

}
