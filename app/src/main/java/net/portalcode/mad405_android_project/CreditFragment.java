package net.portalcode.mad405_android_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected ViewPager viewPager;
    private CreditsPagerAdapter creditsPagerAdapter;

    private OnFragmentInteractionListener mListener;

    public CreditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditFragment newInstance(String param1, String param2) {
        CreditFragment fragment = new CreditFragment();
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
        View view = inflater.inflate(R.layout.fragment_credit, container, false);

        // This will set the PagerAdapter to the adapter built for the Member information
        creditsPagerAdapter = new CreditsPagerAdapter((getChildFragmentManager()));
        viewPager = (ViewPager) view.findViewById(R.id.creditcontent);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setAdapter(creditsPagerAdapter);
        return view;
    }

    /**
     * This is the CreditsPagerAdapter for use in displaying content in the ViewPager
     */
    public class CreditsPagerAdapter extends FragmentPagerAdapter {
        public CreditsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                // Feature name, Author Name, License Name, Feature Description, Image Resource, Download Link
                case 0:
                    return CreditDisplayFragment.newInstance("Send Sound", "http://soundbible.com/", "Sampling Plus 1.0", "This is the sound effect used when sending a message. It is used to notify the user that their message has been sent.", R.drawable.sound, "http://soundbible.com/706-Swoosh-3.html");
                case 1:
                    return CreditDisplayFragment.newInstance("ColorPicker", "Tianyu (xdtianyu)", "Apache 2.0", "This is the color picker used in the settings to pick the user's color in the chat part of the app.", R.drawable.colorpicker, "https://android-arsenal.com/details/1/3324");
                case 2:
                    return CreditDisplayFragment.newInstance("WCViewPagerIndicators", "Darwin Morocho", "MIT License", "This is the indicator at the bottom of this page to show how many pages are available to swipe through using the ViewPager. This was an aesthetic choice over including a set of left and right buttons.", R.drawable.circle, "https://android-arsenal.com/details/1/5494");
                case 3:
                    return CreditDisplayFragment.newInstance("Icicle App", "Brandon Brown", "N/A", "Co-Author of the Icicle app. Worked on front and back-end portions of the app. Worked on features required by Circuit Logistics to be implemented in the app.", R.drawable.icicle, "www.github.com");
                case 4:
                    return CreditDisplayFragment.newInstance("Icicle App", "James Pierce", "N/A", "Co-Author of the Icicle app. Worked on front and back-end portions of the app. Worked on features required by Circuit Logistics to be implemented in the app.", R.drawable.icicle, "www.github.com");
                case 5:
                    return CreditDisplayFragment.newInstance("Sound Image", "geralt", "CC0 Public Domain", "Used for App Credits", R.drawable.sound, "https://pixabay.com/en/according-to-sound-speakers-volume-214445/");
                case 6:
                    return CreditDisplayFragment.newInstance("Color Picker Image", "geralt", "CC0 Public Domain", "Used for App Credits", R.drawable.colorpicker, "https://pixabay.com/en/color-district-colorful-pattern-455365/");
                case 7:
                    return CreditDisplayFragment.newInstance("Circle Indicator Image", "OpenClipart-Vectors", "CC0 Public Domain", "Used for App Credits", R.drawable.circle, "https://pixabay.com/en/colors-chromatic-circle-red-green-157474/");

                default:
                    return CreditDisplayFragment.newInstance(null, null, null, null, 0, null);
            }
        }

        @Override
        public int getCount() {
            return 8;
        }
    }

    /**
     * This is the DepthPageTransformer, as provided by Google for use in ViewPagers.
     */
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
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
