package com.bignerdranch.android.fyp.network;

import android.util.Log;

import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.models.Appointment;
import com.bignerdranch.android.fyp.models.CommentNew;
import com.bignerdranch.android.fyp.models.Post;
import com.bignerdranch.android.fyp.models.QuizAnswerSheet;
import com.bignerdranch.android.fyp.utils.FirebaseLab;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 3/5/2017.
 */

public class DataSaving {
    private static final String TAG = "DataSaving";
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    Firebase mFirebase;

    public DataSaving() {
        mFirebaseAuth = FirebaseLab.get().getFirebaseAuth();
        mFirebaseUser  = FirebaseLab.get().getFirebaseUser();
        mFirebase = FirebaseLab.get().getFirebaseDatabaseLink();
    }

    public void saveDataToFirebase(Post post) {
        if (mFirebaseUser == null) {
        } else {
            Firebase postRef = mFirebase.child("post_collection");
            Firebase newPostRef = postRef.push();
            Map<String, Object> postToInsert = new HashMap<String, Object>();
            postToInsert.put("author", post.getAuthor());
            postToInsert.put("title", post.getTitle());
            postToInsert.put("content", post.getContent());
            postToInsert.put("subject", post.getSubject());
            postToInsert.put("date", post.getDate());
            postToInsert.put("solve", post.isSolve());
            postToInsert.put("id", newPostRef.getKey());
            postToInsert.put("comment", 0);
            newPostRef.setValue(postToInsert);
            String postIdKey = newPostRef.getKey();
            saveFirstComment(postIdKey);
        }
    }

    private void saveFirstComment(String postIdKey) {
        Firebase commentRef = mFirebase.child("comment_collection");
        Map<String, Object> firstComment = new HashMap<String, Object>();
        firstComment.put("postid", postIdKey);
        commentRef.push().setValue(firstComment);
    }

