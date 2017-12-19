package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthropicandroid.patterngallery.R;
import com.anthropicandroid.patterngallery.databinding.LayoutGalleryImageBinding;
import com.anthropicandroid.patterngallery.entities.interactions.PatternMetaData;
import com.anthropicandroid.patterngallery.entities.ui.SVGItemViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class GalleryListAdapter
        extends RecyclerView.Adapter {

    public static final String TAG = GalleryListAdapter.class.getSimpleName();

    private static Subscription patternSubscription;

    @BindingAdapter(value = {
            "numSpans",
            "patternGroup"})
    public static void setEntries(
            final GalleryActivityComponent galleryActivityComponent,
            RecyclerView view,
            final Integer numSpans,
            final String patternGroup
    ) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                Log.d(TAG, "attached gallery list view");
                Activity  activity  = (Activity) view.getContext();
                Resources resources = activity.getResources();

                // create and populate list adapter and give it to the view
                ((RecyclerView)view).setAdapter(new GalleryListAdapter(
                        galleryActivityComponent.getGalleryActionHandlers(),
                        galleryActivityComponent.getRepository().observePatterns(patternGroup).take(8),
                        (int) (galleryActivityComponent.getScreenWidthInPx() / numSpans  // Max width for a child
                                - resources.getDimension(R.dimen.gallery_padding) * 2 / numSpans  // Account for padding across spans
                                * galleryActivityComponent.getDisplayMetrics().xdpi / 160),  // Px-to-dp conversion factor
                        resources));

            }

            @Override
            public void onViewDetachedFromWindow(
                    View view
            ) {
                if (patternSubscription != null && !patternSubscription.isUnsubscribed()) patternSubscription
                        .unsubscribe();
            }
        });
    }


    private SortedList<PatternMetaData> patterns = new SortedList<>(
            PatternMetaData.class,
            new SortedList.Callback<PatternMetaData>() {
                @Override
                public int compare(
                        PatternMetaData o1,
                        PatternMetaData o2
                ) {
                    return o1.getPathPoints().length()-o2.getPathPoints().length();
                }

                @Override
                public void onInserted(
                        int position,
                        int count
                ) {
                    notifyDataSetChanged();
                }

                @Override
                public void onRemoved(
                        int position,
                        int count
                ) {
                    notifyDataSetChanged();
                }

                @Override
                public void onMoved(
                        int fromPosition,
                        int toPosition
                ) {
                    notifyDataSetChanged();
                }

                @Override
                public void onChanged(
                        int position,
                        int count
                ) {
                    notifyDataSetChanged();
                }

                @Override
                public boolean areContentsTheSame(
                        PatternMetaData oldItem,
                        PatternMetaData newItem
                ) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(
                        PatternMetaData item1,
                        PatternMetaData item2
                ) {
                    return item1.getPathPoints().equals(item2.getPathPoints());
                }
            });
    private GalleryActionHandlers galleryActionHandlers;
    private int maxChildWidth;
    private Resources resources;
    private TypedArray bgColors;

    private GalleryListAdapter(
            GalleryActionHandlers galleryActionHandlers,
            Observable<PatternMetaData> patternObservable,
            int maxChildWidth,
            Resources resources
    ) {

        this.galleryActionHandlers = galleryActionHandlers;
        this.maxChildWidth = maxChildWidth;
        this.resources = resources;
        this.bgColors = resources.obtainTypedArray(R.array.bg_colors);

        patternSubscription = patternObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PatternMetaData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "pattern observable completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error while presenting patterns: " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PatternMetaData patternMetaData) {
                        patterns.add(patternMetaData);
                        Log.d(TAG, "pattern observable onNext");
                    }
                });
    }

    private static class BindingHolder
            extends RecyclerView.ViewHolder {

        private LayoutGalleryImageBinding dataBinding;

        public BindingHolder(
                View rowView,
                GalleryActionHandlers galleryActionHandlers
        ) {
            // get tie-in to this view's databinding
            super(rowView);
            dataBinding = DataBindingUtil.bind(rowView);
            // add the binding objects
            dataBinding.setViewModel(new SVGItemViewModel());
            dataBinding.setGalleryActionHandlers(galleryActionHandlers);
        }

        public LayoutGalleryImageBinding getDataBinding() {
            return dataBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {
        // inflate view
        LayoutGalleryImageBinding galleryImageBinding = LayoutGalleryImageBinding.inflate(
                (LayoutInflater) parent.getContext().getSystemService(LAYOUT_INFLATER_SERVICE),
                parent,
                false);
        return new BindingHolder(galleryImageBinding.getRoot(), galleryActionHandlers);
    }

    @Override
    public void onBindViewHolder(
            RecyclerView.ViewHolder holder,
            int position
    ) {
        // Sets item-specific data
        if (holder instanceof BindingHolder) {
            LayoutGalleryImageBinding galleryImageBinding = ((BindingHolder) holder)
                    .getDataBinding();
            SVGItemViewModel svgItemViewModel = galleryImageBinding.getViewModel();
            svgItemViewModel.setColorResId(bgColors.getResourceId(position % 8, R.color.colorOrange));
            svgItemViewModel.setMaxChildWidth(maxChildWidth);
            svgItemViewModel.setName("Pattern " + position);
            svgItemViewModel.setLastKnownHeight(400);
            svgItemViewModel.setLastKnownWidth(900);

            Log.d(getClass().getSimpleName(), "at position: "+position+" and the count is: "+patterns.size());
            if (position < patterns.size())
                assignMetaData(svgItemViewModel, patterns.get(position));
        }
    }

    private void assignMetaData(
            SVGItemViewModel svgItemViewModel,
            PatternMetaData patternMetaData
    ) {
        Log.d(getClass().getSimpleName(), "assigning a data");
        svgItemViewModel.setLastKnownWidth(patternMetaData.getLastKnownWidth());
        svgItemViewModel.setLastKnownHeight(patternMetaData.getLastKnownHeight());
        svgItemViewModel.setPathPoints(patternMetaData.getPathPoints());
        svgItemViewModel.setWellBehaved(patternMetaData.getWellBehaved());
        svgItemViewModel.setName(patternMetaData.getName());
    }

    @Override
    public int getItemCount() {
        return patterns.size();
    }
}
