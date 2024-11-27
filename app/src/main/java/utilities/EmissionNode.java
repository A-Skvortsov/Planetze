package utilities;

public class EmissionNode {
    private String emissionType;
    private float emissionAmount;

    public EmissionNode(String emissionType, float emissionAmount) {
        this.emissionType = emissionType;
        this.emissionAmount = emissionAmount;
    }

    public String getEmissionType() {
        return emissionType;
    }

    public float getEmissionsAmount() {
        return emissionAmount;
    }

    public String toString() {
        return "Emission Type: " + emissionType + ", Emissions Amount: " + emissionAmount;
    }
}
