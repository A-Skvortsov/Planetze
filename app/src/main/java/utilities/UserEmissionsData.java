package utilities;

import static utilities.Constants.EMISSIONS_INDEX;

import com.example.planetze.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

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
    private DataReadyListener listener;

    public interface DataReadyListener {
        void onDataReady();
        void onError(String errorMessage);
    }

    public UserEmissionsData(String userId, DataReadyListener listener) {
        this.userId = userId;
        this.listener = listener;
        fetchData(); // Fetch data when the object is created
    }

    private void fetchData() {
        Database db = new Database();
        String path = "user data/" + userId + "/calendar";

        db.mReadDataOnce(path, new Database.OnGetDataListener() {
            @Override
            public void onStart() {
                // TODO: Show progress dialog
            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    data = (HashMap<String, Object>) dataSnapshot.getValue();
                    System.out.println(data);
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


    public float getUserDailyEmissions() {
        if (data.size() <= 1) {
            return 0;
        }

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = simpleDateFormat.format(c);

        if (data.containsKey(formattedDate) && !formattedDate.equals("0000-00-00")) {
            return sumEmissions(formattedDate);
        }

        String lastAvailableDate = getLastAvailableDate(formattedDate);

        // TODO: Change this to use the results from the survey
        if (!lastAvailableDate.equals("0000-00-00")) {
            return sumEmissions(lastAvailableDate);
        }
        return 0;
    }

    public Object getUserDailyEmissionsData() {
        if (data.size() <= 1) {
            return 0;
        }

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = simpleDateFormat.format(c);

        if (data.containsKey(formattedDate) && !formattedDate.equals("0000-00-00")) {
            return data.get(formattedDate);
        }

        String lastAvailableDate = getLastAvailableDate(formattedDate);

        // TODO: Change this to use the results from the survey
        if (!lastAvailableDate.equals("0000-00-00")) {
            return data.get(lastAvailableDate);
        }
        return null;
    }

    public float getUserWeeklyEmissions() {
        return 0F;
    }

    public float getUserYearlyEmissions() {
        return 0F;
    }

    public float getUserOverallEmissions() {
        if (data.isEmpty()) {
            return 0;
        }

        float emissions = 0;

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String date = entry.getKey();
            Object dateData = entry.getValue();

            if (date.equals("0000-00-00")) {
                continue;
            }

            List<List<Object>> activities = (List<List<Object>>) dateData;

            for (List<Object> activity : activities) {
                emissions += Float.parseFloat((String) activity.get(EMISSIONS_INDEX));
            }
        }
        return emissions;
    }

    private float sumEmissions(String date) {
        if (data == null || !data.containsKey(date)) {
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        List<String> sortedDates = new ArrayList<>(data.keySet());

        // Sort dates in descending order i.e. 1/1/2024 comes before 1/1/2023
        sortedDates.sort((date1, date2) -> {
            try {
                // Compare the dates
                return Objects.requireNonNull(dateFormat.parse(date2)).compareTo(dateFormat.parse(date1));
            } catch (ParseException e) {
                throw new RuntimeException("An error occurred while sorting the keys in the data HashMap. " + e);
            }
        });

        for (String sortedDate : sortedDates) {
            try {
                // Check if a new date comes before the 'date'
                if (Objects.requireNonNull(dateFormat.parse(sortedDate)).compareTo(dateFormat.parse(date)) < 0) {
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
