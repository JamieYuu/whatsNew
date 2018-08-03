package com.mad.whatsnew;

/**
 * Interface base view
 */
public interface BaseView<T> {
    /**
     * Set view presenter
     * @param presenter the presenter
     */
    void setPresenter(T presenter);
}
