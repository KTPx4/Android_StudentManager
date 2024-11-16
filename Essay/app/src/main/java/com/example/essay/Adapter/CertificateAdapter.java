package com.example.essay.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.essay.R;
import com.example.essay.component.student.AddCertificate;
import com.example.essay.models.Certificate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder> {

    private Context context;
    private List<Certificate> certificateList;
    private FirebaseFirestore db;  // Declare Firestore instance

    // Constructor
    public CertificateAdapter(Context context, List<Certificate> certificateList) {
        this.context = context;
        this.certificateList = certificateList;
        this.db = FirebaseFirestore.getInstance();  // Initialize Firestore
    }

    @Override
    public CertificateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout for each certificate
        View view = LayoutInflater.from(context).inflate(R.layout.item_certificate, parent, false);
        return new CertificateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CertificateViewHolder holder, int position) {
        Certificate certificate = certificateList.get(position);
        holder.txtID.setText(certificate.getCertificateId());
        holder.txtCertificate.setText(certificate.getCertificateName());
        holder.txtIssuingOrganization.setText(certificate.getIssuingOrganization());

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm deletion")
                    .setMessage("Are you sure you want to delete this Certificate?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteCertificate(holder.getAdapterPosition());
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        holder.btnEdit.setOnClickListener(v -> {
            // Send certificate data to AddCertificate activity to edit
            Certificate certificateToEdit = certificateList.get(position);
            Intent intent = new Intent(context, AddCertificate.class);
            intent.putExtra("certificateId", certificateToEdit.getCertificateId());
            intent.putExtra("certificateName", certificateToEdit.getCertificateName());
            intent.putExtra("issuingOrganization", certificateToEdit.getIssuingOrganization());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return certificateList.size();
    }

    // ViewHolder class
    public static class CertificateViewHolder extends RecyclerView.ViewHolder {
        TextView txtID, txtCertificate, txtIssuingOrganization;
        ImageButton btnDelete, btnEdit;

        public CertificateViewHolder(View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtID);
            txtCertificate = itemView.findViewById(R.id.txt_Certificate);
            txtIssuingOrganization = itemView.findViewById(R.id.txt_IssuingOrganization);
            btnDelete = itemView.findViewById(R.id.btndel);
            btnEdit = itemView.findViewById(R.id.btnedit);
        }
    }
    public void updateList(List<Certificate> newList) {
        certificateList = newList;
        notifyDataSetChanged();
    }

    // Delete certificate method
    public void deleteCertificate(int position) {
        Certificate certificateToDelete = certificateList.get(position);
        String certificateId = certificateToDelete.getCertificateId();

        // Delete from Firestore
        db.collection("certificates")
                .whereEqualTo("certificateId", certificateId) // Query by certificateId
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot document : task.getResult()) {
                            // Delete the document from Firestore
                            db.collection("certificates").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Remove the certificate from the list and notify the adapter
                                        certificateList.remove(position);
                                        notifyItemRemoved(position);
                                        Toast.makeText(context, "Certificate deleted successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Error deleting certificate", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(context, "Certificate not found", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
