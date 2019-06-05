package cdictv.twds.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import cdictv.twds.R;
import cdictv.twds.util.CircleProgressbar;
import cdictv.twds.util.Sputils;

public class guideActivity extends BaseActivity {
    private Button cancel;
    private Button save;
    private EditText ed_port;
    private EditText ed_ip;
    private CircleProgressbar mCircleProgressbar;
    private boolean isClick = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        Boolean pd = Sputils.getBolean("bd", true);
        if(pd){
            Sputils.putString("ip","");
            Sputils.putString("port","");
        }
        Sputils.putBoolean("bd",false);
        mCircleProgressbar.setOutLineColor(Color.TRANSPARENT);
        mCircleProgressbar.setInCircleColor(Color.parseColor("#505559"));
        mCircleProgressbar.setProgressColor(Color.parseColor("#1BB079"));
        mCircleProgressbar.setProgressLineWidth(5);
        mCircleProgressbar.setProgressType(CircleProgressbar.ProgressType.COUNT);
        mCircleProgressbar.setTimeMillis(10000);
        mCircleProgressbar.reStart();

        mCircleProgressbar.setCountdownProgressListener(1,progressListener);

        mCircleProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isClick = true;
                View inflate = View.inflate(guideActivity.this, R.layout.set_ip_dialog, null);
                cancel=inflate.findViewById(R.id.cancel);
                save=inflate.findViewById(R.id.save);
                ed_port=inflate.findViewById(R.id.ed_port);
                ed_ip=inflate.findViewById(R.id.ed_ip);
                if(Sputils.getString("ip").isEmpty()||Sputils.getString("port").isEmpty()){

                }else {
                    ed_port.setText(Sputils.getString("port"));
                    ed_ip.setText(Sputils.getString("ip"));
                }
                final AlertDialog alertDialog = new AlertDialog.Builder(guideActivity.this).setView(inflate).show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(guideActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        alertDialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Sputils.putString("ip",ed_ip.getText().toString().trim());
                        Sputils.putString("port",ed_port.getText().toString().trim());
                        Intent intent = new Intent(guideActivity.this,MainActivity.class);
                         startActivity(intent);
                        finish();
                    }
                });


            }
        });
    }
    private CircleProgressbar.OnCountdownProgressListener progressListener = new CircleProgressbar.OnCountdownProgressListener() {
        @Override
        public void onProgress(int what, int progress)
        {

            if(what==1 && progress==100 && !isClick)
            {
                Intent intent = new Intent(guideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                Log.e("===", "onProgress: =="+progress );
            }

        }
    };
    private void initView() {
        mCircleProgressbar = (CircleProgressbar) findViewById(R.id.tv_red_skip);
    }

}
