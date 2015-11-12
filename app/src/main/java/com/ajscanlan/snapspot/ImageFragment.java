package com.ajscanlan.snapspot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ajscanlan.snapspot.model.Image;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {

    private String id;
    private static ImageView imageView;
    private Image mImage;


    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static ImageFragment newInstance(Image image) {
        ImageFragment fragment = new ImageFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        //fragment.id = id;
        fragment.mImage = image;

        if(imageView == null) Log.d("HASHMAP", "IN FRAGMENT, IMAGEVIEW NULL");

        return fragment;
    }

    public ImageFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = (ImageView) v.findViewById(R.id.imageView);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d("LISTENER", "in onDetach");

        super.onDetach();
        mListener.onFragmentInteraction();
        mListener = null;
        //imageView.setImageBitmap(null);
        imageView = null;
        mImage = null;
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
        public void onFragmentInteraction();
    }

    @Override
    public void onStart(){
        super.onStart();

        if(imageView == null) Log.d("HASHMAP", "IN FRAGMENT, IMAGEVIEW NULL");

        Log.d("HASHMAP", mImage == null ? "null" : "not null");
        imageView.setImageBitmap(mImage.getBitmap());
    }
}
