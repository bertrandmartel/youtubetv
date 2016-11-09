/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package fr.bmartel.youtubetv.showcase.cards.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v17.leanback.widget.ImageCardView;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.squareup.picasso.Picasso;

import fr.bmartel.youtubetv.showcase.R;
import fr.bmartel.youtubetv.showcase.model.Card;
import fr.bmartel.youtubetv.showcase.samples.YoutubeActivityApiShowcase;
import fr.bmartel.youtubetv.showcase.samples.YoutubeActivityFragment;
import fr.bmartel.youtubetv.showcase.samples.YoutubeTvViewDebug;
import fr.bmartel.youtubetv.showcase.samples.YoutubeTvViewFullScreen;
import fr.bmartel.youtubetv.showcase.samples.YoutubeTvViewSplitted;

/**
 * A very basic {@link ImageCardView} {@link android.support.v17.leanback.widget.Presenter}.You can
 * pass a custom style for the ImageCardView in the constructor. Use the default constructor to
 * create a Presenter with a default ImageCardView style.
 */
public class ImageCardViewPresenter extends AbstractCardPresenter<ImageCardView> {

    private Card.Type mType;

    public ImageCardViewPresenter(Context context, int cardThemeResId) {
        super(new ContextThemeWrapper(context, cardThemeResId));
    }

    public ImageCardViewPresenter(Context context, Card.Type type) {
        this(context, R.style.DefaultCardTheme);
        mType = type;
    }

    public ImageCardViewPresenter(Context context) {
        this(context, R.style.DefaultCardTheme);
    }

    @Override
    protected ImageCardView onCreateView() {
        ImageCardView imageCardView = new ImageCardView(getContext());
        imageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;

                switch (mType) {
                    case YOUTUBETV_FRAGMENT:
                        intent = new Intent(getContext(), YoutubeActivityFragment.class);
                        getContext().startActivity(intent);
                        break;
                    case YOUTUBETV_FULLSCREEN:
                        intent = new Intent(getContext(), YoutubeTvViewFullScreen.class);
                        getContext().startActivity(intent);
                        break;
                    case YOUTUBETV_API:
                        intent = new Intent(getContext(), YoutubeActivityApiShowcase.class);
                        getContext().startActivity(intent);
                        break;
                    case YOUTUBETV_SPLITTED:
                        intent = new Intent(getContext(), YoutubeTvViewSplitted.class);
                        getContext().startActivity(intent);
                        break;
                    case YOUTUBETV_DEBUG:
                        intent = new Intent(getContext(), YoutubeTvViewDebug.class);
                        getContext().startActivity(intent);
                        break;
                }
            }
        });
        return imageCardView;
    }

    @Override
    public void onBindViewHolder(Card card, final ImageCardView cardView) {
        cardView.setTag(card);
        cardView.setTitleText(card.getTitle());
        cardView.setContentText(card.getDescription());
        if (card.getLocalImageResourceName() != null) {
            int resourceId = getContext().getResources()
                    .getIdentifier(card.getLocalImageResourceName(),
                            "drawable", getContext().getPackageName());
            Picasso.with(getContext()).load(resourceId).into(cardView.getMainImageView());
        }
    }

}
