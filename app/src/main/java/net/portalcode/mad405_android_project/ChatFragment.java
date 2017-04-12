package net.portalcode.mad405_android_project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


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
    RecyclerView rvMessages;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();

        // Attempt to move content up when opening the EditText
        // This does not work all the time. No idea why.
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        messageContent = (EditText) view.findViewById(R.id.editMessage);
        sendButton = (Button) view.findViewById(R.id.sendButton);
        rvMessages = (RecyclerView) view.findViewById(R.id.chatList);

        DatabaseHandler db = new DatabaseHandler(getContext());
        final ArrayList<Message> messageList = db.getAllMessages();

        db.closeDB();

        final MessagesAdapter adapter = new MessagesAdapter(this.getContext(), messageList);

        // Attach the adapter to the recyclerview to populate items
        rvMessages.setAdapter(adapter);

        // Set layout manager to position the items
        rvMessages.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // This will be used to send a message once the user is ready to send it.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMessage =  messageContent.getText().toString();

                // This will get the current time.
                currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                // This will add the message to the messageList if there are users in the database
                DatabaseHandler db = new DatabaseHandler(getContext());
                ArrayList<User> test= new ArrayList<User>();
                System.out.println(db.getAllUsers());
                System.out.println(test);

                // This will test to see if the user is connected to wifi.
                ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                
                // Confirm there are valid users in the local database
                // NOTE This does not confirm the CURRENT user is valid simply that there are valid users.
                //TODO This should probably be updated to confirm the user is a validated user before sending the message
                if(!db.getAllUsers().equals(test)){
                    if (mWifi.isConnected() || mData.isConnected()) {
                        System.out.println("I am adding a message to the chat");
                        db.addMessage(new Message(currentDateTimeString, newMessage, 2));
                        messageList.add(new Message(currentDateTimeString, newMessage, 2));

                        // This will update the adapter so that the new message will be displayed on the screen
                        // This will update the view adapter
                        adapter.notifyDataSetChanged();

                        // This will clear the editText
                        messageContent.setText("");

                        rvMessages.scrollToPosition(adapter.getItemCount()-1);
                    } else {
                        Toast.makeText(getContext(), "You are not connected to a network.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "You are not a valid user. Please speak with IT.", Toast.LENGTH_LONG).show();
                }

            }
        });

        rvMessages.scrollToPosition(adapter.getItemCount() -1);

        return view;
    }


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
