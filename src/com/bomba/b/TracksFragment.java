package com.bomba.b;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class TracksFragment extends Fragment implements OnClickListener {
	Button bt;

	String noddy = "this us su naknldaldq";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.trackslistfragment, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		bt = (Button) getActivity().findViewById(R.id.btFragA);
		bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btFragA:

			Toast.makeText(getActivity().getApplicationContext(), "my name is kermie", Toast.LENGTH_LONG).show();

			break;
		}

	}

}
