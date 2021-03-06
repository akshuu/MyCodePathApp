package com.example.akshatjain.mycodepathapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * {@link EditItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private static final String ARG_DESC = "desc";
    private static final String ARG_ID = "id";
    private static final String ARG_PRIORITY = "priority";

    private String mName;
    private String mDesc;
    private int mPriority;
    private Long mId;
    private static int mPosition;
    Spinner prioritySpinner;
    private Context mContext;

    private OnFragmentInteractionListener mListener;

    public EditItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param todo Parameter 1.
     * @param position
     * @return A new instance of fragment EditItemFragment.
     */
    public static EditItemFragment newInstance(Todo todo, int position) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, todo.name);
        args.putString(ARG_DESC, todo.description);
        args.putInt(ARG_PRIORITY, todo.priority);
        args.putLong(ARG_ID, todo._id);
        mPosition = position;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
            mDesc = getArguments().getString(ARG_DESC);
            mPriority = getArguments().getInt(ARG_PRIORITY);
            mId = getArguments().getLong(ARG_ID);
        }
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);
        final EditText txtName = (EditText) view.findViewById(R.id.editText);
        txtName.setText(mName);
        final EditText txtDesc = (EditText) view.findViewById(R.id.editText2);
        txtDesc.setText(mDesc);
        prioritySpinner = (Spinner) view.findViewById(R.id.spinner);

        Button btnOk = (Button) view.findViewById(R.id.btnSave);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        prioritySpinner.setSelection(mPriority);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtName.getText())){
                    Toast.makeText(mContext,"Please enter some text",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    int priority = prioritySpinner.getSelectedItemPosition();
                    Todo todo = new Todo(mId,txtName.getText().toString(),txtDesc.getText().toString(),priority);
                    if(mListener != null)
                        mListener.onUpdate(todo,mPosition);
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
//        getDialog().setTitle(mName);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onCancel();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        ((TodoActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.editItem));
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onCancel();
        void onUpdate(Todo todo, int mPosition);
    }
}
