package com.example.essay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.essay.Adapter.UserAdapter;
import com.example.essay.component.user.HistoryInfo;
import com.example.essay.component.user.UserInfo;
import com.example.essay.models.account.User;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String COLECTION_NAME = "accounts";
    // TODO: Rename and change types of parameters
    private String UserName;
    private String Role;
    private static boolean isAdmin = false;
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private Button btnAllUser;
    private Button btnManagerUser;
    private Button btnEmployeeUser;
    private EditText edtSearch;
    private ProgressBar progressBar;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private ActivityResultLauncher<Intent> userInfoLauncher;


    public UserFragment() {
        // Required empty public constructor
    }

     public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserName = getArguments().getString(ARG_PARAM1);
            Role = getArguments().getString(ARG_PARAM2);
            if(Role.toLowerCase().equals("admin")) isAdmin = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        edtSearch = view.findViewById(R.id.txtSearch);
        btnAllUser = view.findViewById(R.id.btnAllUser);
        btnManagerUser = view.findViewById(R.id.btnManagerUser);
        btnEmployeeUser = view.findViewById(R.id.btnEmployeeUser);
        edtSearch = view.findViewById(R.id.txtSearch);
        progressBar = view.findViewById(R.id.fragment_user);

        btnAllUser.setOnClickListener(this);
        btnManagerUser.setOnClickListener(this);
        btnEmployeeUser.setOnClickListener(this);
        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize the user info launcher
        userInfoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result for UserInfo activity

                        String rs = result.getData().getStringExtra("success");

                        if(rs != null && rs.equals("ok"))
                        {
                            Log.d("HomeActivity", "it run: ");
                            // reload User Fragment
                            initUser_v2("", "");
                        }
                    }
                }
        );
        searchName();
        initAdmin(view);
        initUser_v2("", "");
        return view;
    }
    private void searchName() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần xử lý ở đây
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Hủy tìm kiếm trước đó nếu người dùng tiếp tục nhập
                searchHandler.removeCallbacks(searchRunnable);


                // Tạo Runnable cho tìm kiếm
                searchRunnable = () -> {
                    // Thực hiện tìm kiếm với chuỗi nhập
                    String searchText = s.toString().trim();
                    initUser_v2(searchText, ""); // Gọi hàm tìm kiếm với chuỗi nhập
                };

                // Đặt độ trễ 300ms trước khi bắt đầu tìm kiếm
                searchHandler.postDelayed(searchRunnable, 600);
            }
        });
    }


    public void initUser_v2(String searchName, String RoleSearch) {
        // Hiển thị ProgressBar
        progressBar.setVisibility(View.VISIBLE);



        // Khởi tạo danh sách người dùng
        userList = new ArrayList<>();


        // Lấy instance của Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference accountsRef = db.collection(COLECTION_NAME);

        // Nếu biến searchName khác null và không rỗng, thực hiện tìm kiếm với điều kiện
        accountsRef
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        userList.clear();
                        // Xóa danh sách người dùng cũ
                        int countALl = -1; // except admin
                        int countManager = 0;
                        int countEmployee = 0;
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String name = document.getString("name");
                            String user = document.getString("user");
                            String phone = document.getString("phone");
                            String role = document.getString("role");
                            String linkAvt = document.getString("linkAvt");
                            String birth = document.getString("birthDay");
                            String email = document.getString("email");

                            countALl ++;
                            if(role !=null && role.toLowerCase().equals("manager")) countManager++;
                            else if(role !=null && role.toLowerCase().equals("employee")) countEmployee++;
                            // Kiểm tra xem tên có chứa chuỗi tìm kiếm hay không
                            if (name != null &&
                                    name.toLowerCase().contains(searchName.toLowerCase()) &&
                                    role.toLowerCase().contains(RoleSearch.toLowerCase()) &&
                                    !role.toLowerCase().equals("admin")
                            )
                            {
                                User newUser = new User(name, user, phone, role, linkAvt,birth, email);
                                userList.add(newUser);

                            }
                        }

                        // Cập nhật adapter
                        userAdapter.notifyDataSetChanged();

                        // Cập nhật nút sau khi tải dữ liệu
                        btnAllUser.setText(countALl + "\nAll");
                        btnManagerUser.setText(countManager + "\nManager");
                        btnEmployeeUser.setText(countEmployee + "\nEmployee");

                    } else {
                        // Xử lý lỗi
                    }
                });

        // Thiết lập adapter cho RecyclerView
        userAdapter = new UserAdapter(userList, Role, new OnUserClickListener() {
            @Override
            public void onUserClick(User User, String type) {
                Intent intent = null;
                if(type.equals("history"))
                {
                    intent = new Intent(getContext(), HistoryInfo.class);
                }
                else{
                    intent = new Intent(getContext(), UserInfo.class);
                }

                intent.putExtra("type", type);
                intent.putExtra("role", User.getRole());
                intent.putExtra("name", User.getName());
                intent.putExtra("user", User.getUser());
                intent.putExtra("phone", User.getPhone());
                intent.putExtra("birth", User.getBirth());
                intent.putExtra("email", User.getEmail());

                userInfoLauncher.launch(intent);

            }
        });
        Runnable refreshListener = this::refresh;

        userAdapter.setRefreshListener( refreshListener);
        recyclerViewUsers.setAdapter(userAdapter);
    }
    public void refresh() {
        initUser_v2("", "");
    }
    private void initAdmin(View view) {
        if(!isAdmin) return;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnAllUser)
        {
            initUser_v2("", "");
            Log.d("UserFragment", "onClick: btnAll");
        }
        else if(id == R.id.btnManagerUser)
        {
            initUser_v2("", "manager");
            Log.d("UserFragment", "onClick: btnManager");
        }
        else if(id == R.id.btnEmployeeUser)
        {
            initUser_v2("", "employee");
            Log.d("UserFragment", "onClick: btnEmployeeUser");

        }
        else{
            Log.d("UserFragment", "onClick: "+ v.getContext().toString());

        }
    }

    public interface OnUserClickListener {
        void onUserClick(User user, String type); // Phương thức được gọi khi click vào user
    }
}
