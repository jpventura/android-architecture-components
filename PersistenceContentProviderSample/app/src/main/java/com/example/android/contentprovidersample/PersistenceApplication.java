package com.example.android.contentprovidersample;

import android.app.Application;

import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.inspector.console.RuntimeReplFactory;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.facebook.stetho.rhino.JsRuntimeReplFactoryBuilder;

public class PersistenceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        InspectorModulesProvider provider = new InspectorModulesProvider() {
            @Override
            public Iterable<ChromeDevtoolsDomain> get() {
                RuntimeReplFactory factory = new JsRuntimeReplFactoryBuilder(PersistenceApplication.this)
                        .addVariable("foo", "bar")
                        .build();

                return new Stetho.DefaultInspectorModulesBuilder(PersistenceApplication.this)
                        .runtimeRepl(factory)
                        .finish();
            }
        };

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(provider)
                .build());
    }

}
