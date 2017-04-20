package net.portalcode.mad405_android_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.R.attr.description;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreditDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreditDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_NIGHT_MODE = "night_mode";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private int mParam5;
    private String mParam6;

    private OnFragmentInteractionListener mListener;

    public CreditDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreditDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditDisplayFragment newInstance(String param1, String param2, String param3, String param4, int param5, String param6) {
        CreditDisplayFragment fragment = new CreditDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putInt(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getInt(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_credit_display, container, false);

        // This will set all values to their appropriate places as long as there were values passed into the fragment
        if(mParam1 != null) {
            TextView featureName = (TextView) view.findViewById(R.id.featureName);
            featureName.setText(mParam1);
        }
        if(mParam2 != null) {
            TextView authorName = (TextView) view.findViewById(R.id.authorName);
            authorName.setText(mParam2);
        }
        if(mParam3 != null) {
            TextView licenseName = (TextView) view.findViewById(R.id.licenseName);
            licenseName.setText(mParam3);
        }
        if(mParam4 != null) {
            TextView description = (TextView) view.findViewById(R.id.usageDescription);
            description.setText(mParam4);
        }
        if(mParam5 != 0) {
            ImageView image = (ImageView) view.findViewById(R.id.creditImage);
            Picasso.with(getContext()).load(mParam5).placeholder(mParam5).centerCrop().resize(250, 250).into(image);
        }
        if(mParam6 != null) {
            ImageView image = (ImageView) view.findViewById(R.id.creditImage);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri webpage = Uri.parse(mParam6);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
        }
        CardView messageCardView = (CardView) view.findViewById(R.id.messageCardView);
        TextView featureTitle = (TextView) view.findViewById(R.id.featureTitle);
        TextView featureName = (TextView) view.findViewById(R.id.featureName);
        TextView authorTitle = (TextView) view.findViewById(R.id.authorTitle);
        TextView authorName = (TextView) view.findViewById(R.id.authorName);
        TextView licenseTitle = (TextView) view.findViewById(R.id.licenseTitle);
        TextView licenseName = (TextView) view.findViewById(R.id.licenseName);
        TextView usageDescription = (TextView) view.findViewById(R.id.usageDescription);
        TextView usageDescriptionTitle = (TextView) view.findViewById(R.id.usageDescriptionTitle);


        // This is the preferences file the user can make changes to
        SharedPreferences preferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean useDayMode = preferences.getBoolean(PREF_NIGHT_MODE, false);
        final int version = Build.VERSION.SDK_INT;
        if(useDayMode && version < 23) {
            messageCardView.setBackgroundColor(getContext().getResources().getColor(R.color.freshSidewalk));
            featureTitle.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            featureName.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            authorName.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            authorTitle.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            licenseName.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            licenseTitle.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            usageDescription.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            usageDescriptionTitle.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        } else if(useDayMode && version >= 23) {
            messageCardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.freshSidewalk));
            featureTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            featureName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            authorName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            authorTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            licenseName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            licenseTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            usageDescription.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            usageDescriptionTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else if(!useDayMode && version < 23){
            messageCardView.setBackgroundColor(getContext().getResources().getColor(R.color.lightGraphite));
            featureTitle.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            featureName.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            authorName.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            authorTitle.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            licenseName.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            licenseTitle.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            usageDescription.setTextColor(getContext().getResources().getColor(R.color.frostbite));
            usageDescriptionTitle.setTextColor(getContext().getResources().getColor(R.color.frostbite));
        } else {
            messageCardView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGraphite));
            featureTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            featureName.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            authorName.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            authorTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            licenseName.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            licenseTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            usageDescription.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
            usageDescriptionTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.frostbite));
        }

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
