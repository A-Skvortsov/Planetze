package utilities;

import static utilities.Constants.DAILY;
import static utilities.Constants.MONTHLY;
import static utilities.Constants.OVERALL;
import static utilities.Constants.WEEKLY;
import static utilities.Constants.YEARLY;

import android.content.Context;

import com.example.planetze.R;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CountryEmissions is a simple class with a single function o access the per capita emissions
 * for each country from the stored map.
 * <p>
 * The data is from a the resource file 'global_averages.csv' stored in res/raw
 */
public class CountryEmissionsData {

    // Map to store the country names with their per capita emissions
    private final Map<String, Double> countryToEmissionsMap = new HashMap<>();

    /**
     * Constructor for the CountryEmissions class.
     * This constructor loads the emissions data from a global_averages CSV file into a HashMap.
     *
     * @param context The context of the application environment.
     * @throws RuntimeException If there is an error reading the CSV file or parsing its contents.
     */
    public CountryEmissionsData(Context context) {
        try {
            // Open the CSV file from resource directory
            InputStream inputStream = context.getResources().openRawResource(R.raw.global_averages);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

            // Skip the header
            reader.readNext();

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // Store Countries along side their Emissions (per capita)
                countryToEmissionsMap.put(nextLine[0], Double.valueOf(nextLine[1]));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Public method to retrieve 'comparable' emissions data of a given country.
     *<p>
     * Comparable emissions data is created based on the time period.
     *
     *
     * @param country The name of the country.
     * @param timePeriod Time period for emissions data in the following form
     *                   ('D': Daily, 'W': Weekly, 'M': Monthly 'Y': Yearly, 'O' Overall)
     * @return The the comparable emissions data based on the country and time period provided
     * or null if the country data is not found.
     */
    public Double getComparableEmissionsDataKG(String country, char timePeriod) {
        Double emissions = getCountryPerCapitaEmissionsKG(country);

        switch(timePeriod) {
            case DAILY:
                return emissions / 356.0;
            case WEEKLY:
                return emissions * 7 / 356.0;
            case MONTHLY:
                return emissions * 30 / 356.0;
            case YEARLY:
                return emissions;
            case OVERALL:
                // TODO: Find a comparable overall emission
                return emissions;
            default:
                return null;
        }
    }

    /**
     * Private method to retrieve the per capita emissions of given country.
     *
     * @param country The name of the country.
     * @return The per capita emissions of the given country in kilograms,
     * or null if the country data is not found.
     */
    private Double getCountryPerCapitaEmissionsKG(String country) {
        return countryToEmissionsMap.get(country) == null?
                null : countryToEmissionsMap.get(country) * 1000;
    }
}
