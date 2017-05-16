package com.shen.tree.bean;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
/**
 * TreeListViewAdapter<T> extends BaseAdapter
 *
 * @param <T>
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter
{

	protected Context mContext;

	/** 存储所有可见的Node */
	protected List<Node> mNodes;

	protected LayoutInflater mInflater;

	/** 存储所有的Node */
	protected List<Node> mAllNodes;

	/** 点击的回调接口 */
	private OnTreeNodeClickListener onTreeNodeClickListener;

	/** 点击的回调接口 */
	public interface OnTreeNodeClickListener {
		void onClick(Node node, int position);
	}

	/** 设置 -- 点击的回调接口 */
	public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
		this.onTreeNodeClickListener = onTreeNodeClickListener;
	}


	/**
	 *  构造函数
	 *
	 * @param mTree						ListView 控件
	 * @param context					上下文
	 * @param datas						要进行排序的数据(bean)
	 * @param defaultExpandLevel  		默认展开几级树
	 *
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public TreeListViewAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel)
			throws IllegalArgumentException, IllegalAccessException {

		mContext = context;

		/** 对所有的Node进行排序 */
		mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);

		/** 过滤出可见的Node */
		mNodes = TreeHelper.filterVisibleNode(mAllNodes);

		mInflater = LayoutInflater.from(context);

		/**
		 * 设置"节点点击时"，可以"展开以及关闭"；并且将ItemClick事件继续往外公布
		 */
		mTree.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				expandOrCollapse(position);

				if (onTreeNodeClickListener != null) {
					onTreeNodeClickListener.onClick(mNodes.get(position), position);
				}
			}

		});

	}

	/**
	 * 相应ListView的点击事件 展开或关闭某节点
	 * 
	 * @param position
	 */
	public void expandOrCollapse(int position) {
		Node n = mNodes.get(position);

		if (n != null){												// 排除传入参数错误异常

			if (!n.isLeaf()){

				n.setExpand(!n.isExpand());							// 设置

				mNodes = TreeHelper.filterVisibleNode(mAllNodes);	// 所有可见的
				notifyDataSetChanged();								// 刷新视图
			}
		}
	}

	@Override
	public int getCount()
	{
		return mNodes.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mNodes.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Node node = mNodes.get(position);
		convertView = getConvertView(node, position, convertView, parent);

		// 设置内边距 -- 缩进
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);

		return convertView;
	}

	public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);

}
