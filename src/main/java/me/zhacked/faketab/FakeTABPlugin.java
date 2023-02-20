package me.zhacked.faketab;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.zhacked.faketab.module.MainModule;
import me.zhacked.faketab.service.Service;
import org.bukkit.plugin.java.JavaPlugin;

public final class FakeTABPlugin extends JavaPlugin {

    private Service service;

    @Override
    public void onEnable() {
        Injector inject = Guice.createInjector(new MainModule(this));
        inject.injectMembers(this);

        service = inject.getInstance(Service.class);
        service.start();
    }

    @Override
    public void onDisable() {
        service.stop();
    }
}
