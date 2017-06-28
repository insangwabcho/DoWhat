package com.comnawa.dowhat.insang;

/**
 * Created by insang on 2017. 6. 28..
 */

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = "MyFirebaseIIDService";

  // [START refresh_token]
  @Override
  public void onTokenRefresh() {
    // Get updated InstanceID token.
    String token = FirebaseInstanceId.getInstance().getToken();

  }

}