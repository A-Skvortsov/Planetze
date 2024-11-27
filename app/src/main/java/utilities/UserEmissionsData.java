package utilities;

import static utilities.Constants.DAILY;
import static utilities.Constants.DEFAULT_DATE;
import static utilities.Constants.EMISSIONS_AMOUNT_INDEX;
import static utilities.Constants.EMISSION_TYPE_INDEX;
import static utilities.Constants.MONTHLY;
import static utilities.Constants.WEEKLY;
import static utilities.Constants.YEARLY;

import com.example.planetze.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import customDataStructures.EmissionNode;
import customDataStructures.EmissionsNodeCollection;

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

    public List<EmissionsNodeCollection> getUserEmissionsDataKG(char timePeriod) {
        if (data == null)
            return null;

        switch(timePeriod) {
            case DAILY:
                return getDailyEmissionsData();
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

    private float getDailyEmissions() {
        return getTotalEmissions(1);
    }

    private float getWeeklyEmissions() {
        return getTotalEmissions(7);
    }

    private float getMonthlyEmissions() {
        return getTotalEmissions(31);
    }

    private float getYearlyEmissions() {
        return getTotalEmissions(365);
    }

    private float getOverallEmissions() {
        return getTotalEmissions(Integer.MAX_VALUE);
    }

    private List<EmissionsNodeCollection> getDailyEmissionsData() {
        return generatedEmissionsData(2);
    }

    private List<EmissionsNodeCollection> getWeeklyEmissionsData() {
        return generatedEmissionsData(7);
    }

    private List<EmissionsNodeCollection> getMonthlyEmissionsData() {
        return generatedEmissionsData(31);
    }

    private List<EmissionsNodeCollection> getYearlyEmissionsData() {
        return generatedEmissionsData(365);
    }

    private List<EmissionsNodeCollection> getOverallEmissionsData() {
        return generatedEmissionsData(Integer.MAX_VALUE);
    }

    private float getTotalEmissions(int days) {
        List<EmissionsNodeCollection> emissionsData = generatedEmissionsData(days);

        if (emissionsData == null || emissionsData.isEmpty()) {
            return 0;
        }

        float totalEmissions = 0;
        for (EmissionsNodeCollection emissionsNodeCollection : emissionsData) {
            for (EmissionNode node : emissionsNodeCollection.getData()) {
                totalEmissions += node.getEmissionsAmount();
            }
        }

        return totalEmissions;
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
            emissions += Float.parseFloat((String) activity.get(EMISSIONS_AMOUNT_INDEX));
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

    public List<EmissionsNodeCollection> generatedEmissionsData(int days) {
        // Make sure there is enough data to use to interpolate
        if (data == null || data.size() < 2) {
            return null;
        }

        Date date = Calendar.getInstance().getTime();
        String x2 = simpleDateFormat.format(date);
        String x1 = x2;

        Set<String> addedDates = new HashSet<>();

        List<EmissionsNodeCollection> chartData = new ArrayList<>();


        for (int i = sortedDates.size() - 1; i >= 1; i--) {
            if (sortedDates.contains(x2) && !addedDates.contains(x2)) {
                EmissionsNodeCollection collection = dataToEmissionsNodesCollection(x2, data.get(x2));
                chartData.add(0, collection);
                addedDates.add(x2);
                x2 = getTheDayBefore(x2);
                x1 = getLastAvailableDate(x2);
            } else if (chartData.isEmpty()) {
                x1 = getLastAvailableDate(x2);
                EmissionsNodeCollection collection = dataToEmissionsNodesCollection(x2, data.get(x1));
                chartData.add(0, collection);
                addedDates.add(x2);
                x2 = getTheDayBefore(x2);
                x1 = getLastAvailableDate(x2);

                i++; /* Ignore the iteration as new data was created */
            } else {
                EmissionsNodeCollection dataForX1 = dataToEmissionsNodesCollection(x1, data.get(x1));

                EmissionsNodeCollection interpolatedCollection = interpolateData(dataForX1, chartData.get(0), x2);
                chartData.add(0, interpolatedCollection);
                addedDates.add(x2);

                x2 = getTheDayBefore(x2);
                x1 = getLastAvailableDate(x2);

                i++; /* Ignore the iteration as new data was created */
            }

            if (chartData.size() == days) {
                break;
            }
        }

        return chartData;
    }

    private EmissionsNodeCollection interpolateData(EmissionsNodeCollection data1, EmissionsNodeCollection data2, String newDate) {
        if (daysBetweenDates(data1.getDate(), data2.getDate()) < 0
                && daysBetweenDates(data1.getDate(), newDate) <= 0
                && daysBetweenDates(newDate, data2.getDate()) >= 0) {
            return null;
        }

        EmissionsNodeCollection interpolatedCollection = new EmissionsNodeCollection(newDate);

        ArrayList<EmissionNode> nodes1 = data1.getData();
        ArrayList<EmissionNode> nodes2 = data2.getData();

        Map<String, Float> emissionMap1 = new HashMap<>();
        Map<String, Float> emissionMap2 = new HashMap<>();

        for (EmissionNode node : nodes1) {
            emissionMap1.put(node.getEmissionType(), node.getEmissionsAmount());
        }

        for (EmissionNode node : nodes2) {
            emissionMap2.put(node.getEmissionType(), node.getEmissionsAmount());
        }

        Set<String> allEmissionTypes = new HashSet<>(emissionMap1.keySet());
        allEmissionTypes.addAll(emissionMap2.keySet());

        for (String emissionType : allEmissionTypes) {
            float value1 = emissionMap1.getOrDefault(emissionType, 0f);
            float value2 = emissionMap2.getOrDefault(emissionType, 0f);

            int daysBetweenData1AndNewDate = daysBetweenDates(data1.getDate(), newDate);
            int daysBetweenData1AndData2 = daysBetweenDates(data1.getDate(), data2.getDate());

            // Apply the linear interpolation formula to estimate the emissions value
            float interpolatedEmissionsValue = value1 + ((value2 - value1) * daysBetweenData1AndNewDate) / daysBetweenData1AndData2;

            EmissionNode interpolatedNode = new EmissionNode(emissionType, interpolatedEmissionsValue);

            interpolatedCollection.addData(interpolatedNode);
        }

        return interpolatedCollection;
    }



    private int daysBetweenDates(String date1, String date2) {
        if (date1 == null || date2 == null) {
            return -1;
        }

        try {
            Date parsedDate1 = simpleDateFormat.parse(date1);
            Date parsedDate2 = simpleDateFormat.parse(date2);

            // Make sure the parsed dates are not null
            assert parsedDate1 != null;
            assert parsedDate2 != null;

            // Calculate the time difference between the two dates in milliseconds
            long diffInMillis = parsedDate2.getTime() - parsedDate1.getTime();

            // Convert the time difference in milliseconds to days
            return (int) (diffInMillis / (1000 * 60 * 60 * 24));
        } catch(ParseException e) {
            throw new RuntimeException("An error occurred while the program was calculated the "
                                        + "number of days between to dates. " + e);
        }

    }

    public EmissionsNodeCollection dataToEmissionsNodesCollection(String date, Object data) {
        if (data == null) {
            return null;
        }

        EmissionsNodeCollection collection = new EmissionsNodeCollection(date);

        List<List<Object>> dataList = (List<List<Object>>) data;

        for (List<Object> row : dataList) {
            String emissionType = (String) row.get(EMISSION_TYPE_INDEX);
            float emissionAmount = Float.parseFloat(row.get(EMISSIONS_AMOUNT_INDEX).toString());

            EmissionNode node = new EmissionNode(emissionType, emissionAmount);
            collection.addData(node);
        }

        return collection;
    }

}
