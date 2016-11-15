package com.classic.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import com.classic.adapter.interfaces.IAdapter;
import com.classic.adapter.interfaces.IData;
import com.classic.adapter.interfaces.IScrollHideListener;
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
@SuppressWarnings({ "WeakerAccess", "unused" }) public abstract class CommonRecyclerAdapter<T>
        extends RecyclerView.Adapter implements IAdapter<T>, IData<T> {

    private final Context mContext;
    private final int     mLayoutResId;
    private final List<T> mData;

    private OnItemClickListener     mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public CommonRecyclerAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public CommonRecyclerAdapter(Context context, int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.mContext = context;
        this.mLayoutResId = layoutResId;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseAdapterHelper helper = get(mContext, null, parent, viewType, -1);
        return new RecyclerViewHolder(helper.getView(), helper);
    }

    @SuppressWarnings("unchecked") @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseAdapterHelper helper = ((RecyclerViewHolder) holder).mAdapterHelper;
        helper.setAssociatedObject(getItem(position));
        onUpdate(helper, getItem(position), position);
    }

    @Override public int getItemViewType(int position) {
        return getLayoutResId(getItem(position), position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public int getItemCount() {
        return mData.size();
    }

    @Override public int getLayoutResId(T item, int position) {
        return this.mLayoutResId;
    }

    @Override public List<T> getData() {
        return mData;
    }

    @Override public void add(T elem) {
        mData.add(elem);
        notifyItemInserted(mData.size());
    }

    @Override public void addAll(List<T> elem) {
        mData.addAll(elem);
        notifyItemRangeInserted(mData.size() - elem.size(), elem.size());
    }

    @Override public void set(T oldElem, T newElem) {
        set(mData.indexOf(oldElem), newElem);
    }

    @Override public void set(int index, T elem) {
        mData.set(index, elem);
        notifyItemChanged(index);
    }

    @Override public void remove(T elem) {
        final int position = mData.indexOf(elem);
        mData.remove(elem);
        notifyItemRemoved(position);
    }

    @Override public void remove(int index) {
        mData.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * @see {@link #replaceAll(List, DiffUtil.Callback)}
     * @param elem
     */
    @Deprecated
    @Override public void replaceAll(List<T> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    @Override public boolean contains(T elem) {
        return mData.contains(elem);
    }

    @Override public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem, DiffUtil.Callback callback) {
        mData.clear();
        mData.addAll(elem);
        DiffUtil.calculateDiff(callback, true).dispatchUpdatesTo(this);
    }

    public T getItem(int position) {
        return position >= mData.size() ? null : mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position);
    }

    private final class RecyclerViewHolder extends RecyclerView.ViewHolder {
        BaseAdapterHelper mAdapterHelper;

        public RecyclerViewHolder(View itemView, BaseAdapterHelper adapterHelper) {
            super(itemView);
            this.mAdapterHelper = adapterHelper;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (null != mItemClickListener) {
                        mItemClickListener.onItemClick(RecyclerViewHolder.this, v, getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    if (null != mItemLongClickListener) {
                        mItemLongClickListener.onItemLongClick(RecyclerViewHolder.this, v, getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public static abstract class AbsScrollControl extends RecyclerView.OnScrollListener implements IScrollHideListener {
        private static final int DEFAULT_SCROLL_HIDE_OFFSET = 20; //滑动隐藏的偏移量

        private int     mCurrentScrollOffset;
        private boolean isControlVisible;

        /**
         * 自定义LayoutManager需要实现此方法
         */
        protected int getFirstVisibleItemPositions() {
            return 0;
        }

        /** 获取滑动隐藏的偏移量 */
        protected int getScrollHideOffset() {
            return DEFAULT_SCROLL_HIDE_OFFSET;
        }

        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            final int firstVisibleItemPosition = findFirstVisibleItemPosition(recyclerView.getLayoutManager());

            if (firstVisibleItemPosition == 0 && !isControlVisible) {
                onShow();
                isControlVisible = true;
            } else if (firstVisibleItemPosition != 0 && mCurrentScrollOffset > getScrollHideOffset() &&
                       isControlVisible) {
                //向上滚动,并且视图为显示状态
                onHide();
                isControlVisible = false;
                mCurrentScrollOffset = 0;
            } else if (firstVisibleItemPosition != 0 && mCurrentScrollOffset < -getScrollHideOffset() &&
                       !isControlVisible) {
                //向下滚动,并且视图为隐藏状态
                onShow();
                isControlVisible = true;
                mCurrentScrollOffset = 0;
            }

            //dy>0:向上滚动
            //dy<0:向下滚动
            if ((isControlVisible && dy > 0) || (!isControlVisible && dy < 0)) {
                mCurrentScrollOffset += dy;
            }
        }

        private int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
            if (layoutManager instanceof GridLayoutManager) {
                return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null)[0];
            } else {
                return getFirstVisibleItemPositions();
            }
        }
    }
}
