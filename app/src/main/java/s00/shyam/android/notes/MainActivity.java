package s00.shyam.android.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import s00.shyam.android.notes.model.Note;
import s00.shyam.android.notes.stub.NoteData;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        LoadDummyItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void LoadDummyItems() {
        NoteListRecyclerAdapter adapter = new NoteListRecyclerAdapter(this, NoteData.getInstance().GetNotes());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //RecyclerViewScrollListener listener = new RecyclerViewScrollListener();
        mRecyclerView = (RecyclerView) findViewById(R.id.note_list);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void OnLoadMore(String text) {
                Snackbar.make(mRecyclerView, text, Snackbar.LENGTH_LONG).show();
            }
        });

        ItemTouchHelper.SimpleCallback noteSwipe = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = ((NoteListRecyclerAdapter.ViewHolder)viewHolder).getNotePosition();

                Note deleted = NoteData.getInstance().RemoveNote(pos);
                Snackbar.make(mRecyclerView, String.format("Deleted Note: %s", deleted.getTitle()), Snackbar.LENGTH_LONG).show();

                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper recyclerViewTouch = new ItemTouchHelper(noteSwipe);
        recyclerViewTouch.attachToRecyclerView(mRecyclerView);
    }
}

