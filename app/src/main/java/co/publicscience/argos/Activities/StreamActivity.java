package co.publicscience.argos.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import java.util.Timer;
import java.util.TimerTask;

import co.publicscience.argos.Adapters.EventAdapter;
import co.publicscience.argos.Adapters.SectionedRecyclerViewAdapter;
import co.publicscience.argos.Models.Event;
import co.publicscience.argos.Models.SearchResult;
import co.publicscience.argos.R;
import co.publicscience.argos.Responses.PaginatedResponse;
import co.publicscience.argos.Adapters.SearchResultAdapter;
import co.publicscience.argos.Services.ArgosService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class StreamActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    SwipeRefreshLayout mSwipeLayout;
    RecyclerView eventListView;
    EventAdapter mAdapter;
    ArrayList mEventList = new ArrayList();
    ArgosService argosService = new ArgosService();
    List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<SectionedRecyclerViewAdapter.Section>();
    SectionedRecyclerViewAdapter mSectionedAdapter;

    private int currentPage = 1;
    private boolean reachedEnd = false;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        eventListView = (RecyclerView)findViewById(R.id.event_listview);
        eventListView.setLayoutManager(layoutManager);
        eventListView.setItemAnimator(new DefaultItemAnimator());

        // Infinite scroll
        eventListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = eventListView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }

                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {

                    if (!reachedEnd) {
                        currentPage++;
                        loading = true;
                        requestData(currentPage);
                    }
                }
            }
        });

        mAdapter = new EventAdapter(mEventList, R.layout.event_card, this);

        // Add the event adapter to the sectioned adapter and initialize it.
        SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new SectionedRecyclerViewAdapter(this, R.layout.event_section, R.id.section_text, mAdapter);
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
        requestData(1);
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

    private void requestData(final int page) {
        argosService.getAPI().getEvents(page, new Callback<PaginatedResponse<Event>>() {
            @Override
            public void success(PaginatedResponse<Event> pResponse, Response response) {
                // Reset if we are refreshing, which will be when the requested page is lte the current page.
                if (currentPage <= page) {
                    mEventList.clear();
                    sections.clear();
                }

                mEventList.addAll(pResponse.getResults());

                HashMap<Integer,String> sectionData = Event.splitClustersByDay(pResponse.getResults());
                for (Map.Entry<Integer, String> entry : sectionData.entrySet()) {
                    sections.add(new SectionedRecyclerViewAdapter.Section(entry.getKey(), entry.getValue()));
                }

                SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[sections.size()];
                mSectionedAdapter.setSections(sections.toArray(dummy));

                mAdapter.notifyDataSetChanged();
                mSwipeLayout.setRefreshing(false);

                PaginatedResponse.Pagination p = pResponse.getPagination();
                if (p.page * p.per_page >= p.total_count) {
                    reachedEnd = true;
                }
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
        requestData(1);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }



    private SearchResultAdapter mSearchResultAdapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stream, menu);

        SearchView searchView = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        mSearchResultAdapter = new SearchResultAdapter(
                                                        this,
                                                        R.layout.search_result,
                                                        null,
                                                        new String[] {"_id", "name", "type", "id"},
                                                        null,
                                                        0);
        searchView.setSuggestionsAdapter(mSearchResultAdapter);

        return true;
    }

    private Timer searchTimer = new Timer();
    @Override
    public boolean onQueryTextSubmit(final String s) {
        if (s.length() > 3) {
            searchTimer.cancel();
            searchTimer = new Timer();
            searchTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    search(s);
                }
            }, 1000);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return onQueryTextSubmit(s);
    }

    private List<SearchResult> searchResults = new ArrayList<SearchResult>();
    private List<SearchResult> search(String query) {
        argosService.getAPI().getSearch(query, new Callback<PaginatedResponse<SearchResult>>() {
            @Override
            public void success(PaginatedResponse<SearchResult> pResponse, Response response) {
                searchResults.clear();
                searchResults.addAll(pResponse.getResults());

                String[] columnNames = {"_id", "name", "type", "id"};
                String[] temp = new String[4];
                int _id = 0;

                MatrixCursor cursor = new MatrixCursor(columnNames);
                for (SearchResult result : pResponse.getResults()) {
                    temp[0] = Integer.toString(_id++);

                    String name = result.getName();
                    if (name == null) {
                        name = result.getTitle();
                    }
                    temp[1] = name;

                    temp[2] = result.getType();

                    String id = result.getSlug();
                    if (id == null) {
                        id = Integer.toString(result.getID());
                    }
                    temp[3] = id;

                    cursor.addRow(temp);
                }
                mSearchResultAdapter.changeCursor(cursor);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("StreamActivity", error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return new ArrayList<SearchResult>();
    }
    @Override
    public boolean onSuggestionSelect(int i) {
        SearchResult searchResult = searchResults.get(i);
        String type = searchResult.getType();
        Intent detailIntent;

        if (type.equals("concept")) {
            detailIntent = new Intent(this, ConceptDetailActivity.class);
            detailIntent.putExtra("concept", searchResult.asConcept());

        } else if (type.equals("event")) {
            detailIntent = new Intent(this, EventDetailActivity.class);
            detailIntent.putExtra("event", searchResult.asEvent());

        } else { // Story
            detailIntent = new Intent(this, StoryDetailActivity.class);
            detailIntent.putExtra("story", searchResult.asStory());
        }

        startActivity(detailIntent);

        return true;
    }

    @Override
    public boolean onSuggestionClick(int i) {
        return onSuggestionSelect(i);
    }
}
