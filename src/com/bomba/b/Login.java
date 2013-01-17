package com.bomba.b;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bomba.R;
import com.facebook.*;
import com.facebook.model.GraphUser;

public class Login extends Activity {

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		tv = (TextView) findViewById(R.id.welcome);

		Session.openActiveSession(this, true, new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {

								

								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									tv.setText(user.getName()+", Thank you for loggin in with facebook");
									startActivity(new Intent(Login.this, MainActivity.class));
									
								}
							});
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}

}
