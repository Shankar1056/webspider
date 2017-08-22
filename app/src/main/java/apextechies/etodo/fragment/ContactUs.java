package apextechies.etodo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import apextechies.etodo.R;

/**
 * Created by Admin on 08-05-2017.
 */

public class ContactUs extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contactus,container,false);
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.phone).setOnClickListener(this);
        view.findViewById(R.id.alternatenumber).setOnClickListener(this);
        view.findViewById(R.id.email).setOnClickListener(this);
        view.findViewById(R.id.websitelink).setOnClickListener(this);
        view.findViewById(R.id.location).setOnClickListener(this);
    
        mapinitialize(view);
        
    }
    
       

    

    private void mapinitialize(View view) {
        try

        {
            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(23.7867885,86.4185349);
        mMap.addMarker(new
                MarkerOptions().position(latLng).title("eWebbazar Prv. Ltd"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Web Spider");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.alternatenumber:
                try {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+"7549098888"));
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(getActivity(), "Phone facility not available on your device", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.phone:
                try {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+"7549097777"));
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(getActivity(), "Phone facility not available on your device", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.email:
                try {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","support@websspider.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
                catch (Exception e){
                    Toast.makeText(getActivity(), "Phone facility not available on your device", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.websitelink:
                try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.websspider.com/"));
                startActivity(i);
                }
                catch (Exception e){
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.location:
                break;
        }
    }
}