package com.iliasahmed.testpluang.di;


import android.app.Application;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.iliasahmed.testpluang.data.PreferenceRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    PreferenceRepository providePreferenceRepository(Context context, Gson gson) {
        return new PreferenceRepository(context, gson);
    }

    @Provides
    @Singleton
    FirebaseFirestore firebaseFirestore() {
        FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
        return mFirebaseFirestore;
    }

    @Provides
    @Singleton
    GoogleSignInOptions provideGoogleSignInOptions(Context context){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("677189332969-1u11drj7uk3atq473ca3q10liop00poo.apps.googleusercontent.com")
                .requestEmail()
                .build();
        return gso;
    }

    @Provides
    @Singleton
    GoogleSignInClient provideGoogleSignIn(GoogleSignInOptions gso, Context context){
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        return mGoogleSignInClient;
    }

    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth(){
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        return mFirebaseAuth;
    }

}
