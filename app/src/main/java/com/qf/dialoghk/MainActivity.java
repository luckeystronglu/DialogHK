package com.qf.dialoghk;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;

import com.qf.adapter.FriendsAdapter;
import com.qf.db.FriendDatabase;
import com.qf.entity.Friend;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, FriendDialog.OnEditListener {

    private EditText etname,etaddress;
    private ListView lv;
    private FriendsAdapter adapter;

    private FriendDatabase friendDatabase;
    private SQLiteDatabase sqLiteDatabase;

    private SimpleCursorAdapter simpleCursorAdapter;//SimpleAdapter

    private PopupWindow popupWindow;
    private FriendDialog friendDialog;
    private Button btnAdd;

    private int index;//修改的下标

    private int id = -1;

//    private List<Friend> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        //初始化popuwindow
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.mipmap.lbs_popup_bg));
        popupWindow.setOutsideTouchable(true);
        View view = LayoutInflater.from(this).inflate(R.layout.popuwindow_layout, null);
        //获取添加好友的button对象
        btnAdd = (Button) view.findViewById(R.id.btn_addfriend);
        btnAdd.setOnClickListener(this);
        popupWindow.setContentView(view);


        lv = (ListView) findViewById(R.id.lv);
        adapter = new FriendsAdapter(this);
        lv.setAdapter(adapter);

        friendDatabase = new FriendDatabase(this);
        sqLiteDatabase = friendDatabase.getReadableDatabase();

        //        simpleCursorAdapter = new SimpleCursorAdapter(
        //                this,
        //                R.layout.item_layout,
        //                null,
        //                new String[]{"name","address"},
        //                new int[]{R.id.tv_name,R.id.tv_address},
        //                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        //        lv.setAdapter(simpleCursorAdapter);



        //初始化dialog
        friendDialog = new FriendDialog(this, this);

        //注册上下文菜单 -- 注册给ListView对象
        registerForContextMenu(lv);

        loadDatas();

    }

    private void loadDatas() {
//        lists = new ArrayList<Friend>();
        Cursor cursor = sqLiteDatabase.query("friend",new String[]{"_id","name","address"}
                ,null,null,null,null,null);
        Log.d("print","-------------"+ cursor.getCount());
        while(cursor.moveToNext()) {
            Friend friend = new Friend();
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String address = cursor.getString(cursor.getColumnIndex("address"));

            adapter.addData(new Friend(id,name,address));
        }
        adapter.notifyDataSetChanged();

    }



    /**
     * 创建上下文菜单
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * 上下文菜单的选中事件
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        index = adapterContextMenuInfo.position;
        Friend friend = (Friend) adapter.getItem(index);
        this.id = friend.getId();

        switch (item.getItemId()) {
            case R.id.menu_update:
                //修改信息
                //				Log.d("print","--->修改信息 " + adapterContextMenuInfo.position);
//                Friend friend = (Friend) simpleCursorAdapter.getItem(adapterContextMenuInfo.position);
                friendDialog.show(friend.getName(), friend.getAddress());
                adapter.notifyDataSetChanged();
                break;
            case R.id.menu_delete:
                //删除好友
                sqLiteDatabase.execSQL("delete from friend where _id = ?",new Object[]{this.id});
                adapter.deleteData(adapterContextMenuInfo.position);
                adapter.notifyDataSetChanged();
                //				Log.d("print","--->删除信息 " + adapterContextMenuInfo.position);

//                        deleteData(adapterContextMenuInfo.position);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 更多的点击事件
     * @param v
     */
    public void btnClick(View v){
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(v);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addfriend:
                //关闭popuwindow
                popupWindow.dismiss();
                //添加好友
                friendDialog.show();
                break;
        }
    }

    /**
     * dialog信息输入的回调方法
     */
    @Override
    public void edit(String name, String address) {
        Log.d("print", "获得添加的好友信息：" + name + "  " + address);
        //
        Friend friend = new Friend();
        friend.setName(name);
        friend.setAddress(address);
        friend.setName(name);
        sqLiteDatabase.execSQL("insert into friend(name,address) values(?,?)",
                new Object[]{name,address});
        adapter.addData(friend);
        adapter.notifyDataSetChanged();

    }

    /**
     * 修改好友信息回调
     */
    @Override
    public void update(String name, String address) {
        Friend friend = new Friend();
        friend.setName(name);
        friend.setAddress(address);
        sqLiteDatabase.execSQL("update friend set name = ?,address = ? where _id = ?",new Object[]{name,address,this.id});
//        this.id = -1;
        adapter.updateData(index, friend);
//
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //	simpleCursorAdapter.changeCursor(null);
    }
}
