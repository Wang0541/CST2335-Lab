package com.example.yu.cst2335_lab;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.example.yu.cst2335_lab.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static android.os.Build.VERSION_CODES.M;

/**
 * An activity representing a list of Messages. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MessageDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MessageListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static final String ACTIVITY_NAME = "Query";
    public static final String SQL_MESSAGE="SQL MESSAGE:";
    public static final String COLUMN_COUNT = "Cursor\'s  column count= ";

    SQLiteDatabase db;
    Cursor cursor;

    ListView listView;
    EditText editText;
    Button sendButton;
    final ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);


        final ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);
        //open database
        db = chatDatabaseHelper.getWritableDatabase();
        String[] allColumns = { ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE };
        cursor = db.query(chatDatabaseHelper.TABLE_NAME,allColumns, null, null, null, null, null);

        Log.i(ACTIVITY_NAME,  COLUMN_COUNT+ cursor.getColumnCount() );

        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            Log.i(ACTIVITY_NAME, SQL_MESSAGE + cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE) ) );
            //shows the message in the screen when you onCreate this program
            list.add(cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }


        listView = (ListView)findViewById(R.id.list_item);
        editText = (EditText)findViewById(R.id.edit_message);
        sendButton = (Button)findViewById(R.id.send_button);

        //inner class
        //ArrayAdapter:A concrete BaseAdapter that is backed by an array of arbitrary objects. By default this class expects that the provided resource id references a single TextView
        class ChatAdapter extends ArrayAdapter<String> {
            // ctx represents the current context, 0 is resource ID
            public ChatAdapter(Context ctx) {
                super(ctx, 0);
            }
            public int getCount(){
                return list.size();
            }
            public String getItem(int position){
                return list.get(position);
            }

            public View getView(int position, View convertView, ViewGroup parent){
                LayoutInflater inflater = MessageListActivity.this.getLayoutInflater();
                View result = null ;
                if(position%2 == 0)
                    // you can make different layout under res/layout
                    result = inflater.inflate(R.layout.chat_row_incoming, null);
                else
                    result = inflater.inflate(R.layout.chat_row_outgoing, null);

                TextView message = (TextView)result.findViewById(R.id.message_text);
                message.setText(   getItem(position)  ); // get the string at position

                final String messageText = getItem(position) ;
                message.setText(messageText);

                result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putString(MessageDetailFragment.ARG_ITEM_ID,messageText);
                            MessageDetailFragment fragment = new MessageDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.message_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, MessageDetailActivity.class);
                            intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, messageText);

                            context.startActivity(intent);
                        }
                    }
                });

                return result;
            }
        }

        //in this case, “this” is the ChatWindow, which is-A Context object ChatAdapter
        final ChatAdapter messageAdapter =new ChatAdapter( this );

        //sets data behind this listView???
        listView.setAdapter (messageAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.add(editText.getText().toString());
                ContentValues cValues = new ContentValues();
                cValues.put(chatDatabaseHelper.KEY_MESSAGE,editText.getText().toString());
                db.insert(chatDatabaseHelper.TABLE_NAME, "null",cValues);
                //this restarts the process of getCount()/ getView()
                messageAdapter.notifyDataSetChanged();
                editText.setText("");

            }
        });


        if (findViewById(R.id.message_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MessageDetailFragment fragment = new MessageDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.message_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MessageDetailActivity.class);
                        intent.putExtra(MessageDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
