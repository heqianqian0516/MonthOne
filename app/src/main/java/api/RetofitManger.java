package api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetofitManger {
    public static String BASE_URL="";
    private static RetofitManger manger;
    private final BaseApis baseApis;

    //单例
    public static RetofitManger getInstance(){
        if (manger==null){
            synchronized (RetofitManger.class){
                manger = new RetofitManger();
            }
        }
        return manger;
    }
    private RetofitManger(){
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(15,TimeUnit.SECONDS);
        builder.readTimeout(15,TimeUnit.SECONDS);
        builder.writeTimeout(15,TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(interceptor);
        OkHttpClient client=builder.build();
        Retrofit retrofit=new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        baseApis = retrofit.create(BaseApis.class);
    }
    //get
    public void get(String url,HttpListener httpListener){
        baseApis.get(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(httpListener));
    }
   // post
    public void post(String url, Map<String,String>map,HttpListener httpListener){
        if (map==null){
            map=new HashMap<>();
        }
        baseApis.post(url,map)
             .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver(httpListener));

    }
    private Observer getObserver(final HttpListener httpListener){
        Observer observer= new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (httpListener!=null){
                    httpListener.onFail(e.getMessage());
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data=responseBody.string();
                    if (httpListener!=null){
                        httpListener.onSuccess(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (httpListener!=null){
                        httpListener.onFail(e.getMessage());
                    }
                }
            }
        };
        return observer;
    }
    //定义接口
    public interface HttpListener{
        void onSuccess(String data);
        void onFail(String error);
    }
}
