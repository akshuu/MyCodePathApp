package com.example.akshatjain.mycodepathapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.akshatjain.mycodepathapp.activities.TodoActivity;
import com.example.akshatjain.mycodepathapp.db.TodoSql;
import com.example.akshatjain.mycodepathapp.fragments.AddFragment;

/**
 * Created by akshatjain on 7/4/16.
 */

public class AddItemTest extends ActivityInstrumentationTestCase2<TodoActivity> {

    private TodoActivity mActivity;

    public AddItemTest() {
        super(TodoActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    public void testAddItem(){
        assertNotNull(mActivity);
        final RecyclerView listView = (RecyclerView) mActivity.findViewById(R.id.listView);
        final FloatingActionButton fab = (FloatingActionButton) mActivity.findViewById(R.id.fab);
        assertNotNull("Cannot find the Floating button",fab);

        int oldCnt = listView.getAdapter().getItemCount();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fab.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AddFragment fragment = (AddFragment) mActivity.getSupportFragmentManager().findFragmentByTag("addFragment");
                Spinner prioritySpinner = (Spinner) fragment.getView().findViewById(R.id.spinner);

                EditText txtName = (EditText) fragment.getView().findViewById(R.id.editText);
                EditText txtDesc = (EditText) fragment.getView().findViewById(R.id.editText2);
                Button btnOk = (Button) fragment.getView().findViewById(R.id.btnSave);

                txtName.setText("Test Item");
                txtDesc.setText("Test Description");
                prioritySpinner.setSelection(1);

                btnOk.performClick();

            }
        });

        getInstrumentation().waitForIdleSync();

        int newCnt = listView.getAdapter().getItemCount();

        assertEquals("New item has not been added",newCnt, oldCnt+1);
    }

    public void testAddDuplicateItem(){
        assertNotNull(mActivity);
        final RecyclerView listView = (RecyclerView) mActivity.findViewById(R.id.listView);
        final FloatingActionButton fab = (FloatingActionButton) mActivity.findViewById(R.id.fab);
        assertNotNull("Cannot find the Floating button",fab);

        int oldCnt = listView.getAdapter().getItemCount();

        clickAndAddItem(fab);

        getInstrumentation().waitForIdleSync();

        int newCnt = listView.getAdapter().getItemCount();

        assertEquals("New item has not been added",newCnt, oldCnt+1);

        clickAndAddItem(fab);

        int newCnt2 = listView.getAdapter().getItemCount();

        assertEquals("New item has not been added",newCnt2, newCnt);

    }

    private void clickAndAddItem(final FloatingActionButton fab) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fab.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AddFragment fragment = (AddFragment) mActivity.getSupportFragmentManager().findFragmentByTag("addFragment");
                Spinner prioritySpinner = (Spinner) fragment.getView().findViewById(R.id.spinner);

                EditText txtName = (EditText) fragment.getView().findViewById(R.id.editText);
                EditText txtDesc = (EditText) fragment.getView().findViewById(R.id.editText2);
                Button btnOk = (Button) fragment.getView().findViewById(R.id.btnSave);

                txtName.setText("Test Item");
                txtDesc.setText("Test Description");
                prioritySpinner.setSelection(1);

                btnOk.performClick();

            }
        });
    }

    @Override
    protected void tearDown() throws Exception {
        TodoSql sql = mActivity.getSql();
        SQLiteDatabase writableDatabase = sql.getWritableDatabase();
        sql.delete(writableDatabase,"Test Item");
        super.tearDown();
    }
}
