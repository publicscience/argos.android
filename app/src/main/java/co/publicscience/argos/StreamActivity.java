package co.publicscience.argos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.publicscience.argos.Models.Event;
import co.publicscience.argos.Responses.EventsResponse;
import co.publicscience.argos.Services.ArgosService;
import co.publicscience.argos.Util.SimpleSectionedRecyclerViewAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class StreamActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeLayout;
    RecyclerView eventListView;
    EventAdapter mAdapter;
    ArrayList mEventList = new ArrayList();
    ArgosService argosService = new ArgosService();
    List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
    SimpleSectionedRecyclerViewAdapter mSectionedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        // Setup the toolbar as the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Setup the pull-to-refresh layout.
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Setup the sectioned recycling list view.
        eventListView = (RecyclerView)findViewById(R.id.event_listview);
        eventListView.setLayoutManager(new LinearLayoutManager(this));
        eventListView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new EventAdapter(mEventList, R.layout.event_card, this);

        // Add the event adapter to the sectioned adapter and initialize it.
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(this, R.layout.event_section, R.id.section_text, mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        eventListView.setAdapter(mSectionedAdapter);

        final StreamActivity self = this;
        mAdapter.SetOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Event event) {
                Intent detailIntent = new Intent(self, EventDetailActivity.class);
                detailIntent.putExtra("event", event);
                startActivity(detailIntent);
            }
        });

        // The layout starts refreshing as it fetches the first set of data.
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
        requestData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stream, menu);
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

    private void requestData() {
        argosService.getAPI().getEvents(new Callback<EventsResponse>() {
            @Override
            public void success(EventsResponse eventsResponse, Response response) {
                mEventList.clear();
                sections.clear();

                mEventList.addAll(eventsResponse.getEvents());

                HashMap<Integer,String> sectionData = Event.splitEvents(eventsResponse.getEvents());
                for (Map.Entry<Integer, String> entry : sectionData.entrySet()) {
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(entry.getKey(), entry.getValue()));
                }

                SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
                mSectionedAdapter.setSections(sections.toArray(dummy));

                mAdapter.notifyDataSetChanged();
                mSwipeLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("StreamActivity", error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
