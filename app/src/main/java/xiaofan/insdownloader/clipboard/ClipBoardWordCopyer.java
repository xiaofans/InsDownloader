package xiaofan.insdownloader.clipboard;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.Stack;

import xiaofan.insdownloader.UserConfirmActivity;
import xiaofan.insdownloader.insparser.Parser;

/**
 * Created by zhaoyu on 2015/2/27.
 */
public class ClipBoardWordCopyer {
    private Context context;
    private Stack<String> copyUrlStack;
    private static final String INS_URL = "https://instagram.com";

    public ClipBoardWordCopyer(Context context) {
        this.context = context;
        copyUrlStack = new Stack<String>();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startListeningClipBoard(){
        final ClipboardManager clipBoard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener(){

            @Override
            public void onPrimaryClipChanged() {
                String copyedText = clipBoard.getText().toString();
                Log.w("ClipBoardWordCopyer",copyedText);
                if(!TextUtils.isEmpty(copyedText) && copyedText.startsWith(INS_URL)){
                    if(!copyUrlStack.contains(copyedText)){
                        copyUrlStack.add(copyedText);
                    }
                    showConfirmAct();
                }
            }
        });
    }

    private void showConfirmAct() {
        if(copyUrlStack != null && copyUrlStack.size() > 0){
            String pageUrl = copyUrlStack.pop();
            context.startActivity(UserConfirmActivity.newIntent(context,pageUrl));
        }
    }


}
