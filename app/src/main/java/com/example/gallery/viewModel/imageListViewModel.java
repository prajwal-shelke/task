package com.example.gallery.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gallery.model.ImageData;
import com.example.gallery.model.Photo;
import com.example.gallery.model.Photos;
import com.example.gallery.network.APIService;
import com.example.gallery.network.RetroInstance;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class imageListViewModel extends ViewModel {

    private final MutableLiveData<List<Photo>> list;
    public imageListViewModel() {
        this.list = new MutableLiveData<>();
    }

    public MutableLiveData<List<Photo>> getImageListObserver() {
        return this.list;
    }

    public void makeAPICall(){
        APIService api = RetroInstance.getRetrofitClient().create(APIService.class);

        Call<ImageData> call = api.getImages();

        call.enqueue(new Callback<ImageData>() {
            @Override
            public void onResponse(@NotNull Call<ImageData> call, @NotNull Response<ImageData> response) {
                ImageData data = response.body();
                assert data != null;
                Photos photos = data.getPhotos();
                List<Photo> photo = photos.getPhoto();
                list.postValue(photo);
            }

            @Override
            public void onFailure(@NotNull Call<ImageData> call, @NotNull Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });


    }
}
