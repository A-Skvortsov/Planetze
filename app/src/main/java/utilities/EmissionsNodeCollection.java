package utilities;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class EmissionsNodeCollection {
    private String date;
    private ArrayList<EmissionNode> data;

    public EmissionsNodeCollection(String date) {
        this.date = date;
        this.data = new ArrayList<>(); // Initialize the data list
    }

    public String getDate() {
        return date;
    }

    public ArrayList<EmissionNode> getData() {
        return data;
    }

    public ArrayList<EmissionNode> addData(EmissionNode emissionNode) {

        for (EmissionNode node : data) {
            if (node.getEmissionType().equals(emissionNode.getEmissionType())) {
                float newAmount = node.getEmissionsAmount() + emissionNode.getEmissionsAmount();
                data.remove(node);
                data.add(new EmissionNode(emissionNode.getEmissionType(), newAmount));
                return data;
            }
        }

        data.add(emissionNode);
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        return "Date: " + date + ", Data: " + data;
    }
}
