package com.example.essay.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.R;
import com.example.essay.models.Certificate;

import java.util.List;

public class ChooseCertificateAdapter extends RecyclerView.Adapter<ChooseCertificateAdapter.CertificateViewHolder> {

    private List<Certificate> certificateList;

    public ChooseCertificateAdapter(List<Certificate> certificateList) {
        this.certificateList = certificateList;
    }

    @NonNull
    @Override
    public CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_choose_certificate, parent, false);
        return new CertificateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder holder, int position) {
        Certificate certificate = certificateList.get(position);
        holder.txtCertificateName.setText(certificate.getCertificateName());
        holder.cbCertificate.setOnCheckedChangeListener(null); // Ngắt kết nối trước khi cập nhật
        holder.cbCertificate.setChecked(certificate.isSelected());
        holder.cbCertificate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            certificate.setSelected(isChecked); // Lưu trạng thái checkbox vào đối tượng Certificate
        });
    }


    @Override
    public int getItemCount() {
        return certificateList.size();
    }

    public static class CertificateViewHolder extends RecyclerView.ViewHolder {
        TextView txtCertificateName;
        CheckBox cbCertificate;

        public CertificateViewHolder(View itemView) {
            super(itemView);
            txtCertificateName = itemView.findViewById(R.id.txt_CertificateName);
            cbCertificate = itemView.findViewById(R.id.cbCertificate);
        }
    }
}
