package br.com.etecia.iservice;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class FragmentHome extends Fragment {

    private ViewPager2 viewPager;
    private Handler autoScrollHandler = new Handler();
    private Runnable autoScrollRunnable;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);

        // Configurar o ViewPager2
        viewPager = view.findViewById(R.id.viewPager);

        // Lista de imagens
        List<Integer> images = Arrays.asList(
                R.drawable.Banana
        );

        // Configurar o adapter
        CarouselAdapter adapter = new CarouselAdapter(images);
        viewPager.setAdapter(adapter);

        // Configurar auto-scroll
        setupAutoScroll();

        return view;
    }

    private void setupAutoScroll() {
        final long AUTO_SCROLL_DELAY = 3000; // 3 segundos

        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager != null && viewPager.getAdapter() != null) {
                    int currentItem = viewPager.getCurrentItem();
                    int totalItems = viewPager.getAdapter().getItemCount();
                    viewPager.setCurrentItem((currentItem + 1) % totalItems, true);
                    autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
                }
            }
        };

        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                autoScrollHandler.removeCallbacks(autoScrollRunnable);
                autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpar handlers para evitar memory leaks
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
        if (viewPager != null) {
            viewPager.unregisterOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {});
        }
    }

    // Adapter para o ViewPager2
    private static class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
        private final List<Integer> images;

        public CarouselAdapter(List<Integer> images) {
            this.images = images;
        }

        @NonNull
        @Override
        public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.iten_carossel_layout, parent, false);
            return new CarouselViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
            holder.imageView.setImageResource(images.get(position));
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        static class CarouselViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public CarouselViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}