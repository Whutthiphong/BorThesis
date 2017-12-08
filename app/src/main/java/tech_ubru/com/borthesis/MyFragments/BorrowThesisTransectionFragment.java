package tech_ubru.com.borthesis.MyFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowThesisTransectionFragment extends Fragment {


    public BorrowThesisTransectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().show();
        return inflater.inflate(R.layout.fragment_borrow_thesis_transection, container, false);
    }

}
