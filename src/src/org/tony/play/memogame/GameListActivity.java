package org.tony.play.memogame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class GameListActivity extends FragmentActivity
        implements GameListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        if (findViewById(R.id.game_detail_container) != null) {
            mTwoPane = true;
            ((GameListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.game_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(GameDetailFragment.ARG_ITEM_ID, id);
            GameDetailFragment fragment = new GameDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.game_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, GameDetailActivity.class);
            detailIntent.putExtra(GameDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
