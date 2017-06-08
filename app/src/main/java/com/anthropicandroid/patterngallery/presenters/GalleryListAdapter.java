package com.anthropicandroid.patterngallery.presenters;

/*
 * Created by Andrew Brin on 7/13/2016.
 */

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
import com.anthropicandroid.patterngallery.entities.ui.GalleryItemViewModel;
import com.anthropicandroid.patterngallery.routers.gallery.GalleryActivityComponent;
import com.anthropicandroid.patterngallery.entities.ui.RawBitmapMeasurement;

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
            GalleryActivityComponent galleryActivityComponent,
            RecyclerView view,
            Integer numSpans,
            String patternGroup
    ) {
        // create and populate list adapter and give it to the view
        view.setAdapter(new GalleryListAdapter(
                galleryActivityComponent.getGalleryActionHandlers(),
                galleryActivityComponent.getRepository().observePatterns(patternGroup),
                galleryActivityComponent.getNarrowestScreenDimenInPx() / numSpans,
                view.getContext().getResources().obtainTypedArray(R.array.bg_colors)));

        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                Log.d(TAG, "I think the program will never get here");
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


    private GalleryActionHandlers galleryActionHandlers;
    private int childWidth;
    private TypedArray bgColors;
    private SortedList<PatternMetaData> patterns = new SortedList<>(
            PatternMetaData.class,
            new SortedList.Callback<PatternMetaData>() {
                @Override
                public int compare(
                        PatternMetaData o1,
                        PatternMetaData o2
                ) {
                    return 0;
                }

                @Override
                public void onInserted(
                        int position,
                        int count
                ) {
                    onInserted(position, count);
                }

                @Override
                public void onRemoved(
                        int position,
                        int count
                ) {
                    onRemoved(position, count);
                }

                @Override
                public void onMoved(
                        int fromPosition,
                        int toPosition
                ) {
                    onMoved(fromPosition, toPosition);
                }

                @Override
                public void onChanged(
                        int position,
                        int count
                ) {
                    onChanged(position, count);
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
                    return item1.getOriginalUri().equals(item2.getOriginalUri());
                }
            },
            8);

    private GalleryListAdapter(
            GalleryActionHandlers galleryActionHandlers,
            Observable<PatternMetaData> patternObservable,
            int childWidth,
            TypedArray bgColors
    ) {
        // class constructed by an annotated static function

        this.galleryActionHandlers = galleryActionHandlers;
        this.childWidth = childWidth;
        this.bgColors = bgColors;

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
                        Log.d(TAG, "gallery list adapter");
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
            dataBinding.setViewModel(new GalleryItemViewModel());
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

            GalleryItemViewModel galleryItemViewModel = galleryImageBinding.getViewModel();
            galleryItemViewModel.setRawBitmapMeasurement(new RawBitmapMeasurement());
            galleryItemViewModel.setWidth(childWidth);
            galleryItemViewModel.setDescription("Photo " + position);
            galleryItemViewModel.setColorResId(bgColors.getResourceId(position % 8, R.color.colorOrange));
        }
    }

    @Override
    public int getItemCount() {
        return patterns.size();
    }
}
