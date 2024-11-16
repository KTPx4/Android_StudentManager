package com.example.essay.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.R;
import com.example.essay.component.student.CertificateInfo;
import com.example.essay.models.CertificateModel;
import com.example.essay.services.StudentCallback;
import com.example.essay.services.StudentService;

import java.util.List;

public class CertificateAdapter2 extends RecyclerView.Adapter<CertificateAdapter2.CertificateViewHolder2> {

    private final List<CertificateModel> certificates;
    private final boolean isEditable;
    private final StudentService studentService;

    public CertificateAdapter2(List<CertificateModel> certificates, boolean isEditable) {
        this.certificates = certificates;
        this.isEditable = isEditable;
        this.studentService = new StudentService();
    }

    @NonNull
    @Override
    public CertificateAdapter2.CertificateViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_certificate_2, parent, false);
        return new CertificateViewHolder2(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder2 holder, int position) {
        CertificateModel certificate = certificates.get(position);
        holder.tvCertificateName.setText( "Name: " + certificate.getCertificateName());
        holder.tvIssue.setText( "Issue Day: "+certificate.getIssueDate());
        holder.tvExp.setText("Exp Day: "+certificate.getExpDate());
        holder.tvId.setText("Id: " + certificate.getId());
        if (isEditable) {

            holder.btnEdit.setVisibility(View.VISIBLE);

            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnEdit.setOnClickListener(v -> {
                // Edit logic
//                editCertificateDialog(certificate);
                Intent intent = new Intent(holder.itemView.getContext(), CertificateInfo.class);
                intent.putExtra("type", "edit");
                intent.putExtra("id", certificate.getId());
                intent.putExtra("studentId", certificate.getStudentId());
                intent.putExtra("certificateName", certificate.getCertificateName());
                intent.putExtra("issueDate", certificate.getIssueDate());
                intent.putExtra("expDate", certificate.getExpDate()); // Use the correct field name

                // Sử dụng startActivityForResult hoặc ActivityResultLauncher
                ((Activity) holder.itemView.getContext()).startActivityForResult(intent, 100); // 100 là request code

            });

            holder.btnDelete.setOnClickListener(v -> {
                // Delete logic
                studentService.deleteCertificate(certificate.getId(), new StudentCallback() {
                    @Override
                    public void onSuccess() {
                        certificates.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, certificates.size()); // Đảm bảo chỉ cập nhật lại các mục sau vị trí bị xóa
                    }

                    @Override
                    public void onFailure(String mess) {
                        Toast.makeText(holder.itemView.getContext(), mess, Toast.LENGTH_SHORT).show();
                    }
                });

            });
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return certificates.size();
    }

    public static class CertificateViewHolder2 extends RecyclerView.ViewHolder {
        TextView tvCertificateName, tvIssue, tvExp, tvId;
        Button btnEdit, btnDelete;

        public CertificateViewHolder2(@NonNull View itemView) {
            super(itemView);
            tvCertificateName = itemView.findViewById(R.id.tvCertificateName);
            tvIssue = itemView.findViewById(R.id.tvIssue);
            tvExp = itemView.findViewById(R.id.tvExp);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btn_cer_Delete);
            tvId = itemView.findViewById(R.id.tvId);
        }
    }
}
