package com.example.androidfrontend.Api;

import com.example.androidfrontend.Model.FeeDetailsResponse;
import com.example.androidfrontend.Model.FeeStructureResponse;
import com.example.androidfrontend.Model.NotificationModel;
import com.example.androidfrontend.Model.PaymentData;
import com.example.androidfrontend.Model.PaymentRequest;
import com.example.androidfrontend.Model.PaymentResponse;
import com.example.androidfrontend.Model.PaymentStatusResponse;
import com.example.androidfrontend.Model.StudentFee;
import com.example.androidfrontend.Model.StudentListResponse;
import com.example.androidfrontend.Model.SubjectListResponse;
import com.example.androidfrontend.Model.contentlistResponse;
import com.example.androidfrontend.Model.facultylistresponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
//    @GET("Student/getStudentsById/{id}")
//    Call<StudentResponse> getStudentById(@Path("id") String studentId);
//
//    @GET("FeeStructure/getFeeStructurebydepandsem/{depId}/{currentSemester}")
//    Call<FeeStructureResponse> getFeeStructure(@Path("depId") int depId, @Path("currentSemester") int semester);
//
//    @GET("Department/GetDepartmentById/{depId}")
//    Call<DepartmentResponse> getDepartment(@Path("depId") int depId);
//
//    @GET("StudentFess/CheckPaymentStatus/{studentId}/{feeStructureId}")
//    Call<PaymentStatusResponse> checkPaymentStatus(@Path("studentId") String studentId, @Path("feeStructureId") int feeStructureId);
//
//    @GET("StudentFess/GetStudentFee/{studentId}")
//    Call<PaidFeeDetailsResponse> getPaidFeeDetails(@Path("studentId") String studentId);
@GET("FeeStructure/GetExpectedFeeStructure/{studentId}")
Call<FeeStructureResponse> getFeeStructure(
        @Header("Authorization") String token,
        @Path("studentId") int studentId
);

    @GET("api/StudentFess/CheckPaymentStatus/{studentId}/{feeStructureId}")
    Call<PaymentStatusResponse> checkPaymentStatus(
            @Path("studentId") int studentId,
            @Path("feeStructureId") int feeStructureId,
            @Header("Authorization") String authHeader
    );

    @POST("api/StudentFess/StudentFees")
    Call<PaymentResponse> submitPayment(
            @Body PaymentRequest paymentRequest,
            @Header("Authorization") String authHeader
    );
//    @GET("api/student/{studentId}/feepaymentdetails")
//    Call<FeeDetailsResponse> getFeePaymentDetails(
//            @Header("Authorization") String token,
//            @Path("studentId") int studentId
//    );

    @POST("api/StudentFees/SubmitPayment")
    Call<Void> submitPayment(@Body PaymentData paymentData);


    @GET("FeeStructure/GetExpectedFeeStructure/{studentId}")
    Call<FeeDetailsResponse> getFeePaymentDetails(
            @Header("Authorization") String authHeader,
            @Path("studentId") int studentId
    );

    @GET("StudentFess/CheckPaymentStatus/{studentId}/{feeStructureId}")
    Call<PaymentStatusResponse> checkPaymentStatus(
            @Header("Authorization") String authHeader,
            @Path("studentId") int studentId,
            @Path("feeStructureId") int feeStructureId
    );

    @POST("StudentFess/StudentFees")
    Call<Void> savePayment(
            @Header("Authorization") String authHeader,
            @Body PaymentRequest paymentRequest
    );
    @GET("StudentFess/GetStudentFee/{studentId}")
    Call<StudentFee> GetStudentFee(
            @Path("studentId") int studentId
    );

    @GET("Student/getStudentsById/{studentId}")
    Call<StudentListResponse> getStudentById(
            @Path("studentId") int studentId
    );
    @GET("CourseContent/GetByIdforstudent/{studentId}")
    Call<contentlistResponse> getCourseContents(
            @Path("studentId") int studentId);

    @GET("Faculties/GetFacultyBystudentId/{studentId}")
    Call<facultylistresponse> GetFacultyInDepartment(
            @Path("studentId") int studentId);


    @GET("Subject/GetSubjectsByStudentonly/{studentId}")
    Call<SubjectListResponse> GetSubjectsInDepartment(
            @Path("studentId") int studentId);

    @GET("Notifications/notifications/{studentId}")
    Call<List<NotificationModel>> getNotifications(@Path("studentId") int studentId);

    @POST("Notifications/markAllAsRead/{studentId}")
    Call<Void> markAllAsRead(@Path("studentId") int studentId);



}
