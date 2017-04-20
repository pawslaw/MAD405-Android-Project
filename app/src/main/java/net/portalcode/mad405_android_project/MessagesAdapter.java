package net.portalcode.mad405_android_project;

/**
 * Created by James Pierce on 3/24/2017.
 */


// Utilizing a guide found on https://guides.codepath.com/android/using-the-recyclerview for RecyclerView
// Need a reycler view because of the content in the list

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * description This is the adapter class I use for the messages and the RecyclerView.
 */
public class MessagesAdapter extends
        RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_NIGHT_MODE = "night_mode";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare variables for the items to display in each row
        public TextView name;
        public TextView time;
        public TextView message;
        public ImageView avatar;
        public CardView card;

        // Constructor for the view
        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            message = (TextView) itemView.findViewById(R.id.message);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            card = (CardView) itemView.findViewById(R.id.messageCardView);
        }
    }

    // Creates a list to store the all of the messages
    private List<Message> mMessages;
    private Context mContext;

    // Pass the array of messages
    public MessagesAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mContext = context;
    }

    /**
     *
     * @return mContext - Returns the context when needed
     */
    private Context getContext() {
        return mContext;
    }


    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.message_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesAdapter.ViewHolder viewHolder, int position) {
        // This code will switch the color scheme for the messages if the theme has been changed.

        // This is the preferences file the user can make changes to
        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // This is the preferences file the login information is stored in
        SharedPreferences sharedPref = MainActivity.sharedPref;

        // Gets the item from the current position
        Message message = mMessages.get(position);
        DatabaseHandler db = new DatabaseHandler(getContext());

        // Set the Name for each message based on the userID of the current message
        TextView name = viewHolder.name;
        TextView time = viewHolder.time;
        TextView messageContent = viewHolder.message;

        CardView card = viewHolder.card;


        boolean useDayMode = preferences.getBoolean(PREF_NIGHT_MODE, false);
        final int version = Build.VERSION.SDK_INT;

        // This makes sure to use the correct version of the command based on android version number
        if(useDayMode && version < 23) {
            card.setBackgroundColor(getContext().getResources().getColor(R.color.freshSidewalk));
            name.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            time.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            messageContent.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        } else if(useDayMode && version >= 23) {
            card.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.freshSidewalk));
            name.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            time.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            messageContent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else if(!useDayMode && version < 23){
            card.setBackgroundColor(getContext().getResources().getColor(R.color.lightGraphite));
            name.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            time.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            messageContent.setTextColor(getContext().getResources().getColor(R.color.frostbite));
        } else {
            card.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGraphite));
            name.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            time.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            messageContent.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
        }





        // This will convert only the current chat users username to the color they selected.
        int selected = preferences.getInt("color", -1672077);
        if(sharedPref.getString("username", "") == (db.getUser(message.getUser_id()).getName())){
            name.setTextColor(selected);
        }

        // This sets it to a string of the user id.
        name.setText((db.getUser(message.getUser_id()).getName()));

        // Set the time sent of the message
        time.setText(message.getTimeSent());

        // Set the content of the message
        messageContent.setText(message.getContent());

        // Sets the avatar using the resource id of the drawable image stored in the message
        ImageView avatar = viewHolder.avatar;
        Picasso.with(getContext())
                .load(db.getUser(message.getUser_id()).getAvatar())
                .placeholder(db.getUser(message.getUser_id()).getAvatar())
                .into(avatar);
//        avatar.setImageResource(db.getUser(message.getUser_id()).getAvatar());
        // Using closeDB because it exists at this point in the program
        db.closeDB();
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
