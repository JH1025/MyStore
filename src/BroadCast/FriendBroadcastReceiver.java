package BroadCast;

import java.util.ArrayList;

import UserInfo.OtherUser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FriendBroadcastReceiver extends BroadcastReceiver {
	public static final String BURN = "com.MyStore.alien.action.NEW_FRIEND";
	public ArrayList<OtherUser> friends = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Get the lifeform details from the intent.
		friends.add((OtherUser) intent.getSerializableExtra("newFriend"));
		

	}
	
	public void setFriends(ArrayList<OtherUser> friends){
		this.friends = friends;
	}
	
}

