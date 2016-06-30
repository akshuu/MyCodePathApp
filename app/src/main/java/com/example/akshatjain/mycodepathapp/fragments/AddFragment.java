package com.example.akshatjain.mycodepathapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.akshatjain.mycodepathapp.R;
import com.example.akshatjain.mycodepathapp.activities.TodoActivity;
import com.example.akshatjain.mycodepathapp.db.Todo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    private Context mContext;
    Spinner prioritySpinner;
    EditText txtName;
    EditText txtDesc;

    private AddListener mListener;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddFragment.
     */
    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View addView = inflater.inflate(R.layout.fragment_add, container, false);
        prioritySpinner = (Spinner) addView.findViewById(R.id.spinner);

        txtName = (EditText) addView.findViewById(R.id.editText);
        txtDesc = (EditText) addView.findViewById(R.id.editText2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        //Populate the spinner
        Button btnOk = (Button) addView.findViewById(R.id.btnSave);
        Button btnCancel = (Button) addView.findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtName.getText())){
                    Toast.makeText(mContext,"Please enter some text",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    int priority = prioritySpinner.getSelectedItemPosition();
                    Todo todo = new Todo(txtName.getText().toString(),txtDesc.getText().toString(),priority);
                    if(mListener != null)
                        mListener.onAdd(todo);
                }
                onButtonPressed();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
        return addView;
    }

    private void onButtonPressed() {
        FragmentManager fm = ((TodoActivity)mListener).getSupportFragmentManager();
        fm.popBackStack();
        if(mListener != null)
            mListener.onCancel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        ((TodoActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.createNew));
        if (context instanceof AddListener) {
            mListener = (AddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddListener");
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
    public interface AddListener {
        void onAdd(Todo newItem);
        void onCancel();
    }
}
