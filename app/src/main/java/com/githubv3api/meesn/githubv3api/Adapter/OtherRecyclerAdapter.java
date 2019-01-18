package com.githubv3api.meesn.githubv3api.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.githubv3api.meesn.githubv3api.FileList;
import com.githubv3api.meesn.githubv3api.R;
import com.githubv3api.meesn.githubv3api.database.Repository;

import java.util.ArrayList;
import java.util.List;

public class OtherRecyclerAdapter extends RecyclerView.Adapter<OtherRecyclerAdapter.UserRecyclerHolder> implements Filterable {

    private List<Repository> listUsers = new ArrayList<Repository>();
    private List<Repository> listUsersFull;

    @NonNull
    @Override
    public UserRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.repo_name, viewGroup, false);
        return new UserRecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerHolder userRecyclerHolder, int i) {
        final Repository repo = listUsers.get(i);
        if (repo.getName() != null)
        {
            Log.d("UserList", repo.getName());
            userRecyclerHolder.name.setText(repo.getName());
            userRecyclerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FileList.class);
                    intent.putExtra("userLoginName", repo.getUsername());
                    intent.putExtra("repoName", repo.getName());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public void setUsers(List<Repository> listUsers)
    {
        this.listUsers = listUsers;
        listUsersFull = new ArrayList<Repository>(listUsers);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Repository> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(listUsersFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Repository repository:listUsersFull) {
                    if (repository.getName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(repository);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values!=null)
            {
                listUsers.clear();
                listUsers.addAll((List<Repository>)results.values);
            }
            notifyDataSetChanged();
        }
    };

    public class UserRecyclerHolder extends RecyclerView.ViewHolder
    {
        private TextView name;

        public UserRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.repo);
        }
    }
}
