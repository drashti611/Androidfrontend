package com.example.androidfrontend.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfrontend.Model.Content;
import com.example.androidfrontend.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder> {

    private Context context;
    private List<Content> allContents;
    private List<Integer> subjectIds;
    private Map<Integer, String> subjectNames;
    private int expandedPosition = -1;

    public SubjectsAdapter(Context context, List<Content> contents) {
        this.context = context;
        this.allContents = contents;
        this.subjectIds = new ArrayList<>();
        this.subjectNames = new LinkedHashMap<>();

        for (Content c : contents) {
            if (!subjectIds.contains(c.getSubjectId())) {
                subjectIds.add(c.getSubjectId());
                subjectNames.put(c.getSubjectId(), c.getsubjectName());
            }
        }
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {
        int subjectId = subjectIds.get(position);
        String subjectName = subjectNames.get(subjectId);

        holder.tvSubjectTitle.setText("Subject Name: " + subjectName);

        final boolean isExpanded = position == expandedPosition;
        holder.contentLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.contentLayout.removeAllViews();

        if (isExpanded) {
            for (Content content : allContents) {
                if (content.getSubjectId() == subjectId) {
                    TextView tv = new TextView(context);
                    tv.setText("📄 " + content.getTitle());
                    tv.setPadding(8, 16, 8, 16);
                    tv.setTextColor(0xFF1E88E5); // Material blue
                    tv.setTextSize(16);
                    tv.setClickable(true);
                    tv.setOnClickListener(v -> {
                        String url = "http://10.0.2.2:5291/" + content.getFilePath();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(intent);
                    });
                    holder.contentLayout.addView(tv);
                }
            }
        }

        holder.subjectItemLayout.setOnClickListener(v -> {
            expandedPosition = isExpanded ? -1 : position;
            notifyItemRangeChanged(0, subjectIds.size());
        });
    }

    @Override
    public int getItemCount() {
        return subjectIds.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectTitle;
        LinearLayout contentLayout;
        LinearLayout subjectItemLayout;

        public SubjectViewHolder(View itemView) {
            super(itemView);
            tvSubjectTitle = itemView.findViewById(R.id.tvSubjectTitle);
            contentLayout = itemView.findViewById(R.id.contentLayout);
            subjectItemLayout = itemView.findViewById(R.id.subjectItemLayout);
        }
    }
}
