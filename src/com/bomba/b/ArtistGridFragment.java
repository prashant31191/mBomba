package com.bomba.b;



import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ArtistGridFragment extends Fragment implements OnClickListener {
	Button b;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.artistgrid, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		b =(Button)getActivity().findViewById(R.id.btFragB);
		b.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btFragB:
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Fragment B");
			builder.setMessage("What would you like to do?");
			builder.setPositiveButton("Nothing", null);
			builder.setNegativeButton("Leave me alone!", null);
			builder.show();
			break;
		}
	}

}
