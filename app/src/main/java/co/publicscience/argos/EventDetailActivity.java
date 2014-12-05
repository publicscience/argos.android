package co.publicscience.argos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import co.publicscience.argos.Models.Article;
import co.publicscience.argos.Models.Concept;
import co.publicscience.argos.Models.Event;
import co.publicscience.argos.Models.SummarySentence;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EventDetailActivity extends ActionBarActivity {

    Event event;
    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);

        // Setup the toolbar as the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        event = (Event)this.getIntent().getSerializableExtra("event");

        // Setup the mentions drawer.
        List<Concept> mentions = event.getConcepts();
        Collections.sort(mentions);
        RecyclerView mentionsDrawer = (RecyclerView)findViewById(R.id.mentions_drawer);
        mentionsDrawer.setLayoutManager(new LinearLayoutManager(this));
        mentionsDrawer.setItemAnimator(new DefaultItemAnimator());
        ConceptAdapter adapter = new ConceptAdapter(mentions, R.layout.concept_item, this);
        mentionsDrawer.setAdapter(adapter);

        final EventDetailActivity self = this;
        adapter.SetOnItemClickListener(new ConceptAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Concept concept) {
                Intent detailIntent = new Intent(self, ConceptDetailActivity.class);
                detailIntent.putExtra("concept", concept);
                startActivity(detailIntent);
            }
        });

        TextView titleTextView = (TextView)findViewById(R.id.eventTitle);
        titleTextView.setText(event.getTitle());

        TextView dateTextView = (TextView)findViewById(R.id.createdAt);
//        dateTextView.setText(String.format("%s, last updated %s",
//                event.getDaysAgo(),
//                DateUtils.getRelativeTimeSpanString(event.getUpdatedAt().getTime(), new Date().getTime(), 0).toString().toLowerCase()));
        dateTextView.setText(String.format("%s. %d articles.",
                event.getDaysAgo(),
                event.getArticles().size()));

        // Download and show the image.
        ImageView imageView = (ImageView)findViewById(R.id.eventImage);
        Picasso.with(this).load(event.getImage()).fit().centerCrop().into(imageView);

        // Add bullet points to summary items TO DO make this nicer
        List<SummarySentence> summary = event.getSummary();
//        for (int i=0; i<summary.size(); i++) {
//            summary.set(i, "• " + summary.get(i));
//        }

        // Create the summary items.
        LinearLayout summaryView = (LinearLayout)findViewById(R.id.eventSummary);
        for (SummarySentence summarySentence : event.getSummary()) {
            View vi = getLayoutInflater().inflate(R.layout.summary_item, null);
            TextView summarySentenceView = (TextView)vi.findViewById(R.id.summarySentence);
            summarySentenceView.setText(summarySentence.getSentence());
            summaryView.addView(vi);
        }

        // Create the source article cards.
        LinearLayout sourcesView = (LinearLayout)findViewById(R.id.sources);
        for (Article article : event.getArticles()) {
            View vi = getLayoutInflater().inflate(R.layout.article_card, null);

            TextView articleTitleView = (TextView)vi.findViewById(R.id.articleTitle);
            TextView sourceNameView = (TextView)vi.findViewById(R.id.sourceName);
            ImageView sourceIconView = (ImageView)vi.findViewById(R.id.sourceIcon);

            Picasso.with(this).load("http://www.nytimes.com/favicon.ico").fit().into(sourceIconView);
            articleTitleView.setText(article.getTitle());
            sourceNameView.setText(article.getSource().getName());

            final String extUrl = article.getExtUrl();
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(extUrl));
                    startActivity(browserIntent);
                }
            });

            sourcesView.addView(vi);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu.
        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the sharing submenu
        if (shareItem != null) {
            mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareItem);
        }

        // Create an Intent to share your content
        setShareIntent();

        return true;
    }


    private void setShareIntent() {

        if (mShareActionProvider != null) {

            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "ay yoyoyoy");

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
