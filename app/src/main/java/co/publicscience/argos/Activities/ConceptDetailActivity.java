package co.publicscience.argos.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import co.publicscience.argos.Models.Concept;
import co.publicscience.argos.Models.Story;
import co.publicscience.argos.R;
import co.publicscience.argos.Services.ArgosService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ConceptDetailActivity extends ActionBarActivity {
    Concept concept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concept_detail);

        // Setup the toolbar as the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        concept = (Concept)this.getIntent().getSerializableExtra("concept");

        // Fetch the full concept data.
        final ConceptDetailActivity self = this;
        ArgosService argosService = new ArgosService();
        argosService.getAPI().getConcept(concept.getSlug(), new Callback<Concept>() {
            @Override
            public void success(Concept concept, Response response) {
                TextView conceptCitations = (TextView)findViewById(R.id.conceptCitations);
                String sources = String.format("Data from %s", TextUtils.join(", ", concept.getSources()));
                conceptCitations.setText(sources);

                // Create the event cards.
                LinearLayout storiesView = (LinearLayout)findViewById(R.id.stories);
                for (Story story : concept.getStories()) {
                    CardView vi = (CardView)getLayoutInflater().inflate(R.layout.story_card, null);

                    TextView storyTitle = (TextView)vi.findViewById(R.id.storyTitle);
                    TextView storyTimeAgo = (TextView)vi.findViewById(R.id.storyTimeAgo);
                    TextView storyEventCount = (TextView)vi.findViewById(R.id.storyEventCount);

                    storyTitle.setText(story.getTitle());
                    storyTimeAgo.setText(story.getTimeAgo());
                    storyEventCount.setText(String.format("%d events", story.getNumEvents()));

                    final Story st = story;
                    vi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent detailIntent = new Intent(self, StoryDetailActivity.class);
                            detailIntent.putExtra("story", st);
                            startActivity(detailIntent);
                        }
                    });

                    storiesView.addView(vi);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ConceptDetailActivity", error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set things using existing concept data.
        TextView conceptName = (TextView)findViewById(R.id.conceptName);
        TextView conceptSummary = (TextView)findViewById(R.id.conceptSummary);
        ImageView imageView = (ImageView)findViewById(R.id.conceptImage);

        conceptName.setText(concept.getName());
        conceptSummary.setText(concept.getSummary());
        Picasso.with(this).load(concept.getImage()).fit().centerCrop().into(imageView);
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
