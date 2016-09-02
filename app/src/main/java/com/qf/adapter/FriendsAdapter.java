package com.qf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qf.dialoghk.R;
import com.qf.entity.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private List<Friend> datas;

    public FriendsAdapter(Context context){
        this.context = context;
        this.datas = new ArrayList<Friend>();
    }

    /**
     * 添加一个好友
     * @param
     */
    public void addData(Friend friend){
        this.datas.add(friend);
        this.notifyDataSetChanged();
    }

    /**
     * 删除position对应的下标
     * @param position
     */
    public void deleteData(int position){
        this.datas.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * 修改position所对应的好友
     * @param position
     * @param friend
     */
    public void updateData(int position, Friend friend){
        this.datas.set(position, friend);
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
            viewHolder.tvname = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvaddress = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(viewHolder);
        }

        viewHolder.tvname.setText(datas.get(position).getName());
        viewHolder.tvaddress.setText(datas.get(position).getAddress());
        return convertView;
    }

    class ViewHolder{
        TextView tvname,tvaddress;
    }
}
