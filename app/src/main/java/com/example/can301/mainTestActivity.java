package com.example.can301;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Window;

import com.example.can301.adapter.PageViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class mainTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main_test);

        // 获取页面上的底部导航栏控件
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // 配置navigation与底部菜单之间的联系
        // 底部菜单的样式里面的item里面的ID与navigation布局里面指定的ID必须相同，否则会出现绑定失败的情况
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_white_noise,R.id.navigation_home,R.id.navigation_profile,R.id.navigation_gift)
                .build();
        // 建立fragment容器的控制器，这个容器就是页面的上的fragment容器
        Bundle extras = getIntent().getExtras();
        NavController navController = navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (extras != null) {
            boolean toNoise = extras.getBoolean("toNoise", false);
            if (toNoise) {
                navController = Navigation.findNavController(this, R.id.navigation_white_noise);
            }
        }

        assert navController!=null;
        // 启动
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }



}
