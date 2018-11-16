package com.ccc.expandablerecycleradapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccc on 2018/11/13.
 * 基于RecyclerView的二级列表适配器
 * T 一级列表bean
 * K 二级列表子集bean
 */
public abstract class ExpandableRecyclerAdapter<T extends ItemBean, K> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 一级列表
     */
    public static final int TYPE_GROUPITEM = 0;
    /**
     * 二级列表
     */
    public static final int TYPE_SUBITEM = 1;

    private List<T> mDataList;
    /**
     * 展开集合
     */
    private List<Boolean> expands;

    private LayoutInflater mInflater;

    public interface OnItemClickListener{
        void GroupItemClick(RecyclerView.ViewHolder holder, int position);
        void SubItemClick(RecyclerView.ViewHolder holder, int position);
    }

    private OnItemClickListener listener;

    public ExpandableRecyclerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUPITEM) {
            return createGroupViewHolder(parent, mInflater);
        } else if (viewType == TYPE_SUBITEM) {
            return createSubViewHolder(parent, mInflater);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        final ItemStatus status = getItemStatusByPosition(position);
        convert(holder, status, position);
        final int pos = position;

        if (status.getViewType() == TYPE_GROUPITEM) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ItemBean bean = mDataList.get(status.getGroupItemIndex());
                    int groupItem = status.getGroupItemIndex();

                    if (!expands.get(status.getGroupItemIndex())) {
                        expands.set(groupItem, true);
                        notifyItemRangeInserted(pos + 1, bean.getSubItems().size());
                        notifyItemRangeChanged(pos + 1, getItemCount() - pos);
                    } else {
                        expands.set(groupItem, false);
                        notifyItemRangeRemoved(pos + 1, bean.getSubItems().size());
                        notifyItemRangeChanged(pos + 1, getItemCount() - pos);
                    }

                    if (listener != null){
                        listener.GroupItemClick(holder, pos);
                    }
                }
            });
        }

        if (status.getViewType() == TYPE_SUBITEM){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.SubItemClick(holder, pos);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        int count = 0;

        if (expands == null){
            return 0;
        }

        for (int i = 0; i < expands.size(); i++) {
            ItemBean bean = mDataList.get(i);
            boolean isExpand = expands.get(i);
            if (isExpand) {
                count += bean.getSubItems().size() + 1;
            } else {
                count += 1;
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemStatusByPosition(position).getViewType();
    }

    public ItemStatus getItemStatusByPosition(int position) {
        ItemStatus status = new ItemStatus();

        int currPos = 0;

        for (int i = 0; i < expands.size(); i++) {

            boolean isExpand = expands.get(i);
            int size = isExpand ? mDataList.get(i).getSubItems().size() + 1 : 1;

            if (position >= currPos && position <= (currPos + size - 1)) {
                //groupItem
                if (currPos == position) {
                    status.setGroupItemIndex(i);
                    status.setViewType(TYPE_GROUPITEM);
                    break;
                } else {//subItem
                    status.setGroupItemIndex(i);
                    status.setSubItemIndex(position - currPos - 1);
                    status.setViewType(TYPE_SUBITEM);
                    break;
                }
            }

            currPos += size;
        }

        return status;
    }

    /**
     * 设置数据Bean
     */
    public void setItemBeans(List<T> datas){

        if (mDataList != null){
            mDataList.clear();
            mDataList.addAll(datas);
        } else {
            mDataList = datas;
        }

        initExpand();

        notifyDataSetChanged();
    }

    protected List<T> getDataList() {
        return mDataList;
    }

    protected List<Boolean> getExpands(){
        return expands;
    }

    protected T getGroupItem(ItemStatus status){
        return mDataList.get(status.getGroupItemIndex());
    }

    protected K getSubItem(ItemStatus status){
        T t = getGroupItem(status);
        return (K) t.getSubItems().get(status.getSubItemIndex());
    }

    public boolean isExpandByPosition(int position){
        ItemStatus status = getItemStatusByPosition(position);
        return expands.get(status.getGroupItemIndex());
    }

    private void initExpand() {
        if (expands == null) {
            expands = new ArrayList<>(mDataList.size());
        }
        for (ItemBean bean : mDataList) {
            expands.add(bean.isExpand());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    protected abstract RecyclerView.ViewHolder createGroupViewHolder(ViewGroup parent, LayoutInflater inflater);
    protected abstract RecyclerView.ViewHolder createSubViewHolder(ViewGroup parent, LayoutInflater inflater);
    protected abstract void convert(RecyclerView.ViewHolder holder, ItemStatus status, int position);
}