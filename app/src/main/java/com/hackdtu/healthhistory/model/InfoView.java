package com.hackdtu.healthhistory.model;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackdtu.healthhistory.R;
import com.hackdtu.healthhistory.activity.ImageDisplay;
import com.hackdtu.healthhistory.utils.Constants;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.ChildPosition;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;
import com.squareup.picasso.Picasso;

/**
 * Created by Nimit Agg on 11-02-2017.
 */

@Layout(R.layout.feed_item)
public class InfoView {

    @ParentPosition
    private int mParentPosition;

    @ChildPosition
    private int mChildPosition;

    @View(R.id.titleTxt)
    private TextView titleTxt;

    @View(R.id.captionTxt)
    private TextView captionTxt;

    @View(R.id.timeTxt)
    private TextView timeTxt;

    @View(R.id.imageView)
    private ImageView imageView;

    private UserHistory userHistory;
    private Context mContext;

    public InfoView(Context context, UserHistory userHistory) {
        mContext = context;
        this.userHistory = userHistory;
    }

    @Resolve
    private void onResolved() {
        titleTxt.setText(userHistory.getImage_title());
        captionTxt.setText("Look at this");
        //timeTxt.setText(mInfo.getTime());
        Log.e("dwjn",userHistory.getHistory_pic());
        Picasso.with(mContext).load(Constants.IMAGE_URL+userHistory.getHistory_pic()).into(imageView);
        //Picasso.with(mContext).load(R.mipmap.ic_launcher).into(imageView);
    }
    @Click(R.id.ll)
    private void onClick(){
        Intent intent = new Intent(mContext, ImageDisplay.class);
        Log.e("k","mk");
        intent.putExtra("path",Constants.IMAGE_URL+userHistory.getHistory_pic());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