    public void saveComment(String postIdKey, final CommentNew commentNew) {
       final Firebase commentRef = mFirebase.child("comment_collection").child("comment").push();
        Query queryRef = mFirebase.child("comment_collection").orderByChild("postid").equalTo(postIdKey);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    String path = "comment_collection/" + key + "/";
                    Map<String,Object> updateComment = commentNew.toMap();
                    Map<String,Object> childUpdate = new HashMap<>();
                    childUpdate.put(commentRef.getKey(),updateComment);
                    mFirebase.child(path).child("comment").updateChildren(childUpdate);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void saveAppointment(Appointment appointment) {
        final Firebase appointmentRef = mFirebase.child("appoinment_collection");
        final Firebase newAppointmentRef = appointmentRef.push();
        Map<String, Object> appointmentToInsert = new HashMap<String, Object>();
        appointmentToInsert.put("bookingDate", appointment.getBookingDate());
        appointmentToInsert.put("bookingTime", appointment.getBookingTime());
        appointmentToInsert.put("bookingLocation", appointment.getBookingLocation());
        appointmentToInsert.put("bookingStatus", appointment.getBookingStatus());
        appointmentToInsert.put("studentId", UserLab.get().getUserId());
        appointmentToInsert.put("studentName", UserLab.get().getUserName());
        appointmentToInsert.put("teacherId", appointment.getTeacherId());
        appointmentToInsert.put("teacherName",appointment.getTeacherName());
        appointmentToInsert.put("id", newAppointmentRef.getKey());
        newAppointmentRef.setValue(appointmentToInsert);
    }

    public void saveUserToken(String token) {
        String userId = UserLab.get().getUserId();
        final Firebase userTokenRef = mFirebase.child("user_token").child(userId).child("notificationToken");
        Map<String, Boolean> tokenToInsert = new HashMap<String, Boolean>();
        tokenToInsert.put(token, true);
        userTokenRef.setValue(tokenToInsert);
    }

    public void saveNotificationToTeacher(String teacherId) {
        final Firebase notificationToTeacher = mFirebase.child("notification_to_teacher");
        final Firebase newNotificationToTeacher = notificationToTeacher.push();
        Map<String, Object> notificationToInsert = new HashMap<String, Object>();
        notificationToInsert.put("id", teacherId);
        newNotificationToTeacher.setValue(notificationToInsert);
    }

    public void saveNotificationToStudent(String studentId) {
        final Firebase notificationToTeacher = mFirebase.child("notification_to_student");
        final Firebase newNotificationToTeacher = notificationToTeacher.push();
        Map<String, Object> notificationToInsert = new HashMap<String, Object>();
        notificationToInsert.put("id", studentId);
        newNotificationToTeacher.setValue(notificationToInsert);
    }

    public void changeAppointmentStatus(String id, String status) {
        final Firebase appointmentRef = mFirebase.child("appoinment_collection");
        Map<String, Object> updateStatus = new HashMap<String, Object>();
        String path = "/" + id + "/" + "bookingStatus";
        updateStatus.put(path, "Success");
        appointmentRef.updateChildren(updateStatus);
    }

    public void saveMacAddress(String address) {
        final Firebase studentRef = mFirebase.child("students");
        Map<String, Object> macAddress = new HashMap<String, Object>();
        String path = "/" + UserLab.get().getUserKey() + "/" + "macAddress";
        macAddress.put(path, address);
        studentRef.updateChildren(macAddress);
    }

    public void saveRollCallToStudent(String coursecode) {
        final Firebase rollCollRef = mFirebase.child("students").child(UserLab.get().getUserKey()).child("rollCall").child(GenericDate.getTodayForRollCall());
        String path = "/" + coursecode;
        Map<String, Object> courseRollCall = new HashMap<>();
        courseRollCall.put(path, GenericDate.getTime());
        rollCollRef.updateChildren(courseRollCall);
    }

    public void saveRollCallToCourse(String key) {
        final Firebase rollCallRef = mFirebase.child("courses").child(key).child("rollCall").child(GenericDate.getTodayForRollCall());
        String path = "/" + UserLab.get().getUserId();
        Map<String, Object> course = new HashMap<>();
        course.put(path, true);
        rollCallRef.updateChildren(course);

    }

    public void increaseCourseRollCallCount(String key) {
        Firebase countRef = mFirebase.child("courses").child(key).child("rollCallTotal").child(GenericDate.getTodayForRollCall()).child("present");
        countRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.i(TAG, " The present number is " + dataSnapshot.getValue().toString());
            }
        });
    }

    public void decreaseCourseRollCallAbsent(String key, final long total) {
        Firebase countRef = mFirebase.child("courses").child(key).child("rollCallTotal").child(GenericDate.getTodayForRollCall()).child("absent");
        countRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(total);
                } else {
                    currentData.setValue((Long) currentData.getValue() - 1);
                }
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.i(TAG, " The absent number is " + dataSnapshot.getValue().toString());
            }
        });
    }

    public String saveStudentAnswerSheet(QuizAnswerSheet sheet, String coursecode, int numOfQuestion, List<Integer> questionNum) {
        Firebase studentSheetRef = mFirebase.child("quiz_answer_sheet").child(coursecode.toLowerCase());
        Firebase newSheetRef = studentSheetRef.push();
        String key = newSheetRef.getKey();
        Map<String, Object> answerToInsert = new HashMap<String, Object>();
        answerToInsert.put("studentId", sheet.getStudentId());
        answerToInsert.put("marked", false);
        answerToInsert.put("date", sheet.getDate());
        answerToInsert.put("id", sheet.getId());
        answerToInsert.put("score", 0);
        answerToInsert.put("total_question_num", numOfQuestion);
        answerToInsert.put("question_num", questionNum);
        newSheetRef.setValue(answerToInsert);
        return key;
    }

    public void updateStudentAnswerSheet(String courseCode, String key, List<String> studentAnswer) {
        final  Firebase answerSheetRef = mFirebase.child("quiz_answer_sheet").child(courseCode.toLowerCase()).child(key);
        String path = "/" + "student_answer";
        Map<String, Object> answerToInsert = new HashMap<String, Object>();
        answerToInsert.put(path, studentAnswer);
        answerSheetRef.updateChildren(answerToInsert);
    }


    public void updateCorrectAnswer(String courseCode, String key , List<Boolean> isCorrectAnswer) {
        final  Firebase answerSheetRef = mFirebase.child("quiz_answer_sheet").child(courseCode.toLowerCase()).child(key);
        String path = "/" + "correct_answer";
        Map<String, Object> answerToInsert = new HashMap<String, Object>();
        answerToInsert.put(path, isCorrectAnswer);
        answerSheetRef.updateChildren(answerToInsert);
    }

    public void updateCommentCount(final Post post) {
        Firebase mRef = mFirebase.child("post_collection").child(post.getId()).child("comment");
        mRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if(currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
            }
        });
    }
}
