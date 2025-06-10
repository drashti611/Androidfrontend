package com.example.androidfrontend;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.androidfrontend.Api.ApiClient;
import com.example.androidfrontend.Api.ApiService;
import com.example.androidfrontend.Model.FeeDetailsResponse;
import com.example.androidfrontend.Model.PaymentRequest;
import com.example.androidfrontend.Model.PaymentStatusResponse;
import com.example.androidfrontend.Model.StudentFee;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeePaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "FeePaymentActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;

    private TextView txtDepartment, txtSemester, txtAmount, txtStatus;
    private Button btnPayNow, btnDownloadReceipt;
    private ProgressBar progressBar;

    private String departmentName = "", semesterName = "", token;
    private double amount = 0.0;
    private boolean paymentSuccess = false;
    private int studentId, feeStructureId;
    private LinearLayout lottieContainer, profileLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_payment);
        lottieContainer = findViewById(R.id.lottieContainer);
        profileLayout = findViewById(R.id.subjectLayoutt);
        LottieAnimationView lottieView = findViewById(R.id.lottieSplash);

        // Initially show Lottie, hide profile layout
        lottieContainer.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Fee Payment");
        }        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        txtDepartment = findViewById(R.id.txtDepartment);
        txtSemester = findViewById(R.id.txtSemester);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);
        btnPayNow = findViewById(R.id.btnPayNow);
        btnDownloadReceipt = findViewById(R.id.btnDownloadReceipt);
        progressBar = findViewById(R.id.progressBar);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        token = prefs.getString("token", null);
        studentId = prefs.getInt("studentId", -1);

        if (token == null || studentId == -1) {
            Toast.makeText(this, "Student not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchFeeDetails(token, studentId);

        btnPayNow.setOnClickListener(v -> {
            if (!paymentSuccess) {
                startPayment();
            }
        });

        btnDownloadReceipt.setOnClickListener(v -> {
            if (checkPermission()) {
                fetchAndGenerateReceipt();
            } else {
                requestPermission();
            }
        });
        lottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                // After animation ends
                lottieContainer.setVisibility(View.GONE);
                profileLayout.setVisibility(View.VISIBLE);
                fetchFeeDetails(token, studentId);
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
    }

    private void fetchFeeDetails(String token, int studentId) {
    showLoading(true);
    ApiService apiService = ApiClient.getApiService();
    Call<FeeDetailsResponse> call = apiService.getFeePaymentDetails("Bearer " + token, studentId);

    call.enqueue(new Callback<FeeDetailsResponse>() {
        @Override
        public void onResponse(Call<FeeDetailsResponse> call, Response<FeeDetailsResponse> response) {
            showLoading(false);
            if (response.isSuccessful() && response.body() != null) {

                FeeDetailsResponse details = response.body();
                departmentName = details.getDepartmentName();
                semesterName = details.getSemesterName();
                amount = details.getAmount();
                feeStructureId = details.getFeeStructureId();

                txtDepartment.setText(departmentName);
                txtSemester.setText(semesterName);
                txtAmount.setText("₹ " + amount);
                checkPaymentStatus(token, studentId, feeStructureId);
            } else {
                Toast.makeText(FeePaymentActivity.this, "Failed to load fee details", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<FeeDetailsResponse> call, Throwable t) {
            showLoading(false);
            Toast.makeText(FeePaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

    private void checkPaymentStatus(String token, int studentId, int feeStructureId) {
    ApiService apiService = ApiClient.getApiService();
    Call<PaymentStatusResponse> call = apiService.checkPaymentStatus("Bearer " + token, studentId, feeStructureId);

    call.enqueue(new Callback<PaymentStatusResponse>() {
        @Override
        public void onResponse(Call<PaymentStatusResponse> call, Response<PaymentStatusResponse> response) {
            if (response.isSuccessful() && response.body() != null && response.body().isPaid()) {
                paymentSuccess = true;
                txtStatus.setText("✅ Fees already paid.");
                btnPayNow.setVisibility(View.GONE);
                btnDownloadReceipt.setVisibility(View.VISIBLE);
            } else {
                paymentSuccess = false;
                btnPayNow.setVisibility(View.VISIBLE);
                btnDownloadReceipt.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailure(Call<PaymentStatusResponse> call, Throwable t) {
            Toast.makeText(FeePaymentActivity.this, "Error checking payment status: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

    private void startPayment() {
    Checkout checkout = new Checkout();
    checkout.setKeyID("rzp_test_mKFFsoRNrHIPv0");

    try {
        JSONObject options = new JSONObject();
        options.put("name", "ICT Home");
        options.put("description", "Semester Fee Payment");
        options.put("currency", "INR");
        options.put("amount", (int) (amount * 100));

        JSONObject prefill = new JSONObject();
        prefill.put("email", "student@example.com");
        prefill.put("contact", "9876543210");

        options.put("prefill", prefill);
        checkout.open(FeePaymentActivity.this, options);
    } catch (Exception e) {
        Toast.makeText(this, "Payment error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Payment start failed", e);
    }
}

    @Override
public void onPaymentSuccess(String razorpayPaymentID) {
    paymentSuccess = true;
    txtStatus.setText("🎉 Payment Successful! ID: " + razorpayPaymentID);
    btnPayNow.setVisibility(View.GONE);
    btnDownloadReceipt.setVisibility(View.VISIBLE);

    PaymentRequest request = new PaymentRequest(studentId, feeStructureId, amount, amount, razorpayPaymentID);
    ApiService apiService = ApiClient.getApiService();
    Call<Void> call = apiService.savePayment("Bearer " + token, request);

    call.enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            Toast.makeText(FeePaymentActivity.this,
                    response.isSuccessful() ? "Payment recorded successfully" : "Failed to save payment",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Toast.makeText(FeePaymentActivity.this, "Error saving payment: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

    private void fetchAndGenerateReceipt() {
    ApiService apiService = ApiClient.getApiService();
    Call<StudentFee> call = apiService.GetStudentFee(studentId);

    call.enqueue(new Callback<StudentFee>() {
        @Override
        public void onResponse(Call<StudentFee> call, Response<StudentFee> response) {
            if (response.isSuccessful() && response.body() != null) {
                generatePdfReceipt(response.body());
            } else {
                Toast.makeText(FeePaymentActivity.this, "Failed to load receipt details", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<StudentFee> call, Throwable t) {
            Toast.makeText(FeePaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

    private void generatePdfReceipt(StudentFee details) {
    PdfDocument pdfDocument = new PdfDocument();
    Paint paint = new Paint();
    Paint titlePaint = new Paint();
    Paint bluePaint = new Paint();
    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
    Canvas canvas = page.getCanvas();

    int x = 40, y = 50;
    titlePaint.setTextSize(15f);
    titlePaint.setFakeBoldText(true);
    titlePaint.setColor(Color.BLUE);

    canvas.drawText("ICT'S Home", x, y, titlePaint);

    y += 20;


    bluePaint.setColor(Color.BLUE);
    bluePaint.setTextSize(14f);
    canvas.drawText("University Campus, Udhna-Magdalla Road, SURAT - 395007", x, y, bluePaint);
    y += 30;

    Paint boldPaint = new Paint();
    boldPaint.setFakeBoldText(true);
    boldPaint.setTextSize(13f);

    paint.setTextSize(13f);
    canvas.drawText("RECEIPT", x, y, boldPaint);
    y += 30;

    canvas.drawText("Transaction Id.:", x, y, boldPaint);
    canvas.drawText(details.getTransactionId(), x + 100, y, paint);
    canvas.drawText("Date:", x + 400, y, boldPaint);
    canvas.drawText(details.getPaymentDate().split("T")[0], x + 440, y, paint);
    y += 20;

    canvas.drawText("Received From:", x, y, boldPaint);
    canvas.drawText(details.getStudentName(), x + 100, y, paint);
    y += 20;

    canvas.drawText("Particulars:", x, y, boldPaint);
    canvas.drawText(details.getDepartmentName() + " - " + details.getSemesterName(), x + 100, y, paint);
    y += 30;

    // Table Config
    int tableStartY = y;
    int rowHeight = 30;
    int colSrNo = x;
    int colDesc = x + 80;
    int colAmount = x + 350;
    int colEnd = colAmount + 120;

    Paint headerPaint = new Paint();
    headerPaint.setColor(Color.rgb(135, 206, 235)); // Sky Blue

    // Draw table header background
    canvas.drawRect(colSrNo, tableStartY, colEnd, tableStartY + rowHeight, headerPaint);

    paint.setFakeBoldText(true);
    canvas.drawText("Sr. No.", colSrNo + 5, tableStartY + 20, paint);
    canvas.drawText("Description", colDesc + 5, tableStartY + 20, paint);
    canvas.drawText("Amount (Rs.)", colAmount + 5, tableStartY + 20, paint);
    paint.setFakeBoldText(false);

    // Draw table borders (6 rows: header + 4 items + total)
    for (int i = 0; i <= 6; i++) {
        int lineY = tableStartY + i * rowHeight;
        canvas.drawLine(colSrNo, lineY, colEnd, lineY, paint);  // Horizontal lines
    }
    // Vertical lines
    canvas.drawLine(colSrNo, tableStartY, colSrNo, tableStartY + 6 * rowHeight, paint);
    canvas.drawLine(colDesc, tableStartY, colDesc, tableStartY + 6 * rowHeight, paint);
    canvas.drawLine(colAmount, tableStartY, colAmount, tableStartY + 6 * rowHeight, paint);
    canvas.drawLine(colEnd, tableStartY, colEnd, tableStartY + 6 * rowHeight, paint);

    // Table rows
    y = tableStartY + rowHeight;
    canvas.drawText("1", colSrNo + 5, y + 20, paint);
    canvas.drawText("Tuition Fee", colDesc + 5, y + 20, paint);
    canvas.drawText("₹ " + details.getFeeType().getTuitionFees(), colAmount + 5, y + 20, paint);
    y += rowHeight;

    canvas.drawText("2", colSrNo + 5, y + 20, paint);
    canvas.drawText("Laboratory Fee", colDesc + 5, y + 20, paint);
    canvas.drawText("₹ " + details.getFeeType().getLabFees(), colAmount + 5, y + 20, paint);
    y += rowHeight;

    canvas.drawText("3", colSrNo + 5, y + 20, paint);
    canvas.drawText("Ground Fee", colDesc + 5, y + 20, paint);
    canvas.drawText("₹ " + details.getFeeType().getCollegeGroundFee(), colAmount + 5, y + 20, paint);
    y += rowHeight;

    canvas.drawText("4", colSrNo + 5, y + 20, paint);
    canvas.drawText("Internal Examination", colDesc + 5, y + 20, paint);
    canvas.drawText("₹ " + details.getFeeType().getInternalExam(), colAmount + 5, y + 20, paint);
    y += rowHeight;

    // Total row inside table
    x+=50;
    String totalLabel = "Total (Rs.):";
    String totalAmountStr = "₹ " + details.getPaidAmount();

// Draw total row aligned to left, like other rows
    canvas.drawText(totalLabel, colDesc + 5, y + 20, boldPaint);
    canvas.drawText(totalAmountStr, colAmount + 5, y + 20, boldPaint);

    float totalLabelWidth = boldPaint.measureText(totalLabel);
    float totalAmountWidth = boldPaint.measureText(totalAmountStr);

    float totalAmountX = colEnd - 5 - totalAmountWidth;
    float totalLabelX = totalAmountX - 10 - totalLabelWidth;


    y += rowHeight + 20;
    y += 30;
    // Mode of Payment (shifted down)
    canvas.drawText("Mode of Payment :", x, y, boldPaint);
    canvas.drawText("Online", x + 120, y, paint);

    pdfDocument.finishPage(page);

    try {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloadsDir.exists()) downloadsDir.mkdirs();
        File file = new File(downloadsDir, "fee_receipt_" + studentId + ".pdf");

        FileOutputStream outputStream = new FileOutputStream(file);
        pdfDocument.writeTo(outputStream);
        outputStream.close();

        Toast.makeText(this, "PDF saved to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
    } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    } finally {
        pdfDocument.close();
    }
}

    // Runtime permission check
private boolean checkPermission() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
}

    private void requestPermission() {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
}

    @Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                       @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSION_REQUEST_CODE &&
            grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        fetchAndGenerateReceipt();
    } else {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    }
}

    private void showLoading(boolean loading) {
    progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    btnPayNow.setEnabled(!loading);
    if (loading) {
        txtDepartment.setText("Loading...");
        txtSemester.setText("Loading...");
        txtAmount.setText("Loading...");
        txtStatus.setText("");
    }
}

    @Override
public void onPaymentError(int code, String response) {
    Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_SHORT).show();
}
}


