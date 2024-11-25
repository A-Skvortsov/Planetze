package utilities;

import static utilities.Constants.DAILY;
import static utilities.Constants.DEFAULT_DATE;
import static utilities.Constants.EMISSIONS_INDEX;
import static utilities.Constants.MONTHLY;
import static utilities.Constants.WEEKLY;
import static utilities.Constants.YEARLY;

import com.example.planetze.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class UserEmissionsData {
    private String userId;
    private HashMap<String, Object> data;
    private List<String> sortedDates;
    private DataReadyListener listener;

    private SimpleDateFormat simpleDateFormat;

    public interface DataReadyListener {
        void onDataReady();
        void onError(String errorMessage);
    }

    public UserEmissionsData(String userId, DataReadyListener listener) {
        this.userId = userId;
        this.listener = listener;
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        fetchData(); // Fetch data when the object is created
    }

    private void fetchData() {
        Database db = new Database();
        String path = "user data/" + userId + "/calendar";

        db.mReadDataOnce(path, new Database.OnGetDataListener() {
            @Override
            public void onStart() {
                // TODO: Show progress dialog
                System.out.println("START");
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    data = (HashMap<String, Object>) dataSnapshot.getValue();
                    System.out.println("SUCCESS");
                    System.out.println(data);

                    sortedDates = new ArrayList<>(data.keySet());

                    // Sort dates in descending order i.e. 1/1/2024 comes before 1/1/2023
                    sortedDates.sort((date1, date2) -> {
                        try {
                            // Compare the dates
                            return Objects.requireNonNull(simpleDateFormat.parse(date2)).compareTo(simpleDateFormat.parse(date1));
                        } catch (ParseException e) {
                            throw new RuntimeException("An error occurred while sorting the keys in the data HashMap. " + e);
                        }
                    });

                    if (listener != null) {
                        listener.onDataReady();
                    }
                } else {
                    if (listener != null) {
                        listener.onError("No data available for user.");
                    }
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                System.out.println("FAIL");
                if (listener != null) listener.onError("Error fetching data: " + databaseError.getMessage());
            }
        });
    }


    /**
     * Gets the past 29 days of the calendar.
     * @return
     */
    public List<String> getPast29Days(String date) {
        List<String> past29Days = new ArrayList<>();

        //convert date string to year, month, day integers
        String[] ymdStrings = date.split("-");
        int[] ymdInts = new int[3];
        for (int i = 0; i < ymdStrings.length; i++)
            ymdInts[i] = Integer.parseInt(ymdStrings[i]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(ymdInts[0],
                ymdInts[1] - 1,  /* note here that months in Calendar class go from 0-11, but we have them set from 1-12. So subtract 1 from ours */
                ymdInts[2]);
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //iterates back one day since saveActivity
        //already adds the activity for the current day

        //adds past 29 days
        for (int i = 0; i < 29; i++) {
            String d = calendar.get(Calendar.YEAR) +"-"+(calendar.get(Calendar.MONTH)+1)+"-"
                    +calendar.get(Calendar.DAY_OF_MONTH);
            past29Days.add(d);
            calendar.add(Calendar.DAY_OF_MONTH, -1);  //iterates back one day
        }

        return past29Days;
    }

    public float getOverallEmissions() {
        if (data.size() <= 1) {
            return 0;
        }

        float emissions = 0;

        Date c = Calendar.getInstance().getTime();

        String formattedDate = simpleDateFormat.format(c);

        while (!formattedDate.equals(DEFAULT_DATE)) {
            emissions += sumEmissions(formattedDate);
            formattedDate = getLastAvailableDate(formattedDate);
        }

        return emissions;
    }

    public float getUserEmissionsKG(char timePeriod) {
        if (data == null) {
            return 0;
        }

        switch(timePeriod) {
            case DAILY:
                return getDailyEmissions();
            case MONTHLY:
                return getMonthlyEmissions();
            case WEEKLY:
                return getWeeklyEmissions();
            case YEARLY:
                return getYearlyEmissions();
            default:
                return getOverallEmissions();
        }
    }

    public Object getUserEmissionsDateKG(char timePeriod) {
        if (data == null)
            return null;

        switch(timePeriod) {
            case DAILY:
                return getUserDailyEmissionsData();
            case MONTHLY:
                return getMonthlyEmissionsData();
            case WEEKLY:
                return getWeeklyEmissionsData();
            case YEARLY:
                return getYearlyEmissionsData();
            default:
                return getOverallEmissionsData();
        }
    }

    private Object getUserDailyEmissionsData() {
        if (data.size() <= 1) {
            return 0;
        }

        Date c = Calendar.getInstance().getTime();

        String formattedDate = simpleDateFormat.format(c);

        if (data.containsKey(formattedDate) && !formattedDate.equals(DEFAULT_DATE)) {
            return data.get(formattedDate);
        }

        String lastAvailableDate = getLastAvailableDate(formattedDate);

        // TODO: Change this to use the results from the survey
        if (!lastAvailableDate.equals(DEFAULT_DATE)) {
            return data.get(lastAvailableDate);
        }
        return null;
    }

    private Object getWeeklyEmissionsData() {
        return null;
    }

    private Object getMonthlyEmissionsData() {
        return null;
    }

    private Object getOverallEmissionsData() {
        return null;
    }

    private Object getYearlyEmissionsData() {
        return null;
    }

    private float getAverageEmissionsUpTo(String date) {
        if (data.isEmpty()) {
            return 0;
        }

        float emissions = 0;

        for (int i = sortedDates.size() - 1; i >= 0; i--) {
            try {
                if (date.equals(DEFAULT_DATE) && Objects.requireNonNull(sortedDates.get(i)).compareTo(date) <= 0) {
                    continue;
                }
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while iterating through sorted dates." + e);
            }


            Object dateData = data.get(sortedDates.get(i));

            List<List<Object>> activities = (List<List<Object>>) dateData;

            for (List<Object> activity : activities) {
                emissions += Float.parseFloat((String) activity.get(EMISSIONS_INDEX));
            }
        }
        return emissions / (float) (data.size() - 1);
    }

    private float getDailyEmissions() {
        if (data.size() <= 1) {
            return 0;
        }

        System.out.println(getAverageEmissionsUpTo("2024-10-10"));

        Date c = Calendar.getInstance().getTime();

        String formattedDate = simpleDateFormat.format(c);

        if (data.containsKey(formattedDate) && !formattedDate.equals(DEFAULT_DATE)) {
            return sumEmissions(formattedDate);
        }

        String lastAvailableDate = getLastAvailableDate(formattedDate);

        // TODO: Change this to use the results from the survey
        if (!lastAvailableDate.equals(DEFAULT_DATE)) {
            return sumEmissions(lastAvailableDate);
        }
        return 0;
    }

    private float getWeeklyEmissions() {
        if (data.size() <= 1) {
            return 0;
        }

        float emissions = 0;

        Date c = Calendar.getInstance().getTime();

        String formattedDate = simpleDateFormat.format(c);

        for (int i = 0; i < 7; i++) {
            emissions += sumEmissions(formattedDate);
            formattedDate = getLastAvailableDate(formattedDate);
        }

        return emissions;
    }

    private float getMonthlyEmissions() {
        if (data.size() <= 1) {
            return 0;
        }

        float emissions = 0;

        Date c = Calendar.getInstance().getTime();

        String formattedDate = simpleDateFormat.format(c);

        for (int i = 0; i < 365; i++) {
            emissions += sumEmissions(formattedDate);
            formattedDate = getLastAvailableDate(formattedDate);
        }

        return emissions;
    }

    private float getYearlyEmissions() {
        if (data.size() <= 1) {
            return 0;
        }

        float emissions = 0;

        Date c = Calendar.getInstance().getTime();

        String formattedDate = simpleDateFormat.format(c);

        for (int i = 0; i < 365; i++) {
            emissions += sumEmissions(formattedDate);
            formattedDate = getLastAvailableDate(formattedDate);
        }

        return emissions;
    }

    private float sumEmissions(String date) {
        if (data == null || !data.containsKey(date) || date.equals(DEFAULT_DATE)) {
            return 0;
        }

        float emissions = 0;

        Object dateData = data.get(date);
        List<List<Object>> activities = (List<List<Object>>) dateData;

        assert activities != null;
        for (List<Object> activity : activities) {
            emissions += Float.parseFloat((String) activity.get(EMISSIONS_INDEX));
        }

        return emissions;
    }

    private String getLastAvailableDate(String date) {
        if (date == null) {
            return null;
        }

        for (String sortedDate : sortedDates) {
            try {
                // Check if a new date comes before the 'date'
                if (Objects.requireNonNull(simpleDateFormat.parse(sortedDate)).compareTo(simpleDateFormat.parse(date)) < 0) {
                    return sortedDate;
                }
            } catch (ParseException e) {
                throw new RuntimeException("An error occurred while parsing through the sortedDate list. " + e);
            }
        }
        return null;
    }

    private String getTheDayBefore(String date) {
        String[] dateSegments = date.split("-");

        int[] ymdInts = new int[3];
        for (int i = 0; i < dateSegments.length; i++) {
            ymdInts[i] = Integer.parseInt(dateSegments[i]);
        }

        Calendar calendar = Calendar.getInstance();

        // note here that months in Calendar class go from 0-11, but we have them set from 1-12. So subtract 1 from ours.
        calendar.set(ymdInts[0], ymdInts[1] - 1, ymdInts[2]);

        calendar.add(Calendar.DAY_OF_MONTH, -1);  // Stores the previous day

        // Add one to the month since months in Calendar class go from 0-11 and return the day before in the yyyy-mm-dd format.
        return calendar.get(Calendar.YEAR) +"-"+(calendar.get(Calendar.MONTH)+1)+"-"
                +calendar.get(Calendar.DAY_OF_MONTH);
    }
}
