package com.example.androidfrontend.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfrontend.Model.Faculty;
import com.example.androidfrontend.R;

import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {
    private List<Faculty> facultyList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public FacultyAdapter(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }

    @NonNull
    @Override
    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_row, parent, false);
        return new FacultyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewHolder holder, int position) {
        Faculty faculty = facultyList.get(position);

        holder.tvFacultyId.setText(String.valueOf(faculty.getFacultyId()));
        holder.tvFacultyName.setText(faculty.getFacultyName());
        holder.tvEmail.setText(faculty.getEmail());
        holder.tvGender.setText(faculty.getGender());

        // Highlight selected row
        holder.itemView.setBackgroundColor(
                selectedPosition == position ? Color.parseColor("#E0F7FA") : Color.TRANSPARENT
        );

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();

            // Example action on click
            Toast.makeText(v.getContext(), "Selected: " + faculty.getFacultyName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }

    public static class FacultyViewHolder extends RecyclerView.ViewHolder {
        TextView tvFacultyId, tvFacultyName, tvEmail, tvGender;

        public FacultyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFacultyId = itemView.findViewById(R.id.tvFacultyId);
            tvFacultyName = itemView.findViewById(R.id.tvFacultyName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvGender = itemView.findViewById(R.id.tvGender);
        }
    }
}
