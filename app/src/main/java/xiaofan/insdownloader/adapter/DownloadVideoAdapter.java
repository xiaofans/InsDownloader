package xiaofan.insdownloader.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import at.markushi.ui.CircleButton;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import xiaofan.insdownloader.PhotoViewActivity;
import xiaofan.insdownloader.R;

/**
 * Created by zhaoyu on 2015/3/2.
 */
public class DownloadVideoAdapter extends RecyclerView.Adapter<DownloadVideoAdapter.ViewHolder>{

    private List<String> downloadPaths;
    private Context context;

    public DownloadVideoAdapter(Context context, List<String> downloadPaths) {
        this.downloadPaths = downloadPaths;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_video_download,parent,false);
        return ViewHolder.newInstance(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String path = downloadPaths.get(position);
        holder.displayImage(path);
        holder.circleButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                shareAction(path);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
              //  context.startActivity(PhotoViewActivity.newIntent(context,path));
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/mp4");
                context.startActivity(intent);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override public void onClick(View v) {
                String path = downloadPaths.get(position);
                new File(path).delete();
                downloadPaths.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/mp4");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadPaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final CircleButton circleButton;
        private final CircleButton deleteButton;
        private final CircleButton playButton;
        private Context context;
        public static ViewHolder newInstance(View itemView,Context context) {
            ImageView imageView = (ImageView) itemView.findViewById(R.id.download_pic_iv);
            CircleButton circleButton = (CircleButton) itemView.findViewById(R.id.btn_share);
            CircleButton deleteButton = (CircleButton) itemView.findViewById(R.id.btn_delete);
            CircleButton playButton = (CircleButton) itemView.findViewById(R.id.btn_play);
            return new ViewHolder(itemView,imageView,circleButton,deleteButton,playButton,context);
        }

        public ViewHolder(View itemView,ImageView imageView,CircleButton circleButton,CircleButton deleteButton,CircleButton playButton,Context context) {
            super(itemView);
            this.imageView = imageView;
            this.circleButton = circleButton;
            this.deleteButton = deleteButton;
            this.playButton = playButton;
            this.context = context;
        }

        public void displayImage(final String path){
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Video.Thumbnails.MINI_KIND);
            imageView.setImageBitmap(bitmap);
        }

    }

    private void shareAction(String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        shareIntent.setType("video/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    public void setDownloadPaths(ArrayList<String> downloadPaths) {
        this.downloadPaths = downloadPaths;
        notifyDataSetChanged();
    }
}
