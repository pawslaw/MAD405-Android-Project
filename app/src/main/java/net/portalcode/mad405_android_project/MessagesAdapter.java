package net.portalcode.mad405_android_project;

/**
 * Created by James Pierce on 3/24/2017.
 */


// Utilizing a guide found on https://guides.codepath.com/android/using-the-recyclerview for RecyclerView
// Need a reycler view because of the content in the list

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare variables for the items to display in each row
        public TextView name;
        public TextView time;
        public TextView message;
        public ImageView avatar;

        // Constructor for the view
        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            message = (TextView) itemView.findViewById(R.id.message);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
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
        // Gets the item from the current position
        Message message = mMessages.get(position);
        DatabaseHandler db = new DatabaseHandler(getContext());

        // Set the Name for each message based on the userID of the current message
        TextView name = viewHolder.name;

        // This sets it to a string of the user id.
        name.setText((db.getUser(message.getUser_id()).getName()));

        // Set the time sent of the message
        TextView time = viewHolder.time;
        time.setText(message.getTimeSent());

        // Set the content of the message
        TextView messageContent = viewHolder.message;
        messageContent.setText(message.getContent());

        // Sets the avatar using the resource id of the drawable image stored in the message
        ImageView avatar = viewHolder.avatar;
        Picasso.with(getContext())
                .load(R.drawable.ic_attach_file_black_24dp)
                .placeholder(db.getUser(message.getUser_id()).getAvatar())
                .into(avatar);
//        avatar.setImageResource(db.getUser(message.getUser_id()).getAvatar());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
