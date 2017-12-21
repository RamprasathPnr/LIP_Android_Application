package com.omneagate.lip.Service;

/**
 *Created by Sathya Rathinavelu on 5/9/2016.
 */
public interface EasyObservable<T> {

    void addListener(OnChangeListener<T> listener);
    void removeListener(OnChangeListener<T> listener);

}