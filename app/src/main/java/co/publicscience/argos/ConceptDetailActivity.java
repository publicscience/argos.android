package co.publicscience.argos;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import co.publicscience.argos.Models.Concept;
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
        ArgosService argosService = new ArgosService();
        argosService.getAPI().getConcept(concept.getSlug(), new Callback<Concept>() {
            @Override
            public void success(Concept concept, Response response) {
                TextView conceptCitations = (TextView)findViewById(R.id.conceptCitations);
                String sources = String.format("Data from %s", TextUtils.join(", ", concept.getSources()));
                conceptCitations.setText(sources);
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
