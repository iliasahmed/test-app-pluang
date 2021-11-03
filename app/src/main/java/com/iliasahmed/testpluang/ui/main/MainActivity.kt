package com.iliasahmed.testpluang.ui.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.iliasahmed.testpluang.R
import com.iliasahmed.testpluang.data.PreferenceRepository
import com.iliasahmed.testpluang.databinding.ActivityMainBinding
import com.iliasahmed.testpluang.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import com.google.firebase.auth.GoogleAuthProvider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.iliasahmed.testpluang.controller.AppController
import com.iliasahmed.testpluang.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener





@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    MainViewModel::class.java,
    R.layout.activity_main
), AppController.AppEvent {
    private val RC_SIGN_IN: Int = 1000
    private var navController: NavController? = null

    var wishSelected = false
    var accountSelected = false

    @Inject lateinit var preferenceRepository: PreferenceRepository
    @Inject lateinit var googleSignInClient : GoogleSignInClient
    @Inject lateinit var mAuth: FirebaseAuth
    private var mAuthListener: AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setupNavigation()
        setupBottomNav()
        AppController.setAppEvent(this)
    }

    private fun setupNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController!!.addOnDestinationChangedListener(OnDestinationChangedListener { controller: NavController?, destination: NavDestination, arguments: Bundle? ->
            if (destination.label != null && !destination.label.toString().toLowerCase()
                    .contains("bottomsheet")
            ) {

            }
        })
    }

    private fun setupBottomNav() {
        NavigationUI.setupWithNavController(binding!!.bottomNavigationView, navController!!)
        binding!!.bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.accountFragment) {
                if (navController == null) return@setOnItemSelectedListener false
                if (preferenceRepository.email.equals("")) {
                    wishSelected = false
                    accountSelected = true
                    signIn()
                }else return@setOnItemSelectedListener NavigationUI.onNavDestinationSelected(
                    item,
                    navController!!
                )

            }else if (item.itemId == R.id.homeFragment){
                navController!!.navigate(R.id.action_homeFragment_Pop)
                return@setOnItemSelectedListener false
            }else if (item.itemId == R.id.wishlistFragment) {
                if (navController == null) return@setOnItemSelectedListener false
                if (preferenceRepository.email.equals("")) {
                    wishSelected = true
                    accountSelected = false
                    signIn()
                }else return@setOnItemSelectedListener NavigationUI.onNavDestinationSelected(
                    item,
                    navController!!
                )
            }
            return@setOnItemSelectedListener false

//        binding!!.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            if (item.itemId === R.id.accountFragment) {
//                if (navController == null) return@setOnNavigationItemSelectedListener false
//                navController!!.navigate(R.id.action_homeFragment_to_accountFragment)
////                if (preferenceRepository.getToken().equals("")) startActivity(
////                    Intent(
////                        this@MainActivity,
////                        SignInActivity::class.java
////                    )
////                ) else return@setOnNavigationItemSelectedListener NavigationUI.onNavDestinationSelected(
////                    item,
////                    navController!!
////                )
//                return@setOnNavigationItemSelectedListener false
//            } else if (item.itemId === R.id.homeFragment) {
//                navController!!.navigate(R.id.action_homeFragment_Pop)
//                return@setOnNavigationItemSelectedListener false
//            } else return@setOnNavigationItemSelectedListener NavigationUI.onNavDestinationSelected(
//                item,
//                navController!!
//            )


        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    user!!.email?.let {
                        Log.e(TAG, it)
                        preferenceRepository.saveEmail(it)
                    }
                    user!!.displayName?.let {
                        Log.e(TAG, it)
                        preferenceRepository.saveUserName(it)
                    }

                    user!!.photoUrl?.let {
                        Log.e(TAG, it.toString())
                        preferenceRepository.saveImage(it.toString())
                    }

                    if (wishSelected){
                        navController!!.navigate(R.id.action_homeFragment_to_wishlistFragment)
                    }else if (accountSelected){
                        navController!!.navigate(R.id.action_homeFragment_to_accountFragment)
                    }

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }



    override fun initViews() {

    }

    override fun liveEventsObservers() {

    }

    override fun clickListeners() {

    }

    override fun onLogout() {
        preferenceRepository.clearAll()
        mAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this,
            OnCompleteListener<Void?> {
                navController!!.navigate(R.id.action_homeFragment_Pop)
            })
    }
}