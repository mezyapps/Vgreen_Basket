package com.mezyapps.vgreenbasket.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mezyapps.vgreenbasket.R;
import com.mezyapps.vgreenbasket.db.AppDatabase;
import com.mezyapps.vgreenbasket.db.entity.CardProductModel;
import com.mezyapps.vgreenbasket.utils.SharedLoginUtils;
import com.mezyapps.vgreenbasket.view.fragment.HomeFragment;
import com.mezyapps.vgreenbasket.view.fragment.NotificationListFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_drawer, iv_notification;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout frameLayout_main;
    private String fragmentName = null;
    Fragment fragmentInstance;
    FragmentManager fragmentManager;
    private boolean doubleBackToExitPressedOnce = false;
    private Dialog dialog_logout;
    private TextView text_version_name, text_app_name,textName,textMobileNumber,textCardCnt;
    private LinearLayout ll_login,ll_sign_up,ll_login_sign_up;
    private AppDatabase appDatabase;
    private ArrayList<CardProductModel> cardProductModelArrayList = new ArrayList<>();
    private RelativeLayout rr_cart;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        user_id=SharedLoginUtils.getUserId(MainActivity.this);
        FirebaseMessaging.getInstance().subscribeToTopic(getResources().getString(R.string.topic));
        FirebaseMessaging.getInstance().subscribeToTopic(user_id);

        find_View_IdS();
        loadFragment(new HomeFragment());
        events();
    }


    private void find_View_IdS() {
        appDatabase= appDatabase= AppDatabase.getInStatce(MainActivity.this);
        iv_drawer = findViewById(R.id.iv_drawer);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        text_version_name = findViewById(R.id.text_version_name);
        text_app_name = findViewById(R.id.text_app_name);
        rr_cart = findViewById(R.id.rr_cart);
        iv_notification = findViewById(R.id.iv_notification);
        View view=navigationView.getHeaderView(0);
        ll_login = view.findViewById(R.id.ll_login);
        ll_sign_up = view.findViewById(R.id.ll_sign_up);
        textMobileNumber = view.findViewById(R.id.textMobileNumber);
        textName = view.findViewById(R.id.textName);
        ll_login_sign_up = view.findViewById(R.id.ll_login_sign_up);
        textCardCnt = findViewById(R.id.textCardCnt);
    }

    private void events() {
        displayUserInfo();
       int size=cartCount();
       textCardCnt.setText(String.valueOf(size));
        iv_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_dashboard) {
                    loadFragment(new HomeFragment());
                }else if (id == R.id.nav_profile) {
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                }else if (id == R.id.nav_order_history) {
                    startActivity(new Intent(MainActivity.this,OrderHistoryActivity.class));
                }   else if (id == R.id.nav_share_app) {
                    shareApplication();
                } else if (id == R.id.nav_logout) {
                    logoutApplication();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        rr_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,CardActivity.class ));
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new NotificationListFragment());
            }
        });
        ll_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }

    private void displayUserInfo() {
        String name=SharedLoginUtils.getUserName(MainActivity.this);
        String mobile_no=SharedLoginUtils.getUserMobile(MainActivity.this);

        if(!name.equalsIgnoreCase("") && !mobile_no.equalsIgnoreCase("")) {
            textName.setText(name);
            textMobileNumber.setText(mobile_no);
            ll_login_sign_up.setVisibility(View.GONE);
        }
    }

    public int cartCount() {
        cardProductModelArrayList.clear();
        cardProductModelArrayList.addAll(appDatabase.getProductDAO().getAppProduct());
        int size=cardProductModelArrayList.size();
        return size;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentName.equals(fragmentInstance.getClass().getSimpleName())) {
                if (fragmentName.equals("HomeFragment")) {
                    doubleBackPressLogic();
                } else
                    loadFragment(new HomeFragment());
            }
        }
    }

    // ============ End Double tab back press logic =================
    private void doubleBackPressLogic() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit !!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void loadFragment(Fragment fragment) {
        fragmentInstance = fragment;
        fragmentName = fragment.getClass().getSimpleName();
        if (fragmentName.equalsIgnoreCase("HomeFragment")) {
            text_app_name.setText(R.string.app_name);
        } else if (fragmentName.equalsIgnoreCase("ChapterListFragment")) {
            text_app_name.setText("Chapter List");
        } else if (fragmentName.equalsIgnoreCase("ChangePasswordFragment")) {
            text_app_name.setText("Change Password");
        } else if (fragmentName.equalsIgnoreCase("LunchFragment")) {
            text_app_name.setText("Launch DC List");
        } else if (fragmentName.equalsIgnoreCase("AddVisitorFragment")) {
            text_app_name.setText("Visitor List");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_main, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    public void logoutApplication() {
        dialog_logout = new Dialog(MainActivity.this);
        dialog_logout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_logout.setContentView(R.layout.dialog_logout);
        TextView txt_cancel = dialog_logout.findViewById(R.id.txt_cancel);
        TextView txt_logout = dialog_logout.findViewById(R.id.txt_logout);
        dialog_logout.setCancelable(false);
        dialog_logout.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog_logout.show();

        Window window = dialog_logout.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_logout.dismiss();
            }
        });
        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(getResources().getString(R.string.topic));
                FirebaseMessaging.getInstance().unsubscribeFromTopic(user_id);

                SharedLoginUtils.removeUserSharedUtils(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void shareApplication() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        String app_url = "https://play.google.com/store/apps/details?id="+getPackageName();
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int size=cartCount();
        textCardCnt.setText(String.valueOf(size));
        displayUserInfo();
    }
}
