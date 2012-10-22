package br.com.event.custom.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class GenericListAdapter<T> extends BaseAdapter {

	protected List<T> list;
	private LayoutInflater inflater;
	private Context context;

	public GenericListAdapter(Context context, List<T> list) {
		inflater = LayoutInflater.from(context);
		this.setContext(context);
		this.list = list;
	}
	
	public int getCount() {
		return getList().size();
	}

	public Object getItem(int position) {
		return getList().get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public LayoutInflater getLayoutInflater() {
		return inflater;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public static class ViewHolder {
        public TextView titleTxt;
    }

}