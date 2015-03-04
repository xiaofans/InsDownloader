package xiaofan.insdownloader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import xiaofan.insdownloader.R;

/**
 * Created by zhaoyu on 2015/3/4.
 */
public class GuideAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<String> steps;
    private Context context;

    public GuideAdapter(List<String> steps, Context context) {
        this.steps = steps;
        this.context = context;
    }

    @Override
    public View getHeaderView(int position, View view, ViewGroup viewGroup) {
        HeaderViewHolder headerViewHolder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_guide_header,viewGroup,false);
            headerViewHolder = new HeaderViewHolder();
            headerViewHolder.textView = (TextView) view.findViewById(R.id.tv_step);
            view.setTag(headerViewHolder);
        }else{
            headerViewHolder = (HeaderViewHolder) view.getTag();
        }
        headerViewHolder.textView.setText(steps.get(position));
        return view;
    }

    @Override
    public long getHeaderId(int position) {
         return steps.get(position).subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public String getItem(int position) {
        return steps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_guide,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.guide_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position == 0){
            viewHolder.imageView.setImageResource(R.drawable.step_1_1);
        }else if(position == 1){
            viewHolder.imageView.setImageResource(R.drawable.step_2_1);
        }else if(position == 2){
            viewHolder.imageView.setImageResource(R.drawable.step_3_1);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
    }

    class HeaderViewHolder{
        TextView textView;
    }
}
