package com.dot3digital.ui.real.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dot3digital.R;
import com.dot3digital.framework.model.ViewCats;
import com.dot3digital.ui.real.Shared;
import com.dot3digital.ui.real.fragment.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @description ViewEntry Category Sub Fragment
 *
 * @author      Stelian
 */

public class ViewDiscoverDefaultEntryFragment extends BaseFragment
{
    View mRootView;         // Root ViewEntry

    ViewCats mViewCats;     // ViewCats From Parameter

    ImageView ivBack;       // Back Button
    TextView tvTitle;       // Title
    TextView tvContent;     // Content
    ImageView ivImage;      // Image

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_discover_default_view_entry, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewCats = getArguments().getParcelable("item");

        ivBack = (ImageView) mRootView.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.remove(ViewDiscoverDefaultEntryFragment.this);
                transaction.commit();
            }
        });

        String title = mViewCats.getName();

        tvTitle = (TextView) mRootView.findViewById(R.id.tvTitle);
        if (!title.isEmpty())
            tvTitle.setText(title);
        else {
            tvTitle.setVisibility(View.GONE);
            ((View) mRootView.findViewById(R.id.layoutSeparator)).setVisibility(View.VISIBLE);
        }

        tvContent = (TextView) mRootView.findViewById(R.id.tvContent);
        tvContent.setText(mViewCats.getText());

        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        ImageLoader.getInstance().displayImage(mViewCats.getImage(), ivImage, Shared.gImageOption);
    }
}
