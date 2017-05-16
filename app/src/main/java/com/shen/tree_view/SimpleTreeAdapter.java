package com.shen.tree_view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shen.tree.bean.Node;
import com.shen.tree.bean.TreeListViewAdapter;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {


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
	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel)
			throws IllegalArgumentException, IllegalAccessException {

		super(mTree, context, datas, defaultExpandLevel);
	}


	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_id_treenode_icon);
			viewHolder.label = (TextView) convertView.findViewById(R.id.tv_id_treenode_label);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}


		if (node.getIcon() == -1) {								// 没有图片
			viewHolder.icon.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
		}

		viewHolder.label.setText(node.getName());
		
		
		return convertView;
	}


	private final class ViewHolder {
		ImageView icon;
		TextView label;
	}

}
