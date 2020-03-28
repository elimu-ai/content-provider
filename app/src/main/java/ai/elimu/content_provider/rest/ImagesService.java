package ai.elimu.content_provider.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ImagesService {

    @GET("content/images")
    Call<ResponseBody> listImages();
}
