package co.publicscience.argos.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import co.publicscience.argos.Models.Event;
import co.publicscience.argos.Models.Story;
import co.publicscience.argos.R;
import co.publicscience.argos.Services.ArgosService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StoryDetailActivity extends ActionBarActivity {
    Story story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_detail);

        // Setup the toolbar as the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        story = (Story)this.getIntent().getSerializableExtra("story");

        // Fetch the full story data.
        final StoryDetailActivity self = this;
        ArgosService argosService = new ArgosService();
        argosService.getAPI().getStory(story.getID(), new Callback<Story>() {
            @Override
            public void success(Story story, Response response) {
                self.story = story;

                TextView storyTitle = (TextView) findViewById(R.id.storyTitle);
                storyTitle.setText(story.getTitle());

                TextView storyMeta = (TextView) findViewById(R.id.storyMeta);
                storyMeta.setText(String.format("%s events.", story.getEvents().size()));

                ImageView imageView = (ImageView) findViewById(R.id.storyImage);
                Picasso.with(self).load(story.getImage()).fit().centerCrop().into(imageView);

                // Create the event cards.
                LinearLayout eventsView = (LinearLayout)findViewById(R.id.events);
                for (Event event : story.getEvents()) {
                    CardView vi = (CardView)getLayoutInflater().inflate(R.layout.event_card, null);

                    TextView eventTitle = (TextView)vi.findViewById(R.id.eventTitle);
                    TextView eventTimeAgo = (TextView)vi.findViewById(R.id.eventTimeAgo);
                    TextView eventArticleCount = (TextView)vi.findViewById(R.id.eventArticleCount);

                    // TO DO make a better way of evaluating event importance/popularity
                    if (event.getScore() > 6304.0) {
                        vi.setCardBackgroundColor(getResources().getColor(R.color.highlight));
                        eventTitle.setTextColor(getResources().getColor(R.color.white));
                    }
                    eventTitle.setText(event.getTitle());
                    eventTimeAgo.setText(event.getTimeAgo());
                    eventArticleCount.setText(String.format("%d articles", event.getNumArticles()));

                    final Event ev = event;
                    vi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent detailIntent = new Intent(self, EventDetailActivity.class);
                            detailIntent.putExtra("event", ev);
                            startActivity(detailIntent);
                        }
                    });

                    eventsView.addView(vi);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("StoryDetailActivity", error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Back button
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
