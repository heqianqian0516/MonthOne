package presenter;

import java.util.Map;

import callBack.MyCallBack;
import model.ModelImp;
import view.Iview;

public class PresenterImp implements IPresenter {
     private Iview mIview;
    private ModelImp model;

    public PresenterImp(Iview iview){
         this.mIview=iview;
        model = new ModelImp();
     }
    @Override
    public void startRequestGet(String url, Class clazz) {
        model.onRequestGet(url, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object o) {
                mIview.onRequestSuccess(o);
            }

            @Override
            public void onFail(String error) {
                mIview.onRequestFail(error);
            }
        });
    }

    @Override
    public void startRequestPost(String url, Map map, Class clazz) {
          model.onRequestPost(url, map, clazz, new MyCallBack() {
              @Override
              public void onSuccess(Object o) {
                  mIview.onRequestSuccess(o);
              }

              @Override
              public void onFail(String error) {
                mIview.onRequestFail(error);
              }
          });
    }
    public void onDeath(){
        if (mIview!=null){
            mIview=null;
        }
        if (model!=null){
            model=null;
        }
    }
}
