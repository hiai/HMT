package cn.edu.scau.hometown.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import cn.edu.scau.hometown.R;

/**
 * Created by Admin on 2015/11/22.
 */
public class ResultOfSearchActivity extends AppCompatActivity {
    //搜索的关键词
    String keyWord;
    Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partition);

        Init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_resultofsearch, menu);
        SearchView sv = (SearchView) menu.findItem(R.id.ab_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String title = "搜索 "+query+" 的结果";
                InitToolBar(title);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void Init(){
        keyWord = (String)getIntent().getSerializableExtra("keyWord");

        toolbar = (Toolbar) findViewById(R.id.partition_toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.tab_brown));
        String title = "搜索 "+keyWord+" 的结果";
       InitToolBar(title);

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.setRefreshing);
        mSwipeRefreshWidget.setEnabled(false);
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));


    }

   private void InitToolBar(String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
