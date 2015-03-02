package xiaofan.insdownloader.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import at.markushi.ui.CircleButton;
import xiaofan.insdownloader.R;

/**
 * Created by zhaoyu on 2015/3/2.
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder>{

    private List<String> downloadPaths;

    public DownloadAdapter(List<String> downloadPaths) {
        this.downloadPaths = downloadPaths;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_download,parent,false);
        return ViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = downloadPaths.get(position);
        holder.displayImage(path);
    }

    @Override
    public int getItemCount() {
        return downloadPaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final CircleButton circleButton;
        public static ViewHolder newInstance(View itemView) {
            ImageView imageView = (ImageView) itemView.findViewById(R.id.download_pic_iv);
            CircleButton circleButton = (CircleButton) itemView.findViewById(R.id.btn_share);
            return new ViewHolder(itemView,imageView,circleButton);
        }

        public ViewHolder(View itemView,ImageView imageView,CircleButton circleButton) {
            super(itemView);
            this.imageView = imageView;
            this.circleButton = circleButton;
        }

        public void displayImage(final String path){
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap =  BitmapFactory.decodeFile(path);
                    imageView.setImageBitmap(bitmap);
                    applyShareButtonColor(bitmap);
                }

            });
        }

        private void applyShareButtonColor(Bitmap bitmap) {
            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    circleButton.setColor(palette.getVibrantColor(Color.argb(0, 255, 255, 255)));
                }
            });
        }

    }
}
