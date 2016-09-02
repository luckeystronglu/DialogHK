package com.qf.dialoghk;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/30.
 */
public class FriendDialog extends Dialog implements View.OnClickListener {


    private Button btnSure, btnCancle;
    private EditText etName, etAddress;

    private OnEditListener onEditListener;

    private int type = 0;//0 - 表示添加好友 1 - 修改好友

    public FriendDialog(Context context, OnEditListener onEditListener) {
        super(context, R.style.dialog_friend);
        this.onEditListener = onEditListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);

        btnSure = (Button) findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(this);
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnCancle.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.et_name);
        etAddress = (EditText) findViewById(R.id.et_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                //点击确定
                String name = etName.getText().toString();
                String address = etAddress.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(address)){
                    //如果内容为空
                    Toast.makeText(getContext(), "内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //如果内容不为空
                    if(onEditListener != null){
                        if(type == 0){
                            onEditListener.edit(name, address);
                        } else if(type == 1){
                            onEditListener.update(name, address);
                        }
                    }
                    this.dismiss();
                }
                break;
            case R.id.btn_cancle:
                //点击取消
                this.dismiss();
                break;
        }
    }

    /**
     * 添加的方式弹出
     */
    @Override
    public void show(){
        this.type = 0;
        super.show();
        //清空输入框
        etName.setText("");
        etAddress.setText("");
    }

    /**
     * 修改的方式弹出
     * @param name
     * @param address
     */
    public void show(String name, String address){
        this.type = 1;
        super.show();
        etName.setText(name);
        etAddress.setText(address);
    }

    public interface OnEditListener{
        void edit(String name, String address);
        void update(String name, String address);
    }
}
