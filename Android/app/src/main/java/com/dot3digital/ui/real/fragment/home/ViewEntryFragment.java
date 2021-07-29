package com.dot3digital.ui.real.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.model.BaseModel;
import com.dot3digital.framework.model.EntryForViewCat;
import com.dot3digital.framework.model.ViewCats;
import com.dot3digital.ui.real.Shared;
import com.dot3digital.ui.real.fragment.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @description ViewEntry Category Sub Fragment
 *
 * @author      Stelian
 */

public class ViewEntryFragment extends BaseFragment
{
    View mRootView;         // Root ViewEntry

    ImageView ivBack;       // Back Button
    TextView ivTitle;       // Title
    TextView tvContent;     // Content
    ImageView ivImage;      // Image

    // Variables
    BaseModel mEntry;        // Entry From Parameter (maybe it can be either of ViewCats or EntryForViewCat)

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home_view_entry, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivBack = (ImageView) mRootView.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.remove(ViewEntryFragment.this);
                transaction.commit();
            }
        });

        mEntry = getArguments().getParcelable("item");

        String title = "", text = "", image = "";
        if (mEntry instanceof ViewCats) {
            ViewCats viewCats = (ViewCats) mEntry;
            title = viewCats.getName();
            text = viewCats.getText();
            image = viewCats.getImage();
        } else if (mEntry instanceof EntryForViewCat) {
            EntryForViewCat entryForViewCat = (EntryForViewCat) mEntry;
            title = entryForViewCat.getHeadline();
            text = entryForViewCat.getText();
            image = entryForViewCat.getImage();
        }

        ivTitle = (TextView) mRootView.findViewById(R.id.ivTitle);
        ivTitle.setText(title);

        tvContent = (TextView) mRootView.findViewById(R.id.tvContent);
        tvContent.setText(text);

        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageLoader.getInstance().displayImage(image, ivImage, Shared.gImageOption);
    }
}
