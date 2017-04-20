package net.portalcode.mad405_android_project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText emailForm = (EditText) view.findViewById(R.id.email);
        final EditText passwordForm = (EditText) view.findViewById(R.id.password);

        Button loginButton = (Button) view.findViewById(R.id.loginButton);

        // Temporarily set credentials
        emailForm.setText("");
        passwordForm.setText("");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

//                if (!mData.isConnected() && !mWifi.isConnected()) {
                if (!mWifi.isConnected()) {
                    Toast.makeText(getContext(), "Please connect to the internet.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (emailForm.getText().length() == 0) {
                    Toast.makeText(getContext(), "Please enter your email.", Toast.LENGTH_LONG).show();
                } else if (passwordForm.getText().length() == 0) {
                    Toast.makeText(getContext(), "Please enter your password.", Toast.LENGTH_LONG).show();
                }

                User user = new User();
                user.setEmail(emailForm.getText().toString());
                user.setPassword(passwordForm.getText().toString());

                JSONObject post_dict = new JSONObject();

                try {
                    post_dict.put("email" , user.getEmail());
                    post_dict.put("password", user.getPassword());
                    post_dict.put("action" , "login");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.i("LOG", String.valueOf(post_dict));

                if (post_dict.length() > 0) {

                    new APICall().execute(String.valueOf(post_dict));

                    //Log.i("LOG", );
                }

            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
