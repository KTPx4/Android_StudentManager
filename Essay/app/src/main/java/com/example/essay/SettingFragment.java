package com.example.essay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.essay.component.user.HistoryInfo;
import com.example.essay.component.user.UserInfo;
import com.example.essay.services.AccountService;
import com.example.essay.services.ServiceCallback;
import com.example.essay.services.SvgLoader;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements  View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_NAME = "name";
    private static final String ARG_PHONE = "phone";
    private static final String ARG_BIRTH = "birth";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_LINKAVT = "email";


    // TODO: Rename and change types of parameters
    private String UserName;
    private String Role;
    private String Name, Phone, Birth, Email, linkAvt;
    private static boolean isAdmin = false;
    private Button btnEdit, btnChangePass;
    private TextView txtName, txtRole, txtPhone, txtBirth, txtEmail;
    private ActivityResultLauncher<Intent> userInfoLauncher;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private ImageView btnAvt;
    private Button btnLogout ;
    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static SettingFragment newInstance(String param1, String param2, String name, String phone, String birth, String email, String linkAvt) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_NAME, name);
        args.putString(ARG_PHONE, phone);
        args.putString(ARG_BIRTH, birth);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_LINKAVT, linkAvt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserName = getArguments().getString(ARG_PARAM1);
            Role = getArguments().getString(ARG_PARAM2);
            Name = getArguments().getString(ARG_NAME);
            Phone = getArguments().getString(ARG_PHONE);
            Birth = getArguments().getString(ARG_BIRTH);
            Email = getArguments().getString(ARG_EMAIL);
            linkAvt = getArguments().getString(ARG_LINKAVT);

            if(Role.toLowerCase().equals("admin")) isAdmin = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        btnEdit = view.findViewById(R.id.btn_setting_edit);
        btnChangePass = view.findViewById(R.id.btn_setting_change_pass);
        txtName = view.findViewById(R.id.txt_setting_name);
        txtRole = view.findViewById(R.id.txt_setting_role);
        txtPhone = view.findViewById(R.id.txt_setting_phone);
        txtBirth = view.findViewById(R.id.txt_setting_birth);
        txtEmail = view.findViewById(R.id.txt_setting_email);
        btnAvt = view.findViewById(R.id.avatarImageView);
        progressBar = view.findViewById(R.id.fragment_setting);
        setAvt();

        db = FirebaseFirestore.getInstance();

        txtName.setText(Name);
        txtRole.setText(Role);
        txtPhone.setText(Phone);
        txtBirth.setText(Birth);
        txtEmail.setText(Email);


        btnEdit.setOnClickListener(this);
        btnChangePass.setOnClickListener(this);

        userInfoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result for UserInfo activity

                        String rs = result.getData().getStringExtra("success");

                        if(rs != null && rs.equals("ok"))
                        {
                            Role = result.getData().getStringExtra("role");
                            Name = result.getData().getStringExtra("name");
                            Phone = result.getData().getStringExtra("phone");
                            Birth = result.getData().getStringExtra("birth");
                            Email = result.getData().getStringExtra("email");
                            linkAvt = result.getData().getStringExtra("linkAvt");

                            txtName.setText(Name);
                            txtPhone.setText(Phone);
                            txtBirth.setText(Birth);
                            txtEmail.setText(Email);
                            txtRole.setText(Role);
                            setAvt();
                        }
                    }
                }
        );
        try{
            LoadData(UserName);
        }
        catch (Exception e)
        {
            Log.w("Setting", "Error getting documents.", e.fillInStackTrace());

        }
        return view;
    }
    private void setAvt()
    {
        SvgLoader.loadSvgFromUrl(linkAvt, btnAvt);
//        Glide.with(getContext())
//                .load(linkAvt) // Đường dẫn tới ảnh
//                .error(R.drawable.ic_person_foreground)
//                .into(btnAvt); // ImageButton bạn muốn thiết lập ảnh
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_setting_edit)
        {
            Intent intent = null;
            intent = new Intent(getContext(), UserInfo.class);
            intent.putExtra("type", "profile");
            intent.putExtra("role", Role);
            intent.putExtra("name", Name);
            intent.putExtra("user", UserName);
            intent.putExtra("phone", Phone);
            intent.putExtra("birth", Birth);
            intent.putExtra("email", Email);
            intent.putExtra("linkAvt", linkAvt);

            userInfoLauncher.launch(intent);
        }
        else if(id == R.id.btn_setting_change_pass)
        {
            // Tạo AlertDialog mới cho Fast Create với hai trường nhập User và Name
            AlertDialog.Builder passDialog = new AlertDialog.Builder(getContext());
            passDialog.setTitle("Change password");

            // Inflate layout từ XML
            LayoutInflater inflater = LayoutInflater.from(getContext());
            @SuppressLint("InflateParams")
            View dialogView = inflater.inflate(R.layout.setting_dialog_change_pass, null);


            EditText oldPass = dialogView.findViewById(R.id.dialog_setting_oldPass);
            EditText newPass = dialogView.findViewById(R.id.dialog_setting_newPass);
            EditText confirmInput = dialogView.findViewById(R.id.dialog_setting_confirmPass);

            passDialog.setView(dialogView);

            // Nút Xác nhận
            passDialog.setPositiveButton("Confirm", (dialogInterface, i) -> {});

            // Nút Hủy
            passDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });

            // Hiển thị AlertDialog Fast Create
            AlertDialog dialog =  passDialog.create() ;
            dialog.show();
            Button buttonOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button buttonCancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String old = oldPass.getText().toString();
                    String newP = newPass.getText().toString();
                    String confirm = confirmInput.getText().toString();

                    if (old.isEmpty() || newP.isEmpty() || confirm.isEmpty())
                    {
                        Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(!newP.equals(confirm))
                    {
                        Toast.makeText(getContext(), "Confirm password not match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AccountService accountService = new AccountService();
                    accountService.changePass(UserName, old, newP, new ServiceCallback() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Change password success", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onResult(boolean exists) {

                        }
                    });
                }
            });
        }
    }

    public boolean LoadData(String user) throws Exception
    {
        progressBar.setVisibility(View.VISIBLE);

        AtomicReference<String> mess = new AtomicReference<>("");

        db.collection("accounts")
                .whereEqualTo("user", user)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {

                            progressBar.setVisibility(View.GONE);

                            // Lấy tài liệu đầu tiên (vì user là duy nhất)
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            // Lấy mật khẩu và role từ Firestore
                            String storedPassword = document.getString("pass");
                            String userRole = document.getString("role");
                            String name = document.getString("name");
                            String phone = document.getString("phone");
                            String birth = document.getString("birthDay");
                            String email = document.getString("email");
                            String link = document.getString("linkAvt");

                            Role = userRole;
                            Name = name;
                            Phone = phone;
                            Birth = birth;
                            Email = email;
                            linkAvt = link;

                            txtName.setText(Name);
                            txtPhone.setText(Phone);
                            txtBirth.setText(Birth);
                            txtEmail.setText(Email);
                            txtRole.setText(Role);
                            setAvt();

                        }
                        else
                        {


                        }
                    } else {

                        Log.w("Setting", "Error getting documents.", task.getException());
                    }
                });

        return true;

    }
}