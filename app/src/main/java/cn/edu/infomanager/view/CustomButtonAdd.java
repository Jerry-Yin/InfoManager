package cn.edu.infomanager.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.Button;

import cn.edu.infomanager.R;

/**
 * Created by JerryYin on 4/25/16.
 */
public class CustomButtonAdd extends Button {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public CustomButtonAdd(Context context) {
        super(context);
        setBackground(getResources().getDrawable(R.drawable.btn_add_bgd));
    }

}
