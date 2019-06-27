package com.store.stack;

import android.app.Application;
import android.support.annotation.Nullable;

import com.facebook.flipper.android.AndroidFlipperClient;
import com.facebook.flipper.core.FlipperClient;
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin;
import com.facebook.flipper.plugins.inspector.DescriptorMapping;
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin;
import com.facebook.flipper.plugins.leakcanary.LeakCanaryFlipperPlugin;
import com.facebook.flipper.plugins.leakcanary.RecordLeakService;
import com.facebook.flipper.plugins.litho.LithoFlipperDescriptors;
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor;
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin;
import com.facebook.flipper.plugins.sandbox.SandboxFlipperPlugin;
import com.facebook.flipper.plugins.sandbox.SandboxFlipperPluginStrategy;
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin;
import com.facebook.litho.config.ComponentsConfiguration;
import com.facebook.soloader.SoLoader;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MyApplication  extends Application {

    @Nullable
    public static OkHttpClient sOkHttpClient = null;

    @Override
    public void onCreate() {
        super.onCreate();

        SoLoader.init(this, false);

        final FlipperClient client = AndroidFlipperClient.getInstance(this);
        final DescriptorMapping descriptorMapping = DescriptorMapping.withDefaults();

        final NetworkFlipperPlugin networkPlugin = new NetworkFlipperPlugin();
        final FlipperOkhttpInterceptor interceptor = new FlipperOkhttpInterceptor(networkPlugin);

        sOkHttpClient = new OkHttpClient.Builder()
                        .addNetworkInterceptor(interceptor)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.MINUTES)
                        .build();

        ComponentsConfiguration.isDebugModeEnabled = true;
        LithoFlipperDescriptors.add(descriptorMapping);

        client.addPlugin(networkPlugin);
        client.addPlugin(new InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()));

        client.addPlugin(new SandboxFlipperPlugin(new SandboxFlipperPluginStrategy() {

            @Nullable
            @Override
            public Map<String, String> getKnownSandboxes() {
                Map map = new HashMap();
                return map;
            }

            @Override
            public void setSandbox(@Nullable String sandbox) {

            }
        }));

        client.addPlugin(new SharedPreferencesFlipperPlugin(this, "flipper_shared_preference"));
        client.addPlugin(new LeakCanaryFlipperPlugin());
        client.addPlugin(CrashReporterPlugin.getInstance());
        client.start();

        LeakCanary.refWatcher(this)
                .listenerServiceClass(RecordLeakService.class)
                .buildAndInstall();
    }

}
