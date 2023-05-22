package com.hongik.insecurememo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hongik.insecurememo.adapter.MemoListAdapter;
import com.hongik.insecurememo.item.MemoItem;

import org.conscrypt.Conscrypt;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    RecyclerView memoList;
    MemoListAdapter memoListAdapter;
    LinearLayoutManager layoutManager;

    Spinner categorySpinner;
    EditText memoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Security.insertProviderAt(Conscrypt.newProvider(), 1);

        context = this;
        setView();
    }

    private void setView() {
        categorySpinner = findViewById(R.id.category);
        memoEdit = findViewById(R.id.memo);

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(this);

        memoList = findViewById(R.id.recyclerview);

        setRecyclerView();
        setMemoListItem();
    }


    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        memoList.setLayoutManager(layoutManager);

        memoListAdapter = new MemoListAdapter(context, R.layout.row_memo_item, new ArrayList<MemoItem>());
        memoList.setAdapter(memoListAdapter);
    }
    private void setMemoListItem() {
        ArrayList<MemoItem> list = getMemoDummyList();
        memoListAdapter.addItemList(list);
    }

    private ArrayList<MemoItem> getMemoDummyList() {
        ArrayList<MemoItem> list = new ArrayList<>();

        MemoItem item1 = new MemoItem("일상", "배고프다");
        MemoItem item2 = new MemoItem("할일", "숙면");

        list.add(item1);
        list.add(item2);
        return list;
    }

    @Override
    public void onClick(View v) {
        registerMemo();
    }

    private void registerMemo() {
        String category = (String) categorySpinner.getSelectedItem();
        String memo = memoEdit.getText().toString();

        if (TextUtils.isEmpty(memo)){
            Toast.makeText(context, R.string.msg_memo_input, Toast.LENGTH_SHORT).show();
            return;
        }

        addMemoItem(category, memo);
        post(category, memo);

        categorySpinner.setSelection(0);
        memoEdit.setText("");

        hideKeyboard();
    }

    private void addMemoItem(String category, String memo) {
        MemoItem item = new MemoItem(category, memo);

        memoListAdapter.addItem(item);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void post(String category, String memo) {
        String url = "http://10.0.2.2:5000/api/memo";
        Map params = new HashMap();
        params.put("category", category);
        params.put("content", memo);

        //BODY에 파라미터 추가
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("category", String.valueOf(params.get("category")));
        formBuilder.add("content", String.valueOf(params.get("content"))); // 바디에 추가

        //HTTP 객체 선언
        OkHttpClient client = new OkHttpClient();
        Request.Builder request = new Request.Builder();

        request.addHeader("Content-Type", "application/json; charset=utf-8;"); // 헤더
        request.post(formBuilder.build());
        request.url(url); //httpBuilder 추가

        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Headers headers = request.build().headers();
                headers.toString();
                Log.e("", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("", "Request successful. Response: " + responseBody);
                } else {
                    Log.e("", "Request failed. Response code: " + response.code());
                }
            }
        });
    }
}
