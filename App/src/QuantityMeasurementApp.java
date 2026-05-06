enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}

class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    public QuantityLength convertTo(LengthUnit targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double baseValue = toBaseUnit();

        double convertedValue =
                targetUnit.convertFromBaseUnit(baseValue);

        return new QuantityLength(convertedValue, targetUnit);
    }

    public QuantityLength add(
            QuantityLength other,
            LengthUnit targetUnit) {

        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double totalBaseValue =
                this.toBaseUnit() + other.toBaseUnit();

        double resultValue =
                targetUnit.convertFromBaseUnit(totalBaseValue);

        return new QuantityLength(resultValue, targetUnit);
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

        return Double.compare(
                this.toBaseUnit(),
                quantity.toBaseUnit()) == 0;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        QuantityLength q1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        System.out.println(
                "Input: " + q1 + ".convertTo(INCHES)");

        System.out.println(
                "Output: " + q1.convertTo(LengthUnit.INCHES));

        System.out.println();

        QuantityLength q2 =
                new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println(
                "Input: " + q1 + ".add(" + q2 + ", FEET)");

        System.out.println(
                "Output: " + q1.add(q2, LengthUnit.FEET));

        System.out.println();

        QuantityLength q3 =
                new QuantityLength(36.0, LengthUnit.INCHES);

        QuantityLength q4 =
                new QuantityLength(1.0, LengthUnit.YARDS);

        System.out.println(
                "Input: " + q3 + ".equals(" + q4 + ")");

        System.out.println(
                "Output: " + q3.equals(q4));

        System.out.println();

        QuantityLength q5 =
                new QuantityLength(1.0, LengthUnit.YARDS);

        QuantityLength q6 =
                new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println(
                "Input: " + q5 + ".add(" + q6 + ", YARDS)");

        System.out.println(
                "Output: " + q5.add(q6, LengthUnit.YARDS));

        System.out.println();

        QuantityLength q7 =
                new QuantityLength(2.54, LengthUnit.CENTIMETERS);

        System.out.println(
                "Input: " + q7 + ".convertTo(INCHES)");

        System.out.println(
                "Output: " + q7.convertTo(LengthUnit.INCHES));

        System.out.println();

        QuantityLength q8 =
                new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength q9 =
                new QuantityLength(0.0, LengthUnit.INCHES);

        System.out.println(
                "Input: " + q8 + ".add(" + q9 + ", FEET)");

        System.out.println(
                "Output: " + q8.add(q9, LengthUnit.FEET));

        System.out.println();

        System.out.println(
                "Input: LengthUnit.FEET.convertToBaseUnit(12.0)");

        System.out.println(
                "Output: " +
                        LengthUnit.FEET.convertToBaseUnit(12.0));

        System.out.println();

        System.out.println(
                "Input: LengthUnit.INCHES.convertToBaseUnit(12.0)");

        System.out.println(
                "Output: " +
                        LengthUnit.INCHES.convertToBaseUnit(12.0));
    }
}