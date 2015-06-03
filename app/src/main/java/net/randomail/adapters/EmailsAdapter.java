package net.randomail.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.randomail.R;
import net.randomail.models.Email;

import java.util.ArrayList;
import java.util.List;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.ViewHolder> implements Filterable {

    private List<Email> emails;
    private int itemLayout;
    private AppCompatActivity context;
    private EmailsFilter mEmailsFilter;

    public EmailsAdapter(AppCompatActivity context, List<Email> emails, int itemLayout) {
        this.emails = emails;
        this.context = context;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Email email = emails.get(position);


        holder.email.setText(email.email);
        holder.label.setText(email.label);
        /*if(email.label.length()==0){
            holder.label.setVisibility(View.GONE);
        }*/
        holder.forwards.setText("");

        for (int i = 0; i < email.forwardTo.size(); i++) {
            if (i == 0) {
                holder.forwards.setText(email.forwardTo.get(i).toString());
            } else {
                holder.forwards.setText(holder.forwards.getText() + "," + email.forwardTo.get(i).toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public void addItem(Email email) {
        emails.add(emails.size(), email);
        notifyItemInserted(emails.size());
    }

    public Email getItem(int position) {
        return emails.get(position);
    }

    public void removeItem(int position) {
        emails.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        emails.clear();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (mEmailsFilter == null)
            mEmailsFilter = new EmailsFilter();

        return mEmailsFilter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView forwards;
        public TextView email;
        public TextView label;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            email = (TextView) itemView.findViewById(R.id.email);
            forwards = (TextView) itemView.findViewById(R.id.forwards);
            label = (TextView) itemView.findViewById(R.id.label);
            this.view = itemView;
        }
    }

    private class EmailsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // Create a FilterResults object
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = emails;
                results.count = emails.size();
            } else {

                ArrayList<Email> filteredEmails = new ArrayList<Email>();

                // We'll go through all the contacts and see
                // if they contain the supplied string
                for (Email e : emails) {
                    if (e.label.toUpperCase().contains(constraint.toString().toUpperCase()) || e.email.toUpperCase().contains(constraint.toString().toUpperCase())) {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredEmails.add(e);
                    }
                }

                // Finally set the filtered values and size/count
                results.values = filteredEmails;
                results.count = filteredEmails.size();
            }

            // Return our FilterResults object
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            emails = (ArrayList<Email>) results.values;
            notifyDataSetChanged();
        }
    }
}

