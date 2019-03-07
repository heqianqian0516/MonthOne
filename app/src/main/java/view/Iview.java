package view;

public interface Iview<T> {
    void onRequestSuccess(T t);
    void onRequestFail(String error);
}
