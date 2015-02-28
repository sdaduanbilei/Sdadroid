package sda.sdadroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.scorpio.frame.util.ToolsUtil;
import com.scorpio.frame.view.pulltorefresh.PullToRefreshBase;
import com.scorpio.frame.view.pulltorefresh.PullToRefreshListView;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;


public class HomeActivity extends ActionBarActivity {

    @ViewInject(id = R.id.pulltorefresh)
    PullToRefreshListView pullToRefreshListView ;
    @ViewInject(id = R.id.toolbar)
    Toolbar toolbar ;

    ListView listView ;

    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FinalActivity.initInjectedView(this);
        context = HomeActivity.this ;
        listView = pullToRefreshListView.getRefreshableView() ;
        setSupportActionBar(toolbar);

        setUp();
    }

    private void setUp() {
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToolsUtil.toast(context,"正在刷新....");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
}
