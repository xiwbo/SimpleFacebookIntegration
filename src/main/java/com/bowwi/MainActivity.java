package com.bowwi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.content.Context;
import android.view.View;
import android.util.Log;
import android.content.Intent;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import android.widget.ImageView;
import com.facebook.HttpMethod;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.AccessToken;
import com.facebook.Profile;
import org.json.JSONObject;


public class MainActivity extends Activity
{
	TextView info;
	LoginButton loginButton;
	ImageView imageView;
	private CallbackManager callbackManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(this.getApplicationContext());
		setContentView(R.layout.main);
		callbackManager = CallbackManager.Factory.create();
		info = (TextView)findViewById(R.id.info);
		loginButton = (LoginButton)findViewById(R.id.login_button);
		imageView = (ImageView)findViewById(R.id.imageView);

		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				AccessToken acToken = loginResult.getAccessToken();
				System.out.println("ID: " + loginResult.getAccessToken().getUserId());
				String userId = loginResult.getAccessToken().getUserId();
				System.out.println("Token: " + loginResult.getAccessToken().getToken());
				String token = loginResult.getAccessToken().getToken();
				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,name,picture");
				new GraphRequest(acToken,userId,parameters,HttpMethod.GET, new GraphRequest.Callback() {
					@Override
					public void onCompleted(GraphResponse response) {
						final Profile profile = Profile.getCurrentProfile();
						try {
							String picLink = profile.getCurrentProfile().getProfilePictureUri(200, 200).toString();
							info.setText("LOGIN SUCCESSFULLY \n Name: " + response.getJSONObject().get("name"));
							new MyTask(imageView).execute(picLink);
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
				}).executeAsync();
			}
			@Override
			public void onCancel() {
				info.setText("Login attempt canceled.");
			}
			@Override
			public void onError(FacebookException e) {
				info.setText("Login attempt failed.");
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
