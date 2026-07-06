package com.example.aplikasiseduhteh;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritManager {

    private static final Set<Integer> favoritIds = new HashSet<>();

    public static boolean isFavorit(int produkId) {
        return favoritIds.contains(produkId);
    }

    public static void clear() {
        favoritIds.clear();
    }
    public static void setFromModels(List<TehModel> models) {
        favoritIds.clear();
        if (models != null) {
            for (TehModel m : models) {
                favoritIds.add(m.getId());
            }
        }
    }
    public static void refresh(Context ctx, Runnable onComplete) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(ctx);

        api.getFavorit(session.gettoken()).enqueue(new Callback<TehResponse>() {
            @Override
            public void onResponse(@NonNull Call<TehResponse> call, @NonNull Response<TehResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getData() != null) {
                    favoritIds.clear();
                    List<TehModel> data = response.body().getData();
                    for (TehModel model : data) {
                        favoritIds.add(model.getId());
                    }
                }
                if (onComplete != null) onComplete.run();
            }

            @Override
            public void onFailure(@NonNull Call<TehResponse> call, @NonNull Throwable t) {
                if (onComplete != null) onComplete.run();
            }
        });
    }

    public static void toggle(Context ctx, int produkId, boolean newStatus) {
        if (newStatus) {
            favoritIds.add(produkId);
        } else {
            favoritIds.remove(produkId);
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        SessionManager session = new SessionManager(ctx);
        Callback<ResponseBody> noop = new Callback<ResponseBody>() {
            @Override public void onResponse(@NonNull Call<ResponseBody> c, @NonNull Response<ResponseBody> r) {}
            @Override public void onFailure(@NonNull Call<ResponseBody> c, @NonNull Throwable t) {}
        };

        if (newStatus) {
            api.tambahFavorit(session.gettoken(), produkId).enqueue(noop);
        } else {
            api.hapusFavorit(produkId, session.gettoken()).enqueue(noop);
        }
    }
}
