package me.zhacked.faketab.loader;

import com.google.inject.Inject;

public class MainLoader implements Loader {

    @Inject private ListenerLoader listenerLoader;
    @Inject private InterceptorLoader interceptorLoader;

    @Override
    public void load() {
        listenerLoader.load();
        interceptorLoader.load();
    }
}
