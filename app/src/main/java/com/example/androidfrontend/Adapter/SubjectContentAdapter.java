package com.example.androidfrontend.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfrontend.Model.Subject;
import com.example.androidfrontend.R;

import java.util.List;

public class SubjectContentAdapter extends RecyclerView.Adapter<SubjectContentAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public SubjectContentAdapter(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use your correct row layout file
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_row, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);

//        holder.tvSubjectId.setText(String.valueOf(subject.getSubjectId()));
        holder.tvSubjectName.setText(subject.getSubjectName());
        holder.tvDeptName.setText(subject.getDeptName());
        holder.tvSemId.setText(String.valueOf(subject.getSemId()));

        // Highlight selected item
        holder.itemView.setBackgroundColor(
                selectedPosition == position ? Color.parseColor("#FFF9C4") : Color.TRANSPARENT
        );

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            Toast.makeText(v.getContext(), "Selected: " + subject.getSubjectName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectId, tvSubjectName, tvDeptName, tvSemId;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvSubjectId = itemView.findViewById(R.id.tvSubjectId);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvDeptName = itemView.findViewById(R.id.tvDeptName);
            tvSemId = itemView.findViewById(R.id.tvSemId);
        }
    }
}
