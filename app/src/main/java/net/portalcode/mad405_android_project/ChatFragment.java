package net.portalcode.mad405_android_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Declare variables for views being displayed
    static RecyclerView rvMessages;
    Button sendButton;


    // Declare variables for the message content to be transferred
    EditText messageContent;
    String newMessage;
    String currentDateTimeString;

    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static ArrayList<Message> messageList;
    public static MessagesAdapter adapter = null;

    public static MediaPlayer mp = null;
    public static Vibrator vibe = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // License: Sampling Plus 1.0
        // License Link : https://creativecommons.org/licenses/sampling+/1.0/
        // Download link : http://soundbible.com/tags-swoosh.html
        mp = MediaPlayer.create(this.getContext(), R.raw.sendbeep);
        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);



        messageContent = (EditText) view.findViewById(R.id.editMessage);
        sendButton = (Button) view.findViewById(R.id.sendButton);
        rvMessages = (RecyclerView) view.findViewById(R.id.chatList);

        run.run();

        return view;
    }

    public static ConnectivityManager connectivityManager;

    public Runnable run = new Runnable() {
        @Override
        public void run() {
            // Attempt to move content up when opening the EditText
            // This does not work all the time. No idea why.
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            DatabaseHandler db = new DatabaseHandler(getContext());
            messageList = db.getAllMessages();

            db.closeDB();

            adapter = new MessagesAdapter(getActivity().getBaseContext(), messageList);

            // Attach the adapter to the recyclerview to populate items
            rvMessages.setAdapter(adapter);

            // Set layout manager to position the items
            rvMessages.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

            // This will be used to send a message once the user is ready to send it.
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newMessage =  messageContent.getText().toString();

                    // This will get the current time.
                    currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    // This will add the message to the messageList if there are users in the database
                    DatabaseHandler db = new DatabaseHandler(getContext());
                    //ArrayList<User> test= new ArrayList<User>();
                    System.out.println(db.getAllUsers());
                    //System.out.println(test);

                    // This will test to see if the user is connected to wifi.
                    ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if (mWifi.isConnected()) {
                        JSONObject post_dict = new JSONObject();

                        try {
                            post_dict.put("action", "getallusers");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Log.i("LOG", String.valueOf(post_dict));

                        if (post_dict.length() > 0) {

                            new APICall().execute(String.valueOf(post_dict));

                            //Log.i("LOG", );
                        }
                    }


                    // Confirm there are valid users in the local database
                    // NOTE This does not confirm the CURRENT user is valid simply that there are valid users.
                    //TODO This should probably be updated to confirm the user is a validated user before sending the message
                    //if(!db.getAllUsers().equals(test)){
                        // The following is commented out as the school tablets do not support Data, only WiFi.
                        // Should look into a proper fix for this, as this does not allow users with data access to send messages.
                        //if (mWifi.isConnected() || mData.isConnected()) {
                        if (mWifi.isConnected()) {
                            if(!newMessage.trim().equals("")){

                                JSONObject post_dict = new JSONObject();

                                try {
                                    post_dict.put("action" , "getallusers");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Log.i("LOG", String.valueOf(post_dict));

                                if (post_dict.length() > 0) {

                                    new APICall().execute(String.valueOf(post_dict));

                                    //Log.i("LOG", );
                                }

                                Log.i("LOG", String.valueOf(adapter.getItemCount()));

                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//                                    newMessage = sharedPref.getString("username", "");
//                                    newMessage += "\n";
//                                    newMessage += sharedPref.getString("password", "");

                                post_dict = new JSONObject();

                                try {
                                    post_dict.put("email" , sharedPref.getString("username", ""));
                                    post_dict.put("password", sharedPref.getString("password", ""));
                                    post_dict.put("action" , "sendmessage");
                                    post_dict.put("message" , newMessage);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Log.i("LOG", String.valueOf(post_dict));

                                if (post_dict.length() > 0) {

                                    new APICall().execute(String.valueOf(post_dict));

                                    //Log.i("LOG", );
                                }


                                String latestMessage = "0000-00-00 00:00:00.000";
                                Message message = db.getLatestMessage();

                                if (message == null) {
                                    message = new Message();
                                    message.setTimeSent(latestMessage);
                                }

                                Log.i("LOG", message.toString());

                                try {
                                    post_dict.put("action" , "getnewmessages");
                                    post_dict.put("timestamp" , message.getTimeSent());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Log.i("LOG", String.valueOf(post_dict));

                                if (post_dict.length() > 0) {

                                    new APICall().execute(String.valueOf(post_dict));

                                    //Log.i("LOG", );
                                }

                                db.closeDB();
                                db = new DatabaseHandler(getContext());
                                //System.out.println("I am adding a message to the chat");
                                //db.addMessage(new Message(currentDateTimeString, newMessage, 2));
                                //messageList.add(new Message(currentDateTimeString, newMessage, 2));
                                messageList = db.getAllMessages();
                                //Log.i("LOGSCREAM", "THE MESSAGE WAS " + String.valueOf(messageList.get(messageList.size()-1).getContent()));

                                // This will update the adapter so that the new message will be displayed on the screen
                                // This will update the view adapter
                                //adapter.
                                adapter.notifyDataSetChanged();

                                // This will clear the editText
                                messageContent.setText("");


                                rvMessages.scrollToPosition(adapter.getItemCount()-1);


                                // Make the app vibrate
                                vibe.vibrate(100);
                                // Play a sent sound
                                mp.start();
                            } else {
                                Toast.makeText(getContext(), "Please do not send empty messages.", Toast.LENGTH_LONG).show();
                                vibe.vibrate(300);
                            }

                        } else {
                            Toast.makeText(getContext(), "You are not connected to a network.", Toast.LENGTH_LONG).show();
                            vibe.vibrate(300);
                        }
//                    } else {
//                        Toast.makeText(getContext(), "You are not a valid user. Please speak with IT.", Toast.LENGTH_LONG).show();
//                        vibe.vibrate(300);
//                    }
                    db.closeDB();
                }
            });

            rvMessages.scrollToPosition(adapter.getItemCount() -1);

            Timer timer = new Timer();
            timer.schedule(new GetNewMessages(), 2500, 2500);
        }
    };


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

class GetNewMessages extends TimerTask {
    public void run() {
        NetworkInfo mWifi = ChatFragment.connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            JSONObject post_dict = new JSONObject();
            DatabaseHandler db = new DatabaseHandler(MainActivity.context);
            Message message = db.getLatestMessage();
            db.closeDB();
            if (message == null) {
                message = new Message();
                String latestMessage = "0000-00-00 00:00:00.000";

                message.setTimeSent(latestMessage);
            }

            try {
                post_dict.put("action", "getnewmessages");
                post_dict.put("timestamp", message.getTimeSent());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("LOG", String.valueOf(post_dict));

            if (post_dict.length() > 0) {

                new APICall().execute(String.valueOf(post_dict));

                //Log.i("LOG", );
            }
        }
    }
}