package yyang3.tacoma.uw.edu.craftcellar;

/*
 * Craft Cellar: My Beverage Recycler View Adapter
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yyang3.tacoma.uw.edu.craftcellar.Beverage.Beverage;
import yyang3.tacoma.uw.edu.craftcellar.BeverageListFragment.OnListFragmentInteractionListener;


import java.util.List;

/**
 * The recycler view for the list fragment
 *
 * @author Tyler Braden and Yicong Yang
 * @version 1.0.0 alpha
 */
public class MyBeverageRecyclerViewAdapter extends RecyclerView.Adapter<MyBeverageRecyclerViewAdapter.ViewHolder> {

    private final List<Beverage> mValues;
    private final OnListFragmentInteractionListener mListener;

    /**
     * Constructor for the MyBeverageRecyclerViewAdapter
     * @param items the list of beverages
     * @param listener the listener for the beverage list
     */
    public MyBeverageRecyclerViewAdapter(List<Beverage> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * {@inheritDoc}
     * opens the fragment_beverage and returns its new viewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_beverage, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@inheritDoc}
     * displays the list of beverages one by one
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBeverage = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getmBrand());
        holder.mContentView.setText(mValues.get(position).getmTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mBeverage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Shows the beverage information item by item in this view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**the current view being used */
        public final View mView;
        /**the brand of beverage */
        public final TextView mIdView;
        /**the title of beverage */
        public final TextView mContentView;
        /**the current beverage */
        public Beverage mBeverage;

        /**
         *Constructor for ViewHolder
         * @param view the information for the view to be displayed
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        /**
         * {@inheritDoc}
         * added the contentView information to the string.
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
