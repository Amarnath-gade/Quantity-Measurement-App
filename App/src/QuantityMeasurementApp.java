enum LengthUnit {

    FEET(12.0),
    INCH(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }
}

class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    private double toInches() {
        return value * unit.getConversionFactor();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityLength quantity = (QuantityLength) obj;

        return Double.compare(this.toInches(), quantity.toInches()) == 0;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.name() + ")";
    }
}

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println("Input: " + q1 + " and " + q2);
        System.out.println("Output: Equal (" + q1.equals(q2) + ")");

        System.out.println();

        QuantityLength q3 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q4 = new QuantityLength(36.0, LengthUnit.INCH);

        System.out.println("Input: " + q3 + " and " + q4);
        System.out.println("Output: Equal (" + q3.equals(q4) + ")");

        System.out.println();

        QuantityLength q5 = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength q6 = new QuantityLength(2.0, LengthUnit.YARDS);

        System.out.println("Input: " + q5 + " and " + q6);
        System.out.println("Output: Equal (" + q5.equals(q6) + ")");

        System.out.println();

        QuantityLength q7 = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        QuantityLength q8 = new QuantityLength(2.0, LengthUnit.CENTIMETERS);

        System.out.println("Input: " + q7 + " and " + q8);
        System.out.println("Output: Equal (" + q7.equals(q8) + ")");

        System.out.println();

        QuantityLength q9 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength q10 = new QuantityLength(0.393701, LengthUnit.INCH);

        System.out.println("Input: " + q9 + " and " + q10);
        System.out.println("Output: Equal (" + q9.equals(q10) + ")");
    }
}