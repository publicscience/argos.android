package co.publicscience.argos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.publicscience.argos.Models.Article;
import co.publicscience.argos.Models.Event;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EventDetailActivity extends Activity {

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);

        event = (Event)this.getIntent().getSerializableExtra("event");

        TextView titleTextView = (TextView)findViewById(R.id.title);
        titleTextView.setText(event.getTitle());

        TextView dateTextView = (TextView)findViewById(R.id.createdAt);
//        dateTextView.setText(String.format("%s, last updated %s",
//                event.getDaysAgo(),
//                DateUtils.getRelativeTimeSpanString(event.getUpdatedAt().getTime(), new Date().getTime(), 0).toString().toLowerCase()));
        dateTextView.setText(String.format("%s. %d articles.",
                event.getDaysAgo(),
                event.getArticles().size()));

        // Download and show the image.
        ImageView imageView = (ImageView)findViewById(R.id.image);
        Picasso.with(this).load(event.getImage()).fit().centerCrop().into(imageView);

        List<String> summary = event.getSummary();
        for (int i=0; i<summary.size(); i++) {
            summary.set(i, "• " + summary.get(i));
        }

        // Create the summary items.
        LinearLayout summaryView = (LinearLayout)findViewById(R.id.summary);
        for (String line : event.getSummary()) {
            View vi = getLayoutInflater().inflate(R.layout.summary_item, null);
            TextView summarySentenceView = (TextView)vi.findViewById(R.id.summarySentence);
            summarySentenceView.setText(line);
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

        // Enable the "Up" button for more navigation options
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
