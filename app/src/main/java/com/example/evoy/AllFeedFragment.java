package com.example.evoy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AllFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFeedFragment newInstance(String param1, String param2) {
        AllFeedFragment fragment = new AllFeedFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_feed, container, false);
        RecyclerView feed = rootView.findViewById(R.id.myRecyclerAll);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String user = prefs.getString("user", "");

        JSONArray results = null;
        int[] idEvents = null;
        try {
            results = controladorBDWebService.getInstance().getAllFeed(this.getActivity(), user);
            idEvents = controladorBDWebService.getInstance().getFollowed(getActivity(),"getFollowed",user);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Bitmap[] imgs;
        String[] names;
        String[] locations;
        Boolean[] followed = null;
        
        if (!results.equals(null)) {
            imgs = new Bitmap[results.size()];
            names = new String[results.size()];
            locations = new String[results.size()];
            followed = new Boolean[results.size()];

            for (int i = 0; i < results.size(); i++) {
                JSONObject tmp = (JSONObject) results.get(i);
                names[i] = (String) tmp.get("name");
                locations[i] = (String) tmp.get("location");
                String img64 = (String) tmp.get("image");
                InputStream stream = new ByteArrayInputStream(Base64.decode(img64.getBytes(), Base64.DEFAULT));
                Bitmap img = BitmapFactory.decodeStream(stream);
                imgs[i] = img;
                int idEvent = Integer.valueOf((String) tmp.get("id"));
                if(idEvents.equals(null)){
                    followed[i]=false;
                }else {
                    if (esta(idEvents,idEvent)) {
                        followed[i] = true;
                    } else {
                        followed[i] = false;
                    }
                }
            }
        } else {
            imgs = new Bitmap[0];
            names = new String[0];
            locations = new String[0];
        }

        MyCardViewAdapter myAdapter = new MyCardViewAdapter(names, imgs, locations, followed);
        feed.setAdapter(myAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        feed.setLayoutManager(linearLayout);
        return rootView;
    }

    private boolean esta(int[] idEvents, int idEvent) {
        boolean esta = false;
        int i = 0;
        while(i<idEvents.length){
            if (idEvents[i]==idEvent){
                esta = true;
                break;
            }
            i++;
        }
        return esta;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
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
*/

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
