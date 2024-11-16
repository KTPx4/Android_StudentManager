package com.example.essay.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.R;
import com.example.essay.component.student.StudentInfo;
import com.example.essay.models.Student;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private Context context;
    // Constructor
    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.txtID.setText(student.getId());
        holder.txtName.setText(student.getName());
        holder.txtGender.setText("Gender: " + student.getGender());
        holder.txtGPA.setText(String.valueOf("GPA: " + student.getGPA()));
        holder.txtAge.setText(String.valueOf("Age: " + student.getAge()));
        // Add listeners for delete and more buttons
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm deletion")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteStudent(holder.getAdapterPosition());
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentInfo.class);
            intent.putExtra("studentId", student.getId());
            intent.putExtra("mode", "edit");
            context.startActivity(intent);
        });
        holder.btnInfo.setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentInfo.class);
            intent.putExtra("studentId", student.getId());
            intent.putExtra("mode", "info");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
    public void updateStudentList(List<Student> newStudentList) {
        this.studentList = newStudentList;
        notifyDataSetChanged();
    }
    // ViewHolder class
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView txtID, txtName, txtGender, txtGPA, txtAge;
        ImageButton btnDelete, btnEdit, btnInfo;

        public StudentViewHolder(View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtID);
            txtName = itemView.findViewById(R.id.txtName);
            txtGender = itemView.findViewById(R.id.txtGender);
            txtGPA = itemView.findViewById(R.id.txtGPA);
            txtAge = itemView.findViewById(R.id.txtAge);
            btnDelete = itemView.findViewById(R.id.btn_del);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnInfo = itemView.findViewById(R.id.btn_info);
        }
    }

    private void deleteStudent(int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String studentId = studentList.get(position).getId();
        db.collection("students").document(studentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Student has been deleted", Toast.LENGTH_SHORT).show();
                    studentList.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Delete failure:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
