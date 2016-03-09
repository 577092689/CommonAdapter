package com.classic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.classic.adapter.interfaces.IAdapter;
import com.classic.adapter.interfaces.IData;
import java.util.ArrayList;
import java.util.List;

import static com.classic.adapter.BaseAdapterHelper.get;

/**
 * 项目名称: CommonAdapter
 * 包 名 称: com.classic.adapter
 * 类 描 述: 通用Adapter,适用于RecyclerView,简化大量重复代码
 * 创 建 人: 续写经典
 * 创建时间: 2016/1/27 17:50.
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter implements IAdapter<T>,IData<T> {
    protected final Context context;
    protected final int layoutResId;
    protected final List<T> data;

    public CommonRecyclerAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public CommonRecyclerAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseAdapterHelper helper = get(context, null, parent, viewType, -1);
        return new RecyclerViewHolder(helper.getView(),helper);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseAdapterHelper helper = ((RecyclerViewHolder)holder).adapterHelper;
        helper.setAssociatedObject(getItem(position));
        onUpdate(helper, getItem(position));
    }

    @Override public int getItemViewType(int position) {
        return getLayoutResId(getItem(position));
    }

    @Override public long getItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        return position >= data.size() ? null : data.get(position);
    }

    @Override public int getItemCount() {
        return data.size();
    }

    @Override public int getLayoutResId(T item) {
        return this.layoutResId;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        BaseAdapterHelper adapterHelper;

        public RecyclerViewHolder(View itemView,BaseAdapterHelper adapterHelper) {
            super(itemView);
            this.adapterHelper = adapterHelper;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(RecyclerViewHolder.this,v,getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    if(null != onItemLongClickListener){
                        onItemLongClickListener.onItemLongClick(RecyclerViewHolder.this,v,getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

    @Override
    public void add(T elem) {
        data.add(elem);
        notifyItemInserted(data.size());
    }
    @Override
    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyItemRangeInserted(data.size()-elem.size(), elem.size());
    }
    @Override
    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }
    @Override
    public void set(int index, T elem) {
        data.set(index, elem);
        notifyItemChanged(index);
    }
    @Override
    public void remove(T elem) {
        final int position = data.indexOf(elem);
        data.remove(elem);
        notifyItemRemoved(position);
    }
    @Override
    public void remove(int index) {
        data.remove(index);
        notifyItemRemoved(index);
    }
    @Override
    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }
    @Override
    public boolean contains(T elem) {
        return data.contains(elem);
    }
    @Override
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
