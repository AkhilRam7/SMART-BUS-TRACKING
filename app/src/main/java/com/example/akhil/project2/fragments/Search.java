package com.example.akhil.project2.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.akhil.project2.FullscreenActivity;
import com.example.akhil.project2.MapsActivity;
import com.example.akhil.project2.R;
import com.example.akhil.project2.busadapter;
import com.example.akhil.project2.config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Search.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button bsearch,dest,sourcebtn,book;
    EditText count;
    AutoCompleteTextView source,destination;
    String myJSON;
    //JSONArray results = null;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private OnFragmentInteractionListener mListener;
    private ListView lis;
    busadapter adapter;

    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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


        /*bsearch = (Button) getActivity().findViewById(R.id.search);
        textView = (TextView) getActivity().findViewById(R.id.textview);
        source = (AutoCompleteTextView) getActivity().findViewById(R.id.source);
        destination = (AutoCompleteTextView) getActivity().findViewById(R.id.destination);
        String [] busstops={"kandigai","tambaram","kelambakkam","vandalur","mambakkam"};

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,busstops);
        source.setAdapter(adapter);
        destination.setAdapter(adapter);*/




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference("id_2");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //showData(dataSnapshot)
                Log.d("check","data changed");
                //String value = dataSnapshot.getValue(String.class);

                Log.v("Value","Data: "+ dataSnapshot.child("lat").getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("check","cancelled");
                Log.w("check", databaseError.toException());

            }
        });

        lis = (ListView)rootView.findViewById(R.id.listview);
        bsearch = (Button) rootView.findViewById(R.id.search);
        //textView = (TextView) rootView.findViewById(R.id.textview);
        source = (AutoCompleteTextView) rootView.findViewById(R.id.source);
        destination = (AutoCompleteTextView) rootView.findViewById(R.id.destination);
        dest = (Button)rootView.findViewById(R.id.dest);
        sourcebtn = (Button)rootView.findViewById(R.id.sourcebtn);
        book= (Button)rootView.findViewById(R.id.Book);
        count=(EditText) rootView.findViewById(R.id.num);
        getData();



        //final String [] busstops={"kandigai","tambaram","kelambakkam","vandalur","mambakkam"};

        /*ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,busstops);
        source.setAdapter(adapter);
        destination.setAdapter(adapter);
        source.setThreshold(1);
        destination.setThreshold(1);*/
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = source.getText().toString();
                String s2 = destination.getText().toString();
                String s3=count.getText().toString();

                final Random random = new Random();
                int rand = random.nextInt(999999)+1;
                String s4 = Integer.toString(rand);

                Log.d("checkcode",s4);

                if (s1.length() == 0) {
                    Toast.makeText(getActivity(), "Enter source to continue..", Toast.LENGTH_LONG).show();
                } else if (s2.length() == 0) {
                    Toast.makeText(getActivity(), "Enter destination to continue..", Toast.LENGTH_LONG).show();
                } else if(s1.equals(s2)) {
                    Toast.makeText(getActivity(), "Source and destination can't be same", Toast.LENGTH_LONG).show();
                } else{
                    booktickets(s1,s2,s3,s4);

                    //Intent i = new Intent(getActivity(), MapsActivity.class);
                    //startActivity(i);
                }
            }
        });

        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = source.getText().toString();
                String s2 = destination.getText().toString();
                if (s1.length() == 0) {
                    Toast.makeText(getActivity(), "Enter source to continue..", Toast.LENGTH_LONG).show();
                } else if (s2.length() == 0) {
                    Toast.makeText(getActivity(), "Enter destination to continue..", Toast.LENGTH_LONG).show();
                } else if(s1.equals(s2)) {
                    Toast.makeText(getActivity(), "Source and destination can't be same", Toast.LENGTH_LONG).show();
                } else{
                    getbus(s1,s2);
                    //Intent i = new Intent(getActivity(), MapsActivity.class);
                    //startActivity(i);
                }
            }
        });

        dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination.setText("");
            }
        });

        sourcebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                source.setText("");
            }
        });

        // Inflate the layout for this fragment
        return rootView;

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


    private void getData() {
        /*String id = editTextId.getText().toString().trim();
        if (id.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }*/
       // loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        Log.d("check9","getdata");
        String url = config.DATA_URL + "busstops.php";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("check9","onRespo");
                showJSON(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response){
        String [] stopname,stopid;
        //String stopname="";
        //String address="";
        //String vc = "";
        //Log.d("check9","showJS");
        try {
            //Log.d("check9","try");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = jsonObject.getJSONArray(config.JSON_ARRAY);

            stopname = new String[results.length()];
            stopid = new String[results.length()];

            for(int i=0; i<results.length(); i++){
                JSONObject data = results.getJSONObject(i);
                stopname[i]= data.getString(config.KEY_NAME);
                stopid[i]=data.getString(config.KEY_ID);
                //Log.d("check9",stopid[i]);
                //Log.d("check2","hello");
                //Log.d("check2",stopname[i]);
                //stopname = collegeData.getString(config.KEY_NAME);
            }

                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, stopname);

                source.setAdapter(adapter);
                destination.setAdapter(adapter);
                source.setThreshold(1);
                destination.setThreshold(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
    }


    private void getbus(final String src, final String dest) {
        /*String id = editTextId.getText().toString().trim();
        if (id.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }*/
        // loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = config.DATA_URL + "busno.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("check9","onRespo");

                showJSON2(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                //Log.d("First", "Start");
                //Log.d("First","");
               // Log.d("First", d.toString());

                parameters.put("source", src);
                parameters.put("destination", dest);


                //Log.d("First", "Stop");


                return parameters;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        requestQueue2.add(stringRequest);
    }

    public void showJSON2(String response){
        String [] busno;
        //String stopname="";
        //String address="";
        //String vc = "";
        //Log.d("check9","showJS");
        Log.d("check10","shJ2");
        try {
            Log.d("check10","try");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = jsonObject.getJSONArray(config.JSON_ARRAY);

            busno = new String[results.length()];
            //stopid = new String[results.length()];
            Log.d("check10","try3");
            for(int i=0; i<results.length(); i++){
                JSONObject data = results.getJSONObject(i);
                busno[i]= data.getString(config.KEY_NAME2);
                //stopid[i]=data.getString(config.KEY_ID);
                Log.d("check10",busno[i]);
                //Log.d("check2","hello");
                //Log.d("check2",stopname[i]);
                //stopname = collegeData.getString(config.KEY_NAME);
            }


            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, busno);
            adapter = new busadapter(getActivity(),busno);
            lis.setAdapter(adapter);

            lis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String k = Integer.toString(position);
                    Log.d("check",k);
                    map(position);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
    }

    public void map(int pos){
        Intent i = new Intent(getActivity(),MapsActivity.class);
        i.putExtra("busid",pos);
        startActivity(i);
    }

    private void booktickets(final String src, final String dest,final String tcount,final String tickcode) {
        /*String id = editTextId.getText().toString().trim();
        if (id.equals("")) {
            Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }*/
        // loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = config.DATA_URL + "ticketgen.php";



        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                Log.d("check9","onRespo");

                Toast.makeText(getActivity(), "ticket booked ", Toast.LENGTH_LONG).show();
                //showJSON3(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"ticket is not booked ",Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                //Log.d("First", "Start");
                //Log.d("First","");
                // Log.d("First", d.toString());


                parameters.put("source", src);
                parameters.put("destination", dest);
                parameters.put("tcount",tcount);
                parameters.put("tid",tickcode);


                //Log.d("First", "Stop");


                return parameters;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        requestQueue2.add(stringRequest);

        Intent tick = new Intent(getActivity(), FullscreenActivity.class);
        tick.putExtra("tick",tickcode);
        startActivity(tick);
    }




}