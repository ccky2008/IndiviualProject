package com.bignerdranch.android.fyp.network;

import android.content.Context;
import android.util.Log;

import com.bignerdranch.android.fyp.utils.GenericDate;
import com.bignerdranch.android.fyp.utils.SharedMethod;
import com.bignerdranch.android.fyp.models.Appointment;
import com.bignerdranch.android.fyp.models.CommentNew;
import com.bignerdranch.android.fyp.models.Course;
import com.bignerdranch.android.fyp.models.MultipleQuestion;
import com.bignerdranch.android.fyp.models.OpenQuestion;
import com.bignerdranch.android.fyp.models.Post;
import com.bignerdranch.android.fyp.models.Quiz;
import com.bignerdranch.android.fyp.models.QuizQuestion;
import com.bignerdranch.android.fyp.models.SingleQuestion;
import com.bignerdranch.android.fyp.models.Student;
import com.bignerdranch.android.fyp.utils.FirebaseLab;
import com.bignerdranch.android.fyp.utils.QueryPreferences;
import com.bignerdranch.android.fyp.utils.UserLab;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 3/6/2017.
 */

public class DataRetrievalUser {

    public interface OnGetDataListener {
        public void onStart();
        public void onSuccess(boolean value);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetPostListener {
        public void onStart();
        public void onSuccess(List<Post> postList);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetCourseListListener {
        public void onStart();
        public void onSuccess(List<Course> courseList);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetCommentListener {
        public void onStart();
        public void onSuccess(List<CommentNew> commenttList);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetAppointmentListener {
        public void onStart();
        public void onSuccess(List<Appointment> appointmentList);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetTeacherListListener {
        public void onStart();
        public void onSuccess(Map<String, String> teacherInfo);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetStringListener {
        public void onStart();
        public void onSuccess(String data);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetCourseListener {
        public void onStart();
        public void onSuccess(Course courseList);
        public void onFailed(FirebaseError firebaseError);
    }
    public interface OnGetStudentListener{
        public void onStart();
        public void onSuccess(Student s);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetQuizListener {
        public void onStart();
        public void onSuccess(List<Quiz> quizList);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetQuizQuestionListener {
        public void onStart();
        public void onSuccess(QuizQuestion question);
        public void onFailed(FirebaseError firebaseError);
    }

    public interface OnGetQuizIdListener {
        public void onStart();
        public void onSuccess(List<String> doneQuizId);
        public void onFailed(FirebaseError firebaseError);
    }
    // three firebase objects are not null
    private static final int SORTITEMNUMBER = 10;
    private List<Post> postsList;
    private static final String TAG = "DataRetrievalUser";
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    Firebase mFirebase;
    DatabaseReference mDatabaseReference;
    Context context;
    private static final String path = "https://fypproject-acd2d.firebaseio.com/";

    public DataRetrievalUser(Context context) {
        this.context = context;
        mFirebase = FirebaseLab.get().getFirebaseDatabaseLink();
        mFirebaseAuth = FirebaseLab.get().getFirebaseAuth();
        mFirebaseUser = FirebaseLab.get().getFirebaseUser();
        mDatabaseReference = FirebaseLab.get().getDatabaseReference();
    }

    public void getUserInfo(final OnGetDataListener listener) {
        listener.onStart();
        String emailAddress = QueryPreferences.getStoredEmail(context);
        final boolean isStudent = QueryPreferences.getIsStudent(context);
        Query queryRef = null;

        if (isStudent) {
            queryRef = mFirebase.child("students").orderByChild("email").equalTo(emailAddress);
        } else {
            queryRef = mFirebase.child("teachers").orderByChild("email").equalTo(emailAddress);
        }

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userInfo : dataSnapshot.getChildren()) {
                    UserLab.get().setUserId((String) userInfo.child("id").getValue());
                    UserLab.get().setUserName((String) userInfo.child("name").getValue());
                    UserLab.get().setUserKey(userInfo.getKey());
                }
                   listener.onSuccess(true);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onFailed(firebaseError);
            }
        });
    }
    public void getPostInfo(final OnGetPostListener listener) {
        listener.onStart();
        postsList = new ArrayList<>();
        Query queryRef = mFirebase.child("post_collection");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postInfo : dataSnapshot.getChildren()) {
                    postsList.add(postInfo.getValue(Post.class));
                }
                //get key get the last 10 items but the last item is the same
                listener.onSuccess(postsList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onFailed(firebaseError);
            }
        });
    }
    public void getPostComment(String postKey, final  OnGetCommentListener listener) {
        listener.onStart();
        final List<CommentNew> commentCollection = new ArrayList<>();
        final Query queryRef = mFirebase.child("comment_collection").orderByChild("postid").equalTo(postKey);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot commentCollectionSnapshot) {
                String key = null;
                for (DataSnapshot snapshot: commentCollectionSnapshot.getChildren()) {
                    key = snapshot.getKey();
                    Log.i(TAG, Long.toString(snapshot.getChildrenCount()));
                }
                String path = "comment_collection/" + key + "/comment";
                Firebase commentPath = mFirebase.child(path);
                commentPath.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot comment: dataSnapshot.getChildren()) {
                            //Log.i(TAG, String.valueOf(comment.getValue()));
                            commentCollection.add(comment.getValue(CommentNew.class));
                        }
                        listener.onSuccess(commentCollection);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onFailed(firebaseError);
            }
        });
    }
    public void getAppointment(String id,boolean isTeacher, final OnGetAppointmentListener listener) {
        listener.onStart();
        Query queryRef = null;
        // This part is tricky due to setVisibleHint method in AppointmentFragment execute before any method
        if (!isTeacher) {
            queryRef = mFirebase.child("appoinment_collection").orderByChild("studentId").equalTo(id);
        } else {
            queryRef = mFirebase.child("appoinment_collection").orderByChild("teacherId").equalTo(id);
        }
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Appointment> appointmentList = new ArrayList<>();
                for (DataSnapshot appointment: dataSnapshot.getChildren()) {
                    appointmentList.add(appointment.getValue(Appointment.class));
                }
                Log.i("teacher", Integer.toString(appointmentList.size()));
                listener.onSuccess(appointmentList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onFailed(firebaseError);
            }
        });
    }
    public void getTeacherNameList(final OnGetTeacherListListener listener) {
        listener.onStart();
        final Map<String,String> teacherInfo = new HashMap<>();
        Query queryRef = mFirebase.child("teachers");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    teacherInfo.put((String)data.child("id").getValue(), (String)data.child("name").getValue());
                }
                //.i(TAG, Integer.toString(teacherInfo.size()));
                listener.onSuccess(teacherInfo);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getCourseCode(final OnGetStringListener listener) {
        listener.onStart();
        final Query courseRef = mFirebase.child("students").child(UserLab.get().getUserKey()).child("courses");
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               String x = (String)dataSnapshot.getValue().toString();
                listener.onSuccess(x);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getCourses(String query, final OnGetCourseListener listener) {
        listener.onStart();
        final Query courseRef = mFirebase.child("courses").orderByChild("id").equalTo(query);
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Course courseOfOne = new Course();
                    for (DataSnapshot courses : dataSnapshot.getChildren()) {
                        courseOfOne.setDay((String) courses.child("day").getValue());
                        courseOfOne.setStartTime((String) courses.child("startTime").getValue());
                        courseOfOne.setEndTime((String) courses.child("endTime").getValue());
                        courseOfOne.setId((String) courses.child("id").getValue());
                        courseOfOne.setName((String) courses.child("name").getValue());
                        courseOfOne.setNoOfStudents((long) courses.child("no_of_users").getValue());
                        courseOfOne.setKey(courses.getKey());
                    }
                    listener.onSuccess(courseOfOne);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
    }

    public void getListOfCourse(String id, final OnGetCourseListListener listener) {
        listener.onStart();
        Query courseListQuery = mFirebase.child("courses").orderByChild("lecturerId").equalTo(id);
        final List<Course> courseList = new ArrayList<>();
        courseListQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courses : dataSnapshot.getChildren()) {
                    Course courseOfOne = new Course();
                    courseOfOne.setDay((String) courses.child("day").getValue());
                    courseOfOne.setStartTime((String) courses.child("startTime").getValue());
                    courseOfOne.setEndTime((String) courses.child("endTime").getValue());
                    courseOfOne.setId((String) courses.child("id").getValue());
                    courseOfOne.setName((String) courses.child("name").getValue());
                    courseOfOne.setNoOfStudents((long) courses.child("no_of_users").getValue());
                    courseOfOne.setMembership(SharedMethod.getAttributeName((HashMap)courses.child("members").getValue()));
                    if (courses.child("rollCall").child(GenericDate.getTodayForRollCall()).exists()) {
                        courseOfOne.setTodayRollCallStudent(SharedMethod.getAttributeName((HashMap)courses.child("rollCall").child(GenericDate.getTodayForRollCall()).getValue()));
                        courseOfOne.setTodayNumStudent(courses.child("rollCall").child(GenericDate.getTodayForRollCall()).getChildrenCount());
                    } else {
                        courseOfOne.setTodayNumStudent(0);
                    }
                    courseList.add(courseOfOne);
                }
                listener.onSuccess(courseList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Get  method successfully
    public void getStudentRollCallCompleted() {
        final Query rollCallRef = mFirebase.child("students").child(UserLab.get().getUserKey()).child("rollCall").child(GenericDate.getTodayForRollCall());
        rollCallRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //HashMap<String, String> saveMap = new HashMap<String, String>()
                    UserLab.get().setRollCall(SharedMethod.findCourses(dataSnapshot.getValue().toString()));
                    Log.i(TAG, "Get Student roll call completed");
                } else {
                    Log.i(TAG, "wrong Link");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getMacAddress(final OnGetDataListener listener) {
        listener.onStart();
        Query x = mFirebase.child("students").child(UserLab.get().getUserKey()).child("macAddress");
        x.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()) {
                   UserLab.get().setMacAddress(dataSnapshot.getValue().toString());
                  listener.onSuccess(true);
               } else {
                   listener.onSuccess(false);
               }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getStudentInfoToShowInCourseList(String studentId, final OnGetStudentListener listener) {
        final Query courseRef = mFirebase.child("students").orderByChild("id").equalTo(studentId);
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot student: dataSnapshot.getChildren()) {
                    Student s = new Student();
                    s.setName((String)student.child("name").getValue());
                    s.setId((String) student.child("id").getValue());
                    s.setIconLink((String) student.child("image").getValue());
                    listener.onSuccess(s);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    public void getQuizInfo(String subject, final OnGetQuizListener listener) {
        Query ref = mFirebase.child("quiz").child(subject);
        final List<Quiz> quizList = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot q: dataSnapshot.getChildren()) {
                    Quiz quiz = new Quiz();
                    quiz.setId((String) q.child("id").getValue());
                    quiz.setAuthor((String) q.child("author").getValue());
                    quiz.setSubject((String) q.child("subject").getValue());
                    quiz.setDate((String) q.child("date").getValue());
                    quiz.setStartTime((String) q.child("start_time").getValue());
                    quiz.setTimeLimit((String) q.child("time_limit").getValue());
                    quiz.setRandomAnswerSequence((boolean) q.child("random_answer_sequence").getValue());
                    quiz.setRandomQuestionSequence((boolean) q.child("random_question_sequence").getValue());
                    quiz.setTotalNum((int)((long) q.child("totalNum").getValue()));
                    quiz.setQuestionNum(SharedMethod.getMapValue((HashMap) q.child("questionNum").getValue()));
                    quizList.add(quiz);
                }
                //Log.i(TAG, "The quiz size is " + Integer.toString(quizList.size()));
                listener.onSuccess(quizList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getQuizQuestion(String subject, int index, final OnGetQuizQuestionListener listener) {
        listener.onStart();
        final String single = "Single";
        final String multiple = "Multiple";
        Query ref = mFirebase.child("quiz_question").child(subject).orderByChild("index").equalTo(index);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String type = (String)snapshot.child("type").getValue();
                    Log.i(TAG, type);
                    if (type.equalsIgnoreCase(single)) {
                        //Log.i(TAG, "Single question");
                       SingleQuestion singleQuestion = snapshot.getValue(SingleQuestion.class);
                       listener.onSuccess(singleQuestion);
                    } else if (type.equalsIgnoreCase(multiple)) {
                        //Log.i(TAG, "Multiple question");
                         MultipleQuestion multipleQuestion = snapshot.getValue(MultipleQuestion.class);
                         listener.onSuccess(multipleQuestion);
                    } else {
                       // Log.i(TAG, "Open question");
                        OpenQuestion openQuestion = snapshot.getValue(OpenQuestion.class);
                        listener.onSuccess(openQuestion);
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getDoneQuizId(String courseCode, String studentId, final OnGetQuizIdListener listener) {
        listener.onStart();
        Query ref = mFirebase.child("quiz_answer_sheet").child(courseCode.toLowerCase()).orderByChild("studentId").equalTo(studentId);
        final List<String> doneQuizId = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    doneQuizId.add((String)snapshot.child("id").getValue());
                }
                listener.onSuccess(doneQuizId);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getAppointmentTime(String date, final String bookTime, final String teacherId, final OnGetDataListener listener) {
        listener.onStart();
        Query ref = mFirebase.child("appoinment_collection").orderByChild("bookingDate").startAt(date).endAt(date);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot x: dataSnapshot.getChildren()) {
                    if(bookTime.equalsIgnoreCase((String) x.child("bookingTime").getValue()) && teacherId.equalsIgnoreCase((String) x.child("teacherId").getValue())) {
                       count++;
                    } else {
                        Log.i(TAG, "Not a slop is same as the time you bok");
                    }
                }
                boolean isValidAppointment = count <= 0;
                listener.onSuccess(isValidAppointment);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
