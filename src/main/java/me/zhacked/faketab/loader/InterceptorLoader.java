package me.zhacked.faketab.loader;

import com.google.inject.Inject;
import me.zhacked.faketab.messenger.join.JoinMessage;
import me.zhacked.faketab.messenger.join.JoinMessageInterceptor;
import me.zhacked.faketab.messenger.quit.QuitMessage;
import me.zhacked.faketab.messenger.quit.QuitMessageInterceptor;
import net.ibxnjadev.vmessenger.universal.Messenger;

public class InterceptorLoader implements Loader{

    @Inject private Messenger messenger;
    @Inject private JoinMessageInterceptor joinMessageInterceptor;
    @Inject private QuitMessageInterceptor quitMessageInterceptor;

    @Override
    public void load() {
        messenger.intercept(JoinMessage.class, joinMessageInterceptor);
        messenger.intercept(QuitMessage.class, quitMessageInterceptor);
    }
}
