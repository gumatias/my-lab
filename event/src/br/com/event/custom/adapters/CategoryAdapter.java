package br.com.event.custom.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import br.com.event.custom.R;
import br.com.event.custom.model.Category;

public class CategoryAdapter extends GenericListAdapter<Category> {

	public CategoryAdapter(Context context, List<Category> categories) {
		super(context, categories);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
        if (convertView == null) {
        	convertView = getLayoutInflater().inflate(R.layout.category_list_row, null);
            holder = new ViewHolder();
            holder.categoryChk	= (CheckedTextView)convertView.findViewById(R.id.category_chk);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        
        holder.categoryChk.setText(getList().get(position).getName());
        holder.categoryChk.setChecked(getList().get(position).isChecked());
        holder.categoryChk.setOnClickListener(new CategoryCheckedListener(getList().get(position)));
        return convertView;
	}
	
	class CategoryCheckedListener implements OnClickListener {

		private Category category;
		
		public CategoryCheckedListener(Category category) {
			this.category = category;
		}

		public void onClick(View v) {
			CheckedTextView checkBox = (CheckedTextView)v;
			checkBox.toggle();
			category.setChecked(checkBox.isChecked());
		}
		
	}

	private static class ViewHolder {
		public CheckedTextView categoryChk;
	}

}